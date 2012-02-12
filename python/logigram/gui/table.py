import wx

class TableGUI:
    def __init__(self, table, app=None):
        self.table = table
        self.app = app
        self.calculateSize()
        self.createWidgets()
        self.drawTable(self.dc, self.dcM)
        self.drawHorizDomains(self.dc)
        self.drawVertEntities(self.dc)
        self.drawVertDomains(self.dc)
        self.drawHorizEntities(self.dc)
        #self.bitmap.SaveFile("test.bmp", wx.BITMAP_TYPE_BMP)

    def createWidgets(self):
        if self.app == None:
            self.app = wx.PySimpleApp()

        self.sizer = wx.GridBagSizer()
        self.frame = wx.Frame(None, title='Logigram table')
        self.panel = wx.Panel(self.frame, 2, size=(self.size+12,self.size+12))
        self.btnSave = wx.Button(self.frame, 1, 'Save bitmap')

        self.bitmap = wx.EmptyBitmap(self.size+1, self.size+1, True)
        self.dc = wx.MemoryDC()
        self.dc.SelectObject(self.bitmap)
        self.dc.Clear()
        self.dcM = wx.MirrorDC(self.dc, True)

        wx.EVT_PAINT(self.panel, self.onPaint)
        wx.EVT_BUTTON(self.panel, 1, self.onSave)

        self.sizer.Add(self.panel, pos=(0,0), span=(1,1))
        self.sizer.Add(self.btnSave, pos=(1,0), span=(1,1))

        self.font = wx.Font(8, wx.FONTFAMILY_SWISS, wx.FONTSTYLE_NORMAL, wx.FONTWEIGHT_NORMAL)
        self.dc.SetFont(self.font)

        self.frame.SetSizer(self.sizer)
        self.frame.SetAutoLayout(True)
        self.sizer.Fit(self.frame)
        
        self.frame.Show()
        
    def onPaint(self, event=None):
        pdc = wx.PaintDC(self.panel)
        pdc.Blit(0, 0, self.size+1, self.size+1, self.dc, 0, 0,
                 wx.AND, useMask=False)
        event.Skip()

    def calculateSize(self):
        self.header = 100
        self.cellsize = 16
        self.textX = 3
        self.textY = 2
        self.domainsize = self.cellsize * self.table.domains.lenE
        self.size = ((self.table.domains.lenD - 1)* self.domainsize) + self.header

    def drawTable(self, dc, dcmirror):
        lines = [ # (x1, y1, x2, y2)
            (self.header, 0, self.size, 0),
            (self.header, self.cellsize, self.size, self.cellsize),
            (0, self.header, self.size, self.header)
        ]
        for i in range(self.table.domains.lenD - 1):
            c = (i + 1) * self.domainsize + self.header
            L = self.size - (i * self.domainsize)
            lines.append((0, c, L, c))
            for j in range(self.table.domains.lenE):
                c2 = c - (j * self.cellsize)
                lines.append((self.cellsize, c2, L, c2))
        for coordinates in lines:
            dc.DrawLine(*coordinates)
            dcmirror.DrawLine(*coordinates)

    def drawHorizDomains(self, dc):
        for i in range(self.table.domains.lenD - 1):
            d = self.table.domains.domains[i].upper()
            x = self.header + (i * self.domainsize) + self.textX
            y = self.textY
            dc.DrawText(d, x, y) 

    def drawVertDomains(self, dc):
        for i in range(self.table.domains.lenD - 1):
            dnum = i + 1
            d = self.table.domains.domains[dnum].upper()
            x = self.textY
            y = self.size - (i * self.domainsize + self.textX)
            dc.DrawRotatedText(d, x, y, 90)

    def drawHorizEntities(self, dc):
        for i in range(self.table.domains.lenD - 1):
            for j in range(self.table.domains.lenE):
                e = str(self.table.domains.entities[i][j])
                x = self.header + (i * self.domainsize) + (j * self.cellsize) \
                    + self.textX
                y = self.header - self.textY
                dc.DrawRotatedText(e, x, y, 90)

    def drawVertEntities(self, dc):
        for i in range(self.table.domains.lenD - 1):
            dnum = self.table.domains.lenD - 1 - i
            for j in range(self.table.domains.lenE):
                e = str(self.table.domains.entities[dnum][j])
                w, h, descent, externalleading = dc.GetFullTextExtent(e)
                x = self.header - w - self.textX
                y = self.header + (i * self.domainsize) + (j * self.cellsize)
                dc.DrawText(e, x, y)

    def drawValue(self, dc, value, x, y):
        h = self.cellsize / 2
        cellX = self.header + (x * self.cellsize)
        cellY = self.header + (y * self.cellsize)
        lines = [ (cellX + 3, cellY + h, cellX + self.cellsize - 2, cellY + h) ]
        if value: lines.append((
            cellX + h, cellY + 3, cellX + h, cellY + self.cellsize - 2))
        for line in lines:
            dc.DrawLine(*line)

    def drawValues(self):
        for (r, row) in enumerate(self.table.table):
            for (c, cell) in enumerate(row):
                if cell != None:
                    self.drawValue(self.dc, cell, c, r)
        self.panel.Refresh()

    def onSave(self):
        pass

