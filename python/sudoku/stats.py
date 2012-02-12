__author__ = 'Joost Molenaar'

class Stats(object):
    'Analyzes and stores facts about a Board object.'

    values = frozenset([1, 2, 3, 4, 5, 6, 7, 8, 9])
    
    def __init__(self, board):
        'Assign and analyze a board.'
        self.board = board
        if hasattr(self.board, 'board'):
            self.analyze()

    def __str__(self):
        'Convert Stats object to string.'
        def listrepr(L):
            result = ''
            for i in L:
                result += str(i)
                result += '\n'
            return result
        result = ''
        for (T, L) in [
            ('row', self.row),
            ('col', self.col),
            ('box', self.box),
            ('empty', self.calcEmpty()),
            ('count', self.calcCount()),
            ('countCoord', self.calcCountCoord()) ]:
            result += '[%s]\n' % (T, )
            result += '\n'.join(map(str, L))
        return result
            
    def analyze(self):
        'Run all analysis functions.'
        self.row = [ self.values - set(self.board.row(n)) for n in range(9) ]
        self.col = [ self.values - set(self.board.col(n)) for n in range(9) ]
        self.box = [ self.values - set(self.board.box(n)) for n in range(9) ]

    def calcEmpty(self):
        'Find coordinates of empty cells.'
        return [ (r, c)
             for (r, row) in enumerate(self.board.board)
             for (c, cell) in enumerate(row)
             if cell is None ]

    def calcCount(self):
        'Return board of counts of possibillities.'
        return [ [ len(self.possible(row, col)) for col in range(9) ]
                 for row in range(9) ]

    def calcCountCoord(self):
        'Return list of (possibilityCount, row, col) tuples.'
        empty = self.calcEmpty()
        result = [ (count, r, c)
                   for (r, row) in enumerate(self.calcCount())
                   for (c, count) in enumerate(row)
                   if (r, c) in empty ]
        result.sort()
        return result

    def possible(self, row, col):
        'Return set of possible values for given coordinate.'
        box = self.board.boxNumber(row, col)
        return self.row[row] \
             & self.col[col] \
             & self.box[box]

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
