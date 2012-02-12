__author__ = 'Joost Molenaar'

class Analysis:
    'Analyzes and stores facts about a Board object.'

    values = set([1, 2, 3, 4, 5, 6, 7, 8, 9])
    
    def __init__(self, board):
        'Assign and analyze a board.'
        self.board = board
        if hasattr(self.board, 'board'):
            self.analyze()

    def __str__(self):
        'Convert Analysis object to string.'
        result = ''
        for title, item in [('row',     self.row),
                            ('col',     self.col),
                            ('box',     self.box),
                            ('count',   self.calcCount())]:
                result += '__%s__' % title
                result += '\n'
                for n, row in enumerate(item):
                        result += '%d = %s' % (n,
                                               ' '.join(map(str, row)))
                        result += '\n'
                result += '\n'
        return result
            
    def analyze(self):
        'Run all analysis functions.'
        self.row, self.col, self.box = self.calcSets()

    def calcEmpty(self):
        'Find coordinates of empty cells.'
        return [ (r, c)
             for (r, row) in enumerate(self.board.board)
             for (c, cell) in enumerate(row)
             if cell is None ]

    def calcSets(self):
        'Generate sets for all rows, columns and boxes.'
        return ( [ self.values - set(self.board.row(n)) for n in range(9) ],
             [ self.values - set(self.board.col(n)) for n in range(9) ],
             [ self.values - set(self.board.box(n)) for n in range(9) ] )

    def calcCount(self):
        'Return board of counts of possibillities.'
        return [ [ len(self.possible(row, col))
               for col in range(9) ]
               for row in range(9) ]

    def calcCountCoord(self):
        'Return list of (possibilityCount, row, col) tuples.'
        empty = self.calcEmpty()
        count = self.calcCount()
        result = [ (p, r, c)
               for (r, row) in enumerate(count)
               for (c, p) in enumerate(row)
               if (r, c) in empty ]
        result.sort()
        return result

    def possible(self, row, col):
        'Return set of possible values for given coordinate.'
        box = self.board.boxNumber(row, col)
        return self.row[row] & self.col[col] & self.box[box]

    def put(self, row, col, value):
        'Put a value on the board, so remove it from the sets.'
        box = self.board.boxNumber(row, col)
        self.row[row] -= set([value])
        self.col[col] -= set([value])
        self.box[box] -= set([value])

    def remove(self, row, col, value):
        'Remove a value from the board, so add it to the sets.'
        box = self.board.boxNumber(row, col)
        self.row[row] |= set([value])
        self.col[col] |= set([value])
        self.box[box] |= set([value])

if __name__ == '__main__':
        from board import Board
        b=Board()
        a=Analysis(b)
        print a
        
