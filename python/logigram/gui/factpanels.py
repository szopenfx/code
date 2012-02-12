import wx
import wx.lib.intctrl

class DomEntPanel:
    def __init__(self, factdlg, caption, num, pos, axis='a', span=(1,1)):
        self.factdlg = factdlg
        self.domains = factdlg.domains
        self.axis = self.createSlicer(axis)
        self.dialog = factdlg.dialog
        self.result = (None, None)
        self.ready = False
        self.createWidgets(caption, num, pos, span)

    def createSlicer(self, axis):
        x = { 'a':self.domains.all,
              'v':self.domains.vertical,
              'h':self.domains.horizontal }
        return x[axis]

    def createWidgets(self, caption, num, pos, span):
        self.panel = wx.Panel(self.dialog)
        self.sizer = wx.GridBagSizer()
        self.label = wx.StaticText(self.panel, -1, label=caption)
        self.cbxDomain = wx.ComboBox(self.panel, num * 100 + 1,
                                     style=wx.CB_READONLY,
                                     choices=self.domains.domains[self.axis])
        self.cbxEntity = wx.ComboBox(self.panel, num * 100 + 2,
                                     style=wx.CB_READONLY)
        self.cbxEntity.Disable()

        wx.EVT_COMBOBOX(self.dialog, num * 100 + 1, self.onDomainSelect)
        wx.EVT_COMBOBOX(self.dialog, num * 100 + 2, self.onEntitySelect)

        self.sizer.Add(self.label, pos=(0,0))
        self.sizer.Add(self.cbxDomain, pos=(1,0), flag=wx.EXPAND)
        self.sizer.Add(self.cbxEntity, pos=(2,0), flag=wx.EXPAND)

        self.panel.SetSizer(self.sizer)
        self.panel.SetAutoLayout(True)
        self.sizer.Fit(self.panel)

        self.factdlg.sizer.Add(self.panel, pos=pos, span=span)

    def onDomainSelect(self, event=None):
        v = self.cbxDomain.GetValue()
        d = self.domains.domainToIdx(v)
        self.cbxEntity.Clear()
        self.cbxEntity.AppendItems(
            [ str(i) for i in self.domains.entities[d] ]
        )
        self.cbxEntity.Enable(True)
        self.ready = False
        self.factdlg.validate()

    def onEntitySelect(self, event=None):
        v = self.cbxDomain.GetValue(), self.cbxEntity.GetValue()
        d, e = self.domains.strToIdx(*v)
        self.result = d, e
        self.ready = True
        self.factdlg.validate()


class ButtonPanel:
    def __init__(self, factdlg, num, pos, span=(1,1)):
        self.factdlg = factdlg
        self.dialog = factdlg.dialog
        self.ready = False
        self.createWidgets(num, pos, span)
        
    def createWidgets(self, num, pos, span):
        self.panel = wx.Panel(self.dialog)
        self.sizer = wx.GridBagSizer()
        self.btnOK = wx.Button(self.panel, num * 100 + 1, 'OK')
        self.btnCancel = wx.Button(self.panel, num * 100 + 2, 'Cancel')

        self.btnOK.Disable()

        wx.EVT_BUTTON(self.dialog, num * 100 + 1, self.onOK)
        wx.EVT_BUTTON(self.dialog, num * 100 + 2, self.onCancel)

        self.sizer.Add(self.btnOK, pos=(0,0), flag=wx.ALIGN_RIGHT)
        self.sizer.Add(self.btnCancel, pos=(0,1), flag=wx.ALIGN_LEFT)

        self.panel.SetSizer(self.sizer)
        self.panel.SetAutoLayout(True)
        self.sizer.Fit(self.panel)

        self.factdlg.sizer.Add(self.panel, pos=pos, span=span,
                               flag=wx.ALIGN_CENTER)

    def onOK(self, event=None):
        self.dialog.EndModal(wx.ID_OK)

    def onCancel(self, event=None):
        self.dialog.EndModal(wx.ID_CANCEL)

    def enableOK(self, enable):
        self.btnOK.Enable(enable)


class OperatorPanel:
    def __init__(self, factdlg, caption, num, pos, span=(1,1)):
        self.factdlg = factdlg
        self.dialog = factdlg.dialog
        self.ready = False
        self.result = None
        self.createWidgets(caption, num, pos, span)

    def createWidgets(self, caption, num, pos, span):
        choices = [ '<', '>', '<=', '>=' ]
        self.panel = wx.Panel(self.dialog)
        self.sizer = wx.GridBagSizer()
        self.label = wx.StaticText(self.panel, -1, label=caption)
        self.cbxOperators = wx.ComboBox(self.panel, num * 100 + 1,
                                     style=wx.CB_READONLY,
                                     choices=choices)

        wx.EVT_COMBOBOX(self.dialog, num * 100 + 1, self.onDomainSelect)

        self.sizer.Add(self.label, pos=(0,0))
        self.sizer.Add(self.cbxOperators, pos=(1,0), flag=wx.EXPAND)

        self.panel.SetSizer(self.sizer)
        self.panel.SetAutoLayout(True)
        self.sizer.Fit(self.panel)

        self.factdlg.sizer.Add(self.panel, pos=pos, span=span)

    def onDomainSelect(self, event=None):
        self.result = self.cbxOperators.GetValue()
        self.ready = True
        self.factdlg.validate()


class DomainPanel:
    def __init__(self, factdlg, caption, num, pos, span=(1,1)):
        self.factdlg = factdlg
        self.domains = factdlg.domains
        self.dialog = factdlg.dialog
        self.result = None
        self.ready = False
        self.createWidgets(caption, num, pos, span)

    def createWidgets(self, caption, num, pos, span):
        self.panel = wx.Panel(self.dialog)
        self.sizer = wx.GridBagSizer()
        self.label = wx.StaticText(self.panel, -1, label=caption)
        self.cbxDomain = wx.ComboBox(self.panel, num * 100 + 1,
                                     style=wx.CB_READONLY,
                                     choices=self.domains.domains)

        wx.EVT_COMBOBOX(self.dialog, num * 100 + 1, self.onDomainSelect)

        self.sizer.Add(self.label, pos=(0,0))
        self.sizer.Add(self.cbxDomain, pos=(1,0), flag=wx.EXPAND)

        self.panel.SetSizer(self.sizer)
        self.panel.SetAutoLayout(True)
        self.sizer.Fit(self.panel)

        self.factdlg.sizer.Add(self.panel, pos=pos, span=span)

    def onDomainSelect(self, event=None):
        v = self.cbxDomain.GetValue()
        self.result = self.domains.domainToIdx(v)
        self.ready = True
        self.factdlg.validate()


class NumberPanel:
    def __init__(self, factdlg, caption, num, pos, span=(1,1)):
        self.factdlg = factdlg
        self.dialog = factdlg.dialog
        self.ready = False
        self.createWidgets(caption, num, pos, span)

    def createWidgets(self, caption, num, pos, span):
        self.panel = wx.Panel(self.dialog)
        self.sizer = wx.GridBagSizer()
        self.label = wx.StaticText(self.panel, -1, label=caption)
        self.number = wx.lib.intctrl.IntCtrl(self.panel, num * 100 + 1)

        wx.EVT_TEXT(self.dialog, num * 100 + 1, self.onText)

        self.sizer.Add(self.label, pos=(0,0))
        self.sizer.Add(self.number, pos=(1,0), flag=wx.EXPAND)

        self.panel.SetSizer(self.sizer)
        self.panel.SetAutoLayout(True)
        self.sizer.Fit(self.panel)

        self.factdlg.sizer.Add(self.panel, pos=pos, span=span)

    def onText(self, event=None):
        self.ready = self.number.GetValue() != 0
        self.result = self.number.GetValue()
        self.factdlg.validate()
