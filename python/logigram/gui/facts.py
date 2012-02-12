import wx
from factdialogs import EqualFactGUI, UnequalFactGUI
from factdialogs import ComparisonFactGUI, DifferenceFactGUI

class FactsGUI:
    def __init__(self, solver, tablegui, app=None):
        self.solver = solver
        self.tablegui = tablegui
        self.app = app
        self.createWidgets()

    def createWidgets(self):
        if self.app == None:
            self.app = wx.PySimpleApp()

        self.frame = wx.Frame(None, -1, 'Logigram Facts')
        self.szPanel = wx.GridBagSizer()
        self.szFrame = wx.GridBagSizer()
        self.panel = wx.Panel(self.frame, -1)
        self.lbxFacts = wx.ListBox(self.panel, 10)
        self.btnEqual = wx.Button(self.panel, 1, 'Equal')
        self.btnUnequal = wx.Button(self.panel, 2, 'Unequal')
        self.btnComparison = wx.Button(self.panel, 3, 'Comparison')
        self.btnDifference = wx.Button(self.panel, 4, 'Difference')
        self.btnDelete = wx.Button(self.panel, 5, 'Delete')

        self.btnDelete.Disable()

        wx.EVT_BUTTON(self.frame, 1, self.onEqual)
        wx.EVT_BUTTON(self.frame, 2, self.onUnequal)
        wx.EVT_BUTTON(self.frame, 3, self.onComparison)
        wx.EVT_BUTTON(self.frame, 4, self.onDifference)
        wx.EVT_BUTTON(self.frame, 5, self.onFactDelete)
        wx.EVT_LISTBOX(self.frame, 10, self.onFactSelect)

        self.szPanel.Add(self.lbxFacts, pos=(0,0), span=(6,1), flag=wx.EXPAND)
        self.szPanel.Add(self.btnEqual, pos=(0,1))
        self.szPanel.Add(self.btnUnequal, pos=(1,1))
        self.szPanel.Add(self.btnComparison, pos=(2,1))
        self.szPanel.Add(self.btnDifference, pos=(3,1))
        self.szPanel.Add(self.btnDelete, pos=(5,1))

        self.szFrame.Add(self.panel, pos=(0,0))

        self.panel.SetSizer(self.szPanel)
        self.panel.SetAutoLayout(True)
        self.szPanel.Fit(self.panel)

        self.frame.SetSizer(self.szFrame)
        self.frame.SetAutoLayout(True)
        self.szFrame.Fit(self.frame)

        self.frame.Show()

    def onEqual(self, event=None):
        dlg = EqualFactGUI(self)
        self.addFact(dlg.show())

    def onUnequal(self, event=None):
        dlg = UnequalFactGUI(self)
        self.addFact(dlg.show())

    def onComparison(self, event=None):
        dlg = ComparisonFactGUI(self)
        self.addFact(dlg.show())

    def onDifference(self, event=None):
        dlg = DifferenceFactGUI(self)
        self.addFact(dlg.show())

    def onFactDelete(self, event=None):
        pass

    def onFactSelect(self, event=None):
        pass

    def addFact(self, fact):
        if fact != None:
            self.solver.addFact(fact)
            fact.register()
            facts = [ str(fact) for fact in self.solver.facts ]
            self.lbxFacts.Set(facts)
            self.tablegui.drawValues()
