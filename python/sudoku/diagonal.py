from stats import Stats
from board import Board 
        
class DiagStats(Stats):
    def __init__(self, board):
        Stats.__init__(self, board)
    
    def analyze(self):
        Stats.analyze(self)

        self.diag0 = self.values - set(self.board.diag0())
        self.diag8 = self.values - set(self.board.diag8())

    def possible(self, row, col):
        if (row == col):
            return Stats.possible(self, row, col) & self.diag0

        elif (row + col) == 8:
            return Stats.possible(self, row, col) & self.diag8

        else:
            return Stats.possible(self, row, col)

    def put(self, row, col, value):
        Stats.put(self, row, col, value)
        if (row == col):        self.diag0 -= set([value])
        if (row + col) == 8:    self.diag8 -= set([value])

    def remove(self, row, col, value):
        Stats.remove(self, row, col, value)
        if (row == col):        self.diag0 |= set([value])
        if (row + col) == 8:    self.diag8 |= set([value])

class DiagBoard(Board):
    def __init__(self, statsclass=DiagStats):
        Board.__init__(self, statsclass)

    def diag0(self):
        return [ self.board[i][i] for i in range(9) ]

    def diag8(self):
        return [ self.board[i][8-i] for i in range(9) ]

