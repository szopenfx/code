from __future__ import with_statement

from stats import Stats
from board import Board
from diagonal import DiagStats, DiagBoard
from shape import ShapeStats, ShapeBoard
from extra import ExtraStats, ExtraBoard

try:
    from sudoku import GUI
except:
    pass

def load(filename, boardclass=Board, shapes=None):
    b = shapes and boardclass(shapes) or boardclass()
    f = open(filename, 'r')

    for line in f.readlines():
        line = line.strip().split(' ')

        box = int(line[0])
        sq = [ map(int, i) for i in line[1:] ]

        for s, v in sq:
            row = int((box-1)/3)*3+int((s-1)/3)
            col = int((box-1)%3)*3+int((s-1)%3)
            b.put(row, col, v)

    f.close()

    def resultfound():
        print b

    b.resultfound = resultfound
    b.stats.analyze()
    return b

def loaddiag(filename):
    return load(filename, boardclass=DiagBoard)

def loadextra(filename):
    return load(filename, boardclass=ExtraBoard)

def loadshape(filename):
    def shapes():
        with open(filename + '-shape') as f:
            return [ map(lambda ch: int(ch) - 1, line.strip())
                     for line in f.readlines()
                     if line.strip() ]
    return load(filename, boardclass=ShapeBoard, shapes=shapes())

def permute(*lists):
    if len(lists) >= 2:
        return ( [head] + tail
                 for head in lists[0]
                 for tail in permute(*lists[1:]) )
    
    if len(lists) == 1:
        return ( [head] for head in lists[0] )

    assert len(lists) > 0

def solvemix(boards, overlaps):
    result = []

    for b in boards:
        print 'solving', type(b).__name__, '...',
        b.resultfound = lambda: None
        b.solve(all=True)
        print len(b.results), 'results'

    for boards in permute(*map(lambda b: b.results, boards)):
        for board1, box1, board2, box2 in overlaps:
            b1 = boards[board1].box(box1 - 1)
            b2 = boards[board2].box(box2 - 1)
            if b1 != b2:
                break
        else:
            result.append(boards)

    return result

#if __name__ == '__main__':

def main():
    import os
    #os.chdir('i:/.storage/sdhc-2g/python')
    os.chdir('..')
    
    try:
        if 0:
            if 0: b = load('.kutsudoku')
            if 0: b = load('.vkdiag')
            if 0: b = loaddiag('.vkdiag')
            if 0: b = loadshape('.vkvorm')
            if 0: b = load('.worst-case')
            if 1: b = loadextra('.nrc20080228')
            print b
            b.solve(1)
            #print b
            print len(b.results), 'results'

        if 1:
            for result in solvemix([loadshape('./.vkvorm'),
                                    loaddiag('./.vkdiag')],
                                   [(0, 8, 1, 1)]):
                print '-------------'
                for board in result:
                    print '###', type(board).__name__
                    print board
    except KeyboardInterrupt, e:
        pass
    return b
if __name__ == '__main__':
    b = main()
