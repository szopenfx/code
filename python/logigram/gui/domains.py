import wx

class DomainsGUI:
    def __init__(self, app=None):
        self.app = app
        self.domains = []
        self.entities = []
        self.domain = None
        self.entity = None
        self.isDone = False
        self.createWidgets()
        
    def createWidgets(self):
        if self.app == None:
            self.app = wx.PySimpleApp()

        self.frame = wx.Frame(None, title='Logigram domains')
        self.panel = wx.Panel(self.frame)
        self.szFrame = wx.GridBagSizer()
        self.szPanel = wx.GridBagSizer()

        self.lblDomains = wx.StaticText(self.panel, label='Domains:')
        self.lblEntities = wx.StaticText(self.panel, label='Entities:')

        self.lbxDomains = wx.ListBox(self.panel, 11)
        self.lbxEntities = wx.ListBox(self.panel, 12)
        
        self.btnDomainAdd = wx.Button(self.panel, 1, 'Add')
        self.btnDomainDel = wx.Button(self.panel, 2, 'Delete')
        self.btnEntityAdd = wx.Button(self.panel, 3, 'Add')
        self.btnEntityDel = wx.Button(self.panel, 4, 'Delete')
        self.btnDone = wx.Button(self.panel, 5, 'Done')
        
        self.btnDomainDel.Disable()
        self.btnEntityAdd.Disable()
        self.btnEntityDel.Disable()
        self.btnDone.Disable()

        wx.EVT_BUTTON(self.frame, 1, self.onDomainAdd)
        wx.EVT_BUTTON(self.frame, 2, self.onDomainDel)
        wx.EVT_BUTTON(self.frame, 3, self.onEntityAdd)
        wx.EVT_BUTTON(self.frame, 4, self.onEntityDel)
        wx.EVT_BUTTON(self.frame, 5, self.onDone)
        wx.EVT_LISTBOX(self.frame, 11, self.onDomainSelect)
        wx.EVT_LISTBOX(self.frame, 12, self.onEntitySelect)

        self.szPanel.Add(self.lblDomains, pos=(0,0), span=(1,3))
        self.szPanel.Add(self.lblEntities, pos=(0,3), span=(1,3))
        self.szPanel.Add(self.lbxDomains, pos=(1,0), span=(1,3), flag=wx.EXPAND)
        self.szPanel.Add(self.lbxEntities, pos=(1,3), span=(1,3), flag=wx.EXPAND)
        self.szPanel.Add(self.btnDomainAdd, pos=(2,0))
        self.szPanel.Add(self.btnDomainDel, pos=(2,1))
        self.szPanel.Add(self.btnDone, pos=(2,2), span=(1,2), flag=wx.ALIGN_CENTER)
        self.szPanel.Add(self.btnEntityAdd, pos=(2,4))
        self.szPanel.Add(self.btnEntityDel, pos=(2,5))
        
        self.szFrame.Add(self.panel, pos=(0,0))
        
        self.panel.SetSizer(self.szPanel)
        self.panel.SetAutoLayout(True)
        self.szPanel.Fit(self.panel)
        
        self.frame.SetSizer(self.szFrame)
        self.frame.SetAutoLayout(True)
        self.szFrame.Fit(self.frame)
        
        self.frame.Show()
    
    def onDomainAdd(self, event=None):
        title = 'Add domain'
        prompt = 'Enter a unique domain name:'
        dlg= wx.TextEntryDialog(self.frame, prompt, title)
        if dlg.ShowModal() == wx.ID_OK:
            v = dlg.GetValue()
            if v not in self.domains:
                self.domains.append(v)
                self.entities.append([])
                self.updateLists()
    
    def onDomainDel(self, event=None):
        self.domains.remove(self.domains[self.domain])
        self.entities.remove(self.entities[self.domain])
        self.domain = None
        self.entity = None
        self.updateLists()

    def onEntityAdd(self, event=None):
        title = 'Add entity'
        prompt = 'Enter a unique entity name:'
        dlg = wx.TextEntryDialog(self.frame, prompt, title)
        dlg.SetValue('')
        if dlg.ShowModal() == wx.ID_OK:
            v = dlg.GetValue()
            if v not in self.entities[self.domain]:
                self.entities[self.domain].append(v)
                self.updateLists()
    
    def onEntityDel(self, event=None):
        curr = self.entities[self.domain][self.entity]
        self.entities[self.domain].remove(curr)
        self.entity = None
        self.updateLists()
    
    def onDone(self, event=None):
        self.frame.Destroy()
        self.isDone = True

    def onDomainSelect(self, event=None):
        str_entities = [ [ str(e) for e in elist ] for elist in self.entities ]
        self.domain = self.lbxDomains.GetSelection()
        self.lbxEntities.Set(str_entities[self.domain])
        self.updateButtons()

    def onEntitySelect(self, event=None):
        self.entity = self.lbxEntities.GetSelection()
        self.updateButtons()
    
    def updateLists(self):
        str_entities = [ [ str(e) for e in elist ] for elist in self.entities ]
        self.lbxDomains.Set(self.domains)
        if self.domain == None:
            self.lbxEntities.Set([])
        else:
            self.lbxEntities.Set(str_entities[self.domain])
        self.updateButtons()    

    def updateButtons(self):
        self.btnDomainDel.Enable(self.domain != None)
        self.btnEntityAdd.Enable(self.domain != None)
        self.btnEntityDel.Enable((self.domain != None) and (self.entity != None))
        self.btnDone.Enable(self.valuesOK())
        
    def valuesOK(self):
        if len(self.domains) < 2:
            return False
        lenFirst = len(self.entities[0])
        if lenFirst < 2:
            return False
        for elist in self.entities:
            if len(elist) != lenFirst:
                return False
        return True
