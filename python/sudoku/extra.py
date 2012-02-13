from stats import Stats
from board import Board

class ExtraStats(Stats):
    def __init__(self, board):
        Stats.__init__(self, board)

    def analyze(self):
        Stats.analyze(self)

        self.extra = [ self.values - set(self.board.extra(n))
                       for n in range(4) ]

    def possible(self, r, c):
        e = self.board.coordToExtra(r, c)
        if e == None:
            return Stats.possible(self, r, c)
        else:
            return Stats.possible(self, r, c) & self.extra[e]

    def put(self, r, c, value):
        Stats.put(self, r, c, value)
        e = self.board.coordToExtra(r, c)
        if e != None:
            self.extra[e] -= set([value])

    def remove(self, r, c, value):
        Stats.remove(self, r, c, value)
        e = self.board.coordToExtra(r, c)
        if e != None:
            self.extra[e] |= set([value])

        
class ExtraBoard(Board):
    def __init__(self, statsclass=ExtraStats):
        Board.__init__(self, statsclass)

    def extra(self, num):
        r = ( 1, 1, 4, 4 )[num]
        f = ( 1, 5, 1, 5 )[num]
        t = ( 4, 8, 4, 8 )[num]
        return self.board[r    ][f:t] \
             + self.board[r + 1][f:t] \
             + self.board[r + 2][f:t]

    def coordToExtra(self, r, c):
        if r in ( 0, 4, 8 ) \
        or c in ( 0, 4, 8 ) :
            return None
        else:
            return (r / 4) * 2 + (c / 4)
    
            
