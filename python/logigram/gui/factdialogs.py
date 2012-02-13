import wx

from factpanels import DomEntPanel, DomainPanel, OperatorPanel, NumberPanel
from factpanels import ButtonPanel
from logigram.fact import Equal, Unequal, Comparison, Difference

class GenericFactGUI:
    def __init__(self, factGUI):
        self.factgui = factGUI
        self.domains = factGUI.solver.domains
        self.title = 'Set self.title in self.createPanels()!'
        self.createWidgets()

    def createWidgets(self):
        self.dialog = wx.Dialog(self.factgui.frame, -1, '')
        self.sizer = wx.GridBagSizer()

        self.createPanels()

        self.dialog.SetTitle(self.title)
        self.dialog.SetSizer(self.sizer)
        self.sizer.Fit(self.dialog)

    def differenceOK(self):
        dv = self.entity1.result[0]
        dh = self.entity2.result[0]
        if None in [dv, dh]:
            return False
        return dv - dh > 0


class EqualFactGUI(GenericFactGUI):
    def createPanels(self):
        self.title = 'New equality fact'
        self.entity1 = DomEntPanel(self, 'Vertical entity:', 1, pos=(0,0), axis='v')
        self.entity2 = DomEntPanel(self, 'Horizontal entity:', 2, pos=(0,1), axis='h')
        self.buttons = ButtonPanel(self, 3, pos=(1,0), span=(1,2))
    
    def show(self):
        if self.dialog.ShowModal() == wx.ID_OK:
            d1, e1 = self.entity1.result
            d2, e2 = self.entity2.result
            return Equal(self.factgui.solver.table, d1, e1, d2, e2)
        else:
            return None

    def validate(self):
        condition = self.entity1.ready and self.entity2.ready and self.differenceOK()
        self.buttons.enableOK(condition)


class UnequalFactGUI(GenericFactGUI):
    def createPanels(self):
        self.title = 'New unequality fact'
        self.entity1 = DomEntPanel(self, 'Vertical entity:', 1, pos=(0,0), axis='v')
        self.entity2 = DomEntPanel(self, 'Horizontal entity:', 2, pos=(0,1), axis='h')
        self.buttons = ButtonPanel(self, 3, pos=(1,0), span=(1,2))

    def show(self):
        if self.dialog.ShowModal() == wx.ID_OK:
            d1, e1 = self.entity1.result
            d2, e2 = self.entity2.result
            return Unequal(self.factgui.solver.table, d1, e1, d2, e2)
        else:
            return None

    def validate(self):
        condition = self.entity1.ready and self.entity2.ready and self.differenceOK()
        self.buttons.enableOK(condition)


class ComparisonFactGUI(GenericFactGUI):
    def createPanels(self):
        self.title = 'New comparison fact'
        self.entity1 = DomEntPanel(self, 'First entity:', 1, pos=(0,0), axis='a')
        self.entity2 = DomEntPanel(self, 'Second entity:', 2, pos=(0,1), axis='a')
        self.domain = DomainPanel(self, 'Domain:', 3, pos=(0,2))
        self.operator = OperatorPanel(self, 'Operator:', 4, pos=(0,3))
        self.buttons = ButtonPanel(self, 5, pos=(1,0), span=(1,4))

    def show(self):
        if self.dialog.ShowModal() == wx.ID_OK:
            d1, e1 = self.entity1.result
            d2, e2 = self.entity2.result
            domain = self.domain.result
            operator = self.operator.result
            return Comparison(self.factgui.solver.table,
                              d1, e1, d2, e2, domain, operator)
        else:
            return None

    def validate(self):
        condition = self.entity1.ready and self.entity2.ready \
                    and self.domain.ready and self.operator.ready
        self.buttons.enableOK(condition)


class DifferenceFactGUI(GenericFactGUI):
    def createPanels(self):
        self.title = 'New difference fact'
        self.entity1 = DomEntPanel(self, 'First entity:', 1, pos=(0,0), axis='a')
        self.entity2 = DomEntPanel(self, 'Second entity:', 2, pos=(0,1), axis='a')
        self.domain = DomainPanel(self, 'Domain:', 3, pos=(0,2))
        self.number = NumberPanel(self, 'Difference:', 4, pos=(0,3))
        self.buttons = ButtonPanel(self, 5, pos=(1,0), span=(1,4))

    def show(self):
        if self.dialog.ShowModal() == wx.ID_OK:
            d1, e1 = self.entity1.result
            d2, e2 = self.entity2.result
            domain = self.domain.result
            number = self.number.result
            return Difference(self.factgui.solver.table, d1, e1, d2, e2, domain, number)
        else:
            return None

    def validate(self):
        condition = self.entity1.ready and self.entity2.ready \
                    and self.domain.ready and self.number.ready
        self.buttons.enableOK(condition)
