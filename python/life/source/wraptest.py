import wx
import wxwrap

class WrapTest:
    def __init__(self):
        self.CreateWidgets()

    def CreateWidgets(self):
        wxw = wxwrap.WxWrap()
        wxw.DefineEvent("TestOneTwoThree")
        print dir()
        print dir(self)
        print dir(wxw)
        print dir(wxwrap)
        print wxw._WxWrap__binders
        self.app = wx.PySimpleApp()

        self.frame = wxw.NewParent(wx.Frame, title='WxWrap Test')
        self.szFrame = wxw.NewSizer(wx.GridBagSizer)

        self.panel = wxw.New(wx.Panel, size=(700,700), gbpos=(1,0))
        self.buttons = wxw.NewParent(wx.Panel, gbpos=(0,0), gbflag=wx.EXPAND)

        self.szButtons = wxw.NewSizer(wx.GridBagSizer)

        self.button1 = wxw.New(wx.Button, label='Button 1', gbpos=(0,0))
        self.button2 = wxw.New(wx.Button, label='Button 2', gbpos=(0,1))
        self.button3 = wxw.New(wx.Button, label='Button 3', gbpos=(0,2))
        self.button4 = wxw.New(wx.Button, label='Button 4', gbpos=(0,3))

        wxw.Size(self.buttons, self.szButtons)
        wxw.Size(self.frame, self.szFrame)

        self.frame.Show()

    def CreateWodgets(self):
        self.app = wx.PySimpleApp()

        self.frame = wx.Frame(title='WxWrap Test')
        self.szFrame = wx.GridBagSizer()

        self.panel1 = wx.Panel(self.frame, 1)
        self.szFrame.Add(self.panel1, pos=(0,0))

        self.sizer1 = wx.GridBagSizer()

        self.label1 = wxw.New(wx.StaticText, label='Panel 1: The quick brown fox jumpeth over the lazy dogs.')
        self.sizer1.Add(self.label1, pos=(0,0))

        self.panel2 = wx.Panel(self.frame)
        self.sizer2 = wx.GridBagSizer()
        self.label2 = wx.StaticText(self.panel2, label='Panel 2', gbpos=(0,0))
        

    def OnClick(self, event=None):
        print 'clickety'

if __name__ == '__main__':
    wt = WrapTest()
    wt.app.MainLoop()
