"""Contains class for easier creation of wxPython forms.

@author: Joost Molenaar
@contact: j.j.molenaar at gmail dot com
@version: 1.0 (24/10/2005)
@license: Copyright(c)2005 Joost Molenaar
"""

import wx
import wx.lib.masked
import types

class WxWrap:
    """Class for easier creation of wxPython forms.

    Normal usage of wxPython::
        import wx

        self.app = wx.PySimpleApp()
        self.frame = wx.Frame(None, title='Example')
        self.panel = wx.Panel(self.frame)
        self.szPanel = wx.GridBagSizer()
        self.szFrame = wx.GridBagSizer()
        BUTTON_ID = wx.NewID()
        self.button = wx.Button(self.panel, BUTTON_ID, label='Click me')
        wx.EVT_BUTTON(self.panel, BUTTON_ID, self.OnButtonClick)
        self.szPanel.Add(self.button, pos=(0,0), span=(1,2))
        self.szFrame.Add(self.panel, pos=(0,0))
        self.panel.SetSizer(self.szPanel)
        self.panel.SetAutoLayout(True)
        self.szPanel.Fit(self.panel)
        self.frame.SetSizer(self.szFrame)
        self.frame.SetAutoLayout(True)
        self.szFrame.Fit(self.frame)
        self.app.MainLoop()
        
    Usage of WxWrap::
        import wx, wxwrap

        wxw = wxwrap.WxWrap()
        self.app = wx.PySimpleApp()
        self.frame = wxw.NewParent(wx.Frame, title='Example')
        self.szFrame = wxw.NewSizer(wx.GridBagSizer)
        self.panel = wxw.NewParent(wx.Panel, gbpos=(0,0))
        self.szPanel = wxw.NewSizer(wx.GridBagSizer)
        self.button = wxw.New(wx.Button, label='Click me', gbpos=(0,1), gbspan=(1,2)
                              OnClick=self.OnButtonClick)
        wxw.Size(self.panel, self.szPanel)
        wxw.Size(self.frame, self.szFrame)
        self.app.MainLoop()

    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @version: 1.0 (24/10/2005)
    @license: Copyright(c)2005 Joost Molenaar

    @cvar _WxWrap__binders:
                    Binder objects/functions of events that are not in the wx
                    module and that therefore cannot be converted in a simple
                    OnPaint --> wx.EVT_PAINT manner. User defined events
                    (L{DefineEvent}) also end up here because they get defined
                    in the L{wxwrap} namespace.
    @cvar _WxWrap__widgets_no_id:
                    List of widgets that don't what an id argument to their
                    constructor.
    @cvar _WxWrap__widgets_no_sz:
                    List of widgets that should not be registered with any
                    sizer.

    @ivar parent:
        Reference to object that will be used as parent when creating new
        objects using L{__CreateWidget}.
    @ivar sizer:
        Refernce to object that will be used as sizer when registering a new
        object using L{__RegisterSizer}.
    """
    __binders = {
        'OnNum': wx.lib.masked.EVT_NUM
    }
    __widgets_no_id = [ wx.Frame ]
    __widgets_no_sz = [ wx.Frame ]

    def __init__(self, parent=None, sizer=None):
        """Initialize a WxWrap object.
        @param parent: Parent to be used when creating controls.
        @param sizer: Sizer to register controls with.
        @type parent: wx.Window object
        @type sizer: wx.GridBagSizer object
        """
        self.parent = parent
        self.sizer = sizer
        self.__call__ = self.New

    def New(self, class_, **kwargs):
        """
        Create a new control with parent C{self.parent} and sizer C{self.sizer}.
        @param class_: Class of control to create
        @param kwargs: Must include keyword argument C{gbpos}. Arguments
                        starting with "gb" will be passed to the GridBagSizer;
                        arguments starting with "On" will be bound to events.
        @type class_: wx.Window class
        @type kwargs: keyword arguments
        @return: Reference to control object.
        @rtype: C{class_} instance
        """
        event_args = self.__RemoveEvents(kwargs)
        sizer_args = self.__RemoveSizerGBS(kwargs)
        widget, wxid = self.__CreateWidget(class_, kwargs)
        self.__RegisterEvents(widget, wxid, event_args)
        self.__RegisterSizer(widget, sizer_args)
        return widget
        
    def Size(self, container, sizer):
        """Fit C{container} to size of contents of C{sizer}.
        @param container: Container to change size of.
        @param sizer: Sizer to act on container.
        @type container: wx.Window object
        @type sizer: wx.GridBagSizer object
        """
        container.SetSizer(sizer)
        container.SetAutoLayout(True)
        sizer.Fit(container)
        
    def SetParent(self, parent):
        """Set the parent for controls to be created.
        @param parent: Parent for subsequent controls.
        @type parent: wx.Container object
        """
        self.parent = parent

    def SetSizer(self, sizer):
        """Set the sizer to which controls will be added.
        @param sizer: Sizer for subsequent controls.
        @type sizer: wx.GridBagSizer object
        """
        self.sizer = sizer
    
    def NewParent(self, *args, **kwargs):
        """Create and register new parent control.
        @param args: Will be passed to L{New} call
        @param kwargs: Will be passed to L{New} call.
        @type args: positional arguments
        @type kwargs: keyword arguments
        @return: object returned by L{New}
        @rtype: wx.Window object
        """
        self.SetParent(self.New(*args, **kwargs))
        return self.parent
    
    def NewSizer(self, class_, *args, **kwargs):
        """Create a new sizer.
        @param class_: Class of sizer (currently only GridBagSizer is
                        supported).
        @param args: Will be passed to constructor of sizer object
        @param kwargs: Wil be passed to constructor of sizer object
        @type class_: wx.GridBagSizer class
        @return: GridBagSizer object
        @rtype: wx.GridBagSizer
        """
        self.SetSizer(class_(*args, **kwargs))
        return self.sizer

    def DefineEvent(self, event):
        """Define a generic event. This event can then be bound through the
        normal way of providing a keyword argument C{OnEvent=self.OnEvent}.
        @param event: Name of event
        @type event: str
        """
        uppercase = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
        upper = 'evt' + event
        for letter in uppercase:
            upper = upper.replace(letter, '_' + letter)
        arguments = { 'EVT':upper.upper(), 'Evt':event }
        generic_event = '%(EVT)s_ID = wx.NewId()\n' + \
        'def %(EVT)s(win, func):\n' + \
        '    win.Connect(-1, -1, %(EVT)s_ID, func)\n' + \
        'class %(Evt)sEvent(wx.PyEvent):\n' + \
        '    def __init__(self, data=None):\n' + \
        '        wx.PyEvent.__init__(self)\n' + \
        '        self.SetEventType(%(EVT)s_ID)\n' + \
        '        self.data = data\n'
        exec generic_event % arguments in globals()
        self.__binders['On' + event] = globals()[upper.upper()]

    def __CreateWidget(self, class_, kwargs):
        """Create a control.
        @param class_: Class of control to be created.
        @param kwargs: Options for control.
        @type class_: wx.Window descendant
        @type kwargs: Keyword arguments
        @return: Instance of C{class}
        @rtype: C{class}
        """
        if class_ in self.__widgets_no_id:
            return (class_(self.parent, **kwargs), None)
        else:
            wxid = wx.NewId()
            return (class_(self.parent, wxid, **kwargs), wxid)
        
    def __RemoveEvents(self, kwargs):
        """Remove all keys that refer to events (that start with 'On')
        and return them in a new dict.
        @param kwargs: Dictionary of keyword arguments
        @type kwargs: Dictionary
        @return: Dictionary of event arguments.
        @rtype: Dictionary
        """
        result = {}
        for key in kwargs.keys():
            if key.startswith('On'):
                result[key] = kwargs[key]
                kwargs.pop(key)
        return result
                
    def __RemoveSizerGBS(self, kwargs):
        """Remove all keys that refer to GridBagSizer arguments (that start
        with 'gb') and return them in a new dict, stripped of their 'gb' prefix.
        @param kwargs: Dictionary of keyword arguments
        @type kwargs: Dictionary
        @return: Dictionary of sizer arguments.
        @rtype: Dictionary
        """
        result = {}
        for key in kwargs.keys():
            if key in [ 'gbpos', 'gbspan', 'gbflag', 'gbborder' ]:
                result[key[2:]] = kwargs[key]
                kwargs.pop(key)
        return result
    
    def __RegisterEvents(self, widget, wxid, events):
        """Register all desired events with this control.
        @param widget: Widget to register events for.
        @param wxid: ID of widget
        @param events: Dictionary of events to bind.
        @type widget: wx.Window object
        @type wxid: Something returned by wx.NewID()
        @type events: Dictionary of event/handler pairs
        @raise TypeError: If event doesn't expect 0 or 1 IDs. The author of this
                          class has never dealt with events that expect less
                          than 0 or more than 1 IDs.
        """
        for key in events:
            try:
                evt = self.__binders[key]
            except KeyError:
                evtname = self.__NiceNameToEventBinder(key)
                evt = wx.__dict__[evtname]
            if isinstance(evt, types.FunctionType) or evt.expectedIDs == 0:
                evt(widget, events[key])
            elif evt.expectedIDs == 1:
                evt(widget, wxid, events[key])
            else:
                msg = "Don't know how to handle events with %d IDs (%s)." % \
                      (evt.expectedIDs, evtname)
                raise TypeError(msg)

    def __RegisterSizer(self, widget, sizerargs):
        """Add a widget to C{self.sizer}.
        @param widget: Widget to register with C{self.sizer}
        @param sizerargs: Arguments to C{self.sizer}.
        @type widget: wx.Window object
        @type sizerargs: Dictionary of arguments for sizer.
        @raise TypeError: If C{gbpos} was missing from call to L{New}.
        """
        if type(widget) not in self.__widgets_no_sz:
            if not sizerargs.has_key('pos'):
                msg = 'Missing keyword argument gbpos.'
                raise TypeError(msg)
            self.sizer.Add(widget, **sizerargs)

    def __NiceNameToEventBinder(self, name):
        """Converts a name like OnEraseBackground to EVT_ERASE_BACKGROUND.
        The 'On' prefix is replaced by 'evt'. Then capital letters are
        transformed to the same letter with a preceding underscore. Then the
        whole string is converted to uppercase.
        @param name: Name of event, for example C{'OnCombobox'}
        @type name: String
        @return: Name of event, for example C{'EVT_COMBOBOX'}
        @rtype: String
        """
        uppercase = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
        name = name.replace('On', 'evt')
        for letter in uppercase:
            name = name.replace(letter, '_' + letter)
        return name.upper()


