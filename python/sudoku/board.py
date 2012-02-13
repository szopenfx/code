__author__ = 'Joost Molenaar'

import random
import stats

class Board(object):
    'Represents a board configuration and provides methods for manipulation.'
    
    def __init__(self, statsclass=stats.Stats):
        self.updatefunc = None
        if statsclass:
            self.stats = statsclass(self)        
        self.empty()
        self.results = []
        self.stop = False
        self.last = None

    def __str__(self):
        'Convert Board object to string object.'
        def itemToStr(item):
            if item is None:
                return ' '
            else:
                return item
        result = ''
        sep_str = '------+-------+------\n'
        row_fmt = '%s %s %s | %s %s %s | %s %s %s\n'
        separators = [ (0, 3, sep_str), (3, 6, sep_str), (6, 9, '') ]
        for (s, e, sep) in separators:
            for row in self.board[s:e]:
                result += row_fmt % tuple(map(itemToStr, row))
            result += sep
        return result

    def empty(self):
        'Generate an empty board.'
        self.board = [ [None] * 9 for _ in range(9) ]
        if hasattr(self, 'stats'):
            self.stats.analyze()

    def random(self, givens = random.randint(0, 81)):
        'Generate a board with random values.'
        self.empty()
        for n in range(givens):
            empty = self.stats.calcEmpty()
            row, col = empty[random.randint(0, len(empty) - 1)]
            possible = list(self.stats.possible(row, col))
            if len(possible) > 0:
                possible_idx = random.randint(0, len(possible) - 1)
                self.put(row, col, possible[possible_idx])
                self.stats.put(row, col, possible[possible_idx])

    def test(self):
        'Generate a board with nonsensical, unique values.'
        self.board = [ [ y * 10 + x for x in range(9) ] for y in range(9) ]

    def user(self):
        'Generate a board from standard input.'
        def zeroToNone(item):
            if item == 0:
                return None
            else:
                return item
        self.board = []
        prompt_string = 'Row %d = '
        print 'Enter list of values with 0s for empty cells.'
        for row_num in range(1, 10):
            in_str = raw_input(prompt_string % (row_num, ))
            values = [ int(v) for v in list(in_str) ]
            self.board.append(map(zeroToNone, values))
        self.stats.analyze()

    def copy(self, board):
        'Set board to value of another board.'
        self.board = [ row[:] for row in board.board ]

    def clone(self):
        'Return a clone of this board.'
        result = type(self)(statsclass=None)
        result.copy(self)
        return result

    def row(self, num):
        'Return a list of a row of the board.'
        return self.board[num]

    def col(self, num):
        'Return a list of a column of the board.'
        return [ row[num] for row in self.board ]

    def box(self, num):
        'Return a list of a box in the board.'
        rs, cs = num / 3 * 3, num % 3 * 3
        re, ce = rs + 3, cs + 3
        return [ cell
             for row in self.board[rs:re]
             for cell in row[cs:ce] ]

    def boxNumber(self, row, col):
        'Calculate the box number of a certain (row, col) coordinate.'
        return (row / 3 * 3) + (col / 3)

    def put(self, row, col, value):
        'Enter a value into the board.'
        self.board[row][col] = value

    def pe(self):
        e = self.stats.calcEmpty()
        print '\n'.join(map(str,[ [ i if (r,c) in e else 0
                                    for c, i in enumerate(row) ]
                                  for r, row in enumerate(self.stats.calcCount())
                                 ]))

##    def get(self, row, col):
##        'Retrieve a value from the board.'
##        board_row = self.board[row]
##        board_row[col] = value
##        boo = bah
##        #this should crash crashity crash crash 
##

    def solve(self, all=False):
        'Solve a sudoku board.'
        if self.updatefunc:
            self.updatefunc()

        if self.stop:
            return False

        count_coord = self.stats.calcCountCoord()

        if len(count_coord) == 0:
            if all:
                self.results.append(self.clone())
                self.resultfound()
                return False
            else:
                return True

        for (count, row, col) in count_coord:
            if count == 0:
                return False
            possible = list(self.stats.possible(row, col))
            possible.sort()
            for value in possible:
                self.put(row, col, value)
                self.stats.put(row, col, value)
                if self.solve(all):
                    return True
                else:
                    self.put(row, col, None)
                    self.stats.remove(row, col, value)
            return False # weird?
        return False
        
    def terminate(self):
        self.stop = True

    def do(self, r, c, v):
        self.put(r, c, v)
        self.stats.put(r, c, v)
        self.stats.analyze()


        print '### step:', v, (r, c)
        print self
        print 'c:', ' '.join(map(lambda t: '('+' '.join(map(str,t))+')',
                                 self.stats.calcCountCoord()))
        print 'p:', self.stats.possible(*self.stats.calcCountCoord()[0][1:])

    def undo(self, r, c, v):
        if hasattr(self, 'last'):
            self.remove(*x)
            self.stats.remove(*x)
        else:
            raise Exception('please call step() first')

if __name__ == '__main__':
    b = Board()
