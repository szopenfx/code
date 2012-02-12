"""Classes for operating the graphical user interface for Conway's
Game Of Life. The Game of Life is a 23/3 cellular automaton - meaning a cell
will die if it does not have 2 or 3 neighbours and a cell will come to life
if it has 3 neighbours.

@version: 1.0
@author: Joost Molenaar
@contact: j.j.molenaar at gmail dot com
@license: Copyright(c)2005 Joost Molenaar
"""

import lifegrid
import lexicon
import wxwrap

import wx
import wx.lib.masked
import threading
import time

##EVT_RESULT_ID = wx.NewId()
##def EVT_RESULT(win, func):
##    win.Connect(-1, -1, EVT_RESULT_ID, func)
##
##class ResultEvent(wx.PyEvent):
##    def __init__(self, data=None):
##        wx.PyEvent.__init__(self)
##        self.SetEventType(EVT_RESULT_ID)
##        self.data = data

class Abortable(threading.Thread):
    """Abortable thread.

    @version: 1.0
    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @license: Copyright(c)2005 Joost Molenaar
    """
    def __init__(self):
        """Sets up thread object to initially not terminate itself.
        """
        threading.Thread.__init__(self)
        self.want_abort = 0

    def abort(self):
        """Set flag that should abort the thread. It is up to the run method to
        actually implement this in some way.
        """
        self.want_abort = 1

class Worker(Abortable):
    """Thread that iterates the L{lifegrid.LifeGrid} one step forward, then
    posts a ResultEvent back to wxPython using C{wx.PostEvent}.

    @version: 1.0
    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @license: Copyright(c)2005 Joost Molenaar
    """
    def __init__(self, lifegrid, lifegui):
        """Initialize worker thread.
        @param lifegrid: Object to do the work on
        @param lifegui: Object to display the results in
        @type lifegrid: L{lifegrid.LifeGrid}
        @type lifegui: L{life.LifeGUI}
        """
        Abortable.__init__(self)
        self.lifegrid = lifegrid
        self.lifegui = lifegui
        self.start()
        
    def run(self):
        """Actually do the work until L{abort} has been called.
        """
        while self.want_abort == 0:
            self.lifegrid.Iterate()
            wx.PostEvent(self.lifegui.frame, wxwrap.ResultEvent())
            time.sleep(float(self.lifegui.delay) / 1000)
    
class LifeGUI:
    """Class for operating Conways' Game of Life.

    @version: 1.0
    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @license: Copyright(c)2005 Joost Molenaar
    """
    def __init__(self, size, app=None):
        """Initialize LifeGUI instance.
        @param size: Length of the sides of the grid
        @param app: wxApplication object (will be created if neccessary)
        @type size: int
        @type app: wx.PySimpleApp()
        """
        self.size = size
        self.app = app
        self.maxfps = 1000000
        self.delay = 0
        self.lexicon = lexicon.Lexicon('patterns')
        self.CreateWidgets()
        self.lifegrid = lifegrid.UnrolledLifeGrid(self.size)
        self.GetSizeOfPattern()
        self.mposx, self.mposy = (0, 0)
        self.worker = None
        self.Redraw()
    
    def CreateWidgets(self):
        """Create wxPython widgets using L{wxwrap.WxWrap}.
        """
        if self.app == None:
            self.app = wx.PySimpleApp()
        
        if self.size == None:
            dlg = wx.TextEntryDialog(None,
                                     'Enter a grid size:', 'Grid size', '50')
            dlg.ShowModal()
            self.size = int(dlg.GetValue())
        
        self.cellsize = 10
        self.realsize = self.size * self.cellsize
        
        wxw = wxwrap.WxWrap()
        wxw.DefineEvent('Result')

        self.frame = wxw.NewParent(wx.Frame, title='Game of Life', OnResult=self.OnResult)
        self.szFrame = wxw.NewSizer(wx.GridBagSizer)

        self.panel = wxw.New(wx.Panel, size=(self.realsize, self.realsize), gbpos=(1,0),
                             OnMotion=self.OnMotion, OnEraseBackground=self.OnErase,
                             OnLeftUp=self.OnLeftUp, OnPaint=self.OnPaint)
        self.buttons = wxw.NewParent(wx.Panel, gbpos=(0,0), gbflag=wx.EXPAND)
        self.szButtons = wxw.NewSizer(wx.GridBagSizer)

        self.btnStep = wxw.New(wx.Button, label='Step', gbpos=(0,0), OnButton=self.OnStep)
        self.btnStart = wxw.New(wx.Button, label='Start', gbpos=(0,1), OnButton=self.OnStart)
        self.btnStop = wxw.New(wx.Button, label='Stop', gbpos=(0,2), OnButton=self.OnStop)
        self.btnClear = wxw.New(wx.Button, label='Clear', gbpos=(0,3), OnButton=self.OnClear)
        self.cbxClasses = wxw.New(wx.ComboBox, style=wx.CB_READONLY, gbpos=(0,4), OnCombobox=self.OnSelectClass)
        self.cbxPatterns = wxw.New(wx.ComboBox, style=wx.CB_READONLY, gbpos=(0,5), OnCombobox=self.OnSelectPattern)
        self.cbxRotation = wxw.New(wx.ComboBox, style=wx.CB_READONLY, gbpos=(0,6), OnCombobox=self.OnSelectRotation)
        self.edtMaxFPS = wxw.New(wx.lib.masked.NumCtrl, integerWidth=2, gbpos=(0,7))
    
        self.btnStop.Disable()
        self.cbxClasses.AppendItems(self.lexicon.names)
        self.cbxPatterns.AppendItems(self.lexicon.patterns[0].names)
        self.cbxRotation.AppendItems(self.lexicon.patterns[0].rotations)
        self.cbxClasses.Select(0)
        self.cbxPatterns.Select(0)
        self.cbxRotation.Select(0)
        self.edtMaxFPS.SetValue(0)
        
        self.bitmap = wx.EmptyBitmap(self.realsize, self.realsize)
        self.dc = wx.MemoryDC()
        self.dc.SelectObject(self.bitmap)
        
        wxw.Size(self.buttons, self.szButtons)
        wxw.Size(self.frame, self.szFrame)
        
        self.frame.Show()

        bg = self.panel.GetBackgroundColour()
        self.bg_brush = wx.Brush(bg)
        self.green_brush = wx.GREEN_BRUSH
        self.white_pen = wx.Pen(wx.Colour(
            red=bg.Red()-10, green=bg.Green()-10, blue=bg.Blue()-10
        ))
        self.black_pen = wx.BLACK_PEN
        
    def Redraw(self):
        """Draw the state of the CA on a bitmap using a wx.MemoryDC, then make
        a PaintEvent happen so that L{OnPaint} will get called.
        """
        self.dc.SetBackground(self.bg_brush)
        self.dc.Clear()
        self.dc.SetPen(self.white_pen)
        total = self.size * self.cellsize
        for x in xrange(0, self.size * self.cellsize, self.cellsize):
            self.dc.DrawLine(0, x, total, x)
            self.dc.DrawLine(x, 0, x, total)
        if self.worker == None or self.worker.want_abort:
            self.dc.SetBrush(wx.TRANSPARENT_BRUSH)
            self.dc.SetPen(wx.WHITE_PEN)
            self.dc.DrawRectangle(self.mposx * self.cellsize, self.mposy * self.cellsize,
                                  self.patw * self.cellsize, self.path * self.cellsize)
        self.dc.SetPen(self.black_pen)
        self.dc.SetBrush(self.green_brush)
        for y in xrange(self.size):
            for x in xrange(self.size):
                if self.lifegrid.grid[y][x]:
                    coords = (x * self.cellsize, y * self.cellsize, 
                              self.cellsize, self.cellsize)
                    self.dc.DrawRectangle(*coords)
        self.frame.Refresh()
                    
    def OnPaint(self, event=None):
        """Blit the bitmap onto the wx.PaintDC.
        @param event: Paint event
        @type event: PaintEvent
        """
        pdc = wx.PaintDC(self.panel)
        pdc.Blit(0, 0, self.realsize + 1, self.realsize + 1, self.dc, 0, 0,
                 wx.COPY, useMask=False)
        event.Skip()
    
    def OnStep(self, event=None):
        """Advance the CA one iteration - when btnStep is clicked.
        @param event: Button click event
        @type event: ButtonEvent
        """
        self.lifegrid.Iterate()
        self.Redraw()
    
    def OnLeftUp(self, event=None):
        """Put a pattern from the lexicon in the lifegrid when mouse is clicked.
        @param event: LeftUp event
        @type event: LeftUpEvent
        """
        x = event.GetX() / self.cellsize
        y = event.GetY() / self.cellsize
        class_ = self.cbxClasses.GetSelection()
        pattern = self.cbxPatterns.GetSelection()
        rotation = self.cbxRotation.GetSelection()
        self.lexicon.Put(self.lifegrid, x, y, class_, pattern, rotation)
        self.Redraw()
    
    def OnStart(self, event=None):
        """Start the worker thread when btnStart is clicked.
        @param event: Button click event
        @type event: ButtonClick
        """
        self.time = time.time()
        self.frames = 0
        self.btnStop.Enable()
        self.btnStart.Disable()
        self.worker = Worker(self.lifegrid, self)
    
    def OnStop(self, event=None):
        """Stop the worker thread by calling L{Worker.abort}
        @param event: Button click event
        @type event: ButtonClick
        """
        self.btnStop.Disable()
        self.btnStart.Enable()
        self.worker.abort()
        self.btnStart.SetLabel('Start')
    
    def OnClear(self, event=None):
        """Reset the lifegrid.
        @param event: Button click event
        @type event: ButtonClick
        """
        self.lifegrid.Reset()
        self.Redraw()
    
    def OnResult(self, event=None):
        """Update the state of the panel when the L{Worker} thread has made an
        iteration. Also adjust  the delay time so that the simulation runs at
        the desired frame rate.
        @param event: Result event
        @type event: ResultEvent (defined by L{wxwrap.WxWrap.DefineEvent} in
                     L{LifeGUI.CreateWidgets}
        """
        self.frames += 1
        fps = float(self.frames) / (time.time() - self.time)
        if self.frames > 4:
            print fps, self.maxfps, self.delay
            self.OnMaxFPS()
            self.time = time.time()
            self.frames = 0
        if fps > (self.maxfps + 0.1):
            self.delay *= 1.01
        elif fps < (self.maxfps - 0.1)and self.delay > 0:
            self.delay /= 1.01
        if self.worker.want_abort:
            self.btnStart.SetLabel('Start')
        else:
            self.btnStart.SetLabel('%.1f fps (%d)' % (fps, self.delay))
        self.Redraw()
        
    def OnMaxFPS(self, event=None):
        """Store the maximum number of frames per second that's desired by the
        user. Also initialize the delay for the sleep call to a more or less
        sensible value (is always to high due to genuine processing time.
        @param event: wx.lib.masked.Number event
        @type event: NumberEvent
        @todo: Initialize C{self.delay} to a better value.
        """
        value = self.edtMaxFPS.GetValue()
        if self.maxfps != value:
            if self.maxfps == 0:
                self.maxfps = 1000000
                self.delay = 1
            else:
                self.maxfps = value
                self.delay = 1000.0 / value


    def OnSelectClass(self, event=None):
        """Update the cbxPatterns list when a new class is selected.
        @param event: Combobox event
        @type event: ComboboxEvent
        """
        patterns = self.cbxClasses.GetSelection()
        self.cbxPatterns.Clear()
        self.cbxPatterns.AppendItems(self.lexicon.patterns[patterns].names)
        self.cbxPatterns.SetSelection(0)
        self.szButtons.Fit(self.buttons)
        self.szFrame.Fit(self.frame)
    
    def OnSelectPattern(self, event=None):
        """Store the size of the currently selected pattern.
        @param event: Combobox event
        @type event: ComboboxEvent
        """
        self.GetSizeOfPattern()
    
    def OnSelectRotation(self, event=None):
        """Store the size of the currently selected pattern; adjusted for the
        selected rotation.
        @param event: Combobox event
        @type event: ComboboxEvent
        """
        self.GetSizeOfPattern()
    
    def OnErase(self, event=None):
        """Do absolutely nothing; especially don't make the frame empty when
        Windows requests so.
        @param event: Erase event
        @type event: EraseEvent
        """
        pass # avoid flickering

    def OnMotion(self, event=None):
        """Store the current X and Y value of the mouse pointer when mouse is
        moved.
        @param event: Motion event
        @type event: MotionEvent
        @todo: Disable this event when simulation is running.
        """
        self.mposx = event.GetX() / self.cellsize
        self.mposy = event.GetY() / self.cellsize
        self.Redraw()
    
    def GetSizeOfPattern(self):
        """Look up the size of the current pattern.
        """
        class_ = self.cbxClasses.GetSelection()
        pattern = self.cbxPatterns.GetSelection()
        flipped = self.cbxRotation.GetSelection() in [1, 3]
        p = self.lexicon.patterns[class_].patterns[pattern]
        wh = len(p[0]), len(p)
        if flipped:
            self.path, self.patw = wh
        else:
            self.patw, self.path = wh

if __name__ == '__main__':
    lifegui = LifeGUI(111)
    lifegui.app.MainLoop()
    
