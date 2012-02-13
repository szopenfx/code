from stats import Stats
from board import Board

class ShapeStats(Stats):
    def __init__(self, board):
        Stats.__init__(self, board)

class ShapeBoard(Board):
    def __init__(self, shapes, statsclass=ShapeStats):
        self.shapes = shapes
##      self.shapesdict = dict([ (num, [ (r, c) 
##                                       for r in range(9)
##                                       for c in range(9)
##                                       if self.shapes[r][c] == num ])
##                               for num
##                               in range(9) ])
##      for k, v in self.shapesdict.items():
##          print k, v        
        
        Board.__init__(self, statsclass)

    def box(self, num):
        return [ self.board[r][c]
                 for r in range(9)
                 for c in range(9)
                 if self.shapes[r][c] == num ]
##      return [ self.board[r][c]
##               for (r, c)
##               in self.shapesdict[num] ]

    def boxNumber(self, row, col):
        return self.shapes[row][col]

    def clone(self):
        result = type(self)(self.shapes, statsclass=None)
        result.copy(self)
        return result
        
