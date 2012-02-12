"""
Classes for representing a Life grid. Currently two algorithms are implemented,
a 'correct' one (L{SimpleLifeGrid}) and a faster one (L{UnrolledLifeGrid}).

@version: 1.0
@author: Joost Molenaar
@contact: j.j.molenaar at gmail dot com
@license: Copyright(c)2005 Joost Molenaar
"""

class LifeGrid:
    """Base class for representing a life grid.

    @version: 1.0
    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @license: Copyright(c)2005 Joost Molenaar
    """
    def __init__(self, size):
        """Initialize LifeGrid instance.
        @param size: Number of cells in each side of the life grid.
        @type size: int
        """
        if size < 3:
            raise Exception("Size %d? Go away." % (size,))
        self.size = size
        
    def EmptyGrid(self):
        """Create an empty life grid.
        @return: List of list of 0's
        @rtype: list
        """
        return [ [ 0 for w in range(self.size) ] for h in range(self.size) ]
    
    def Put(self, x, y, value):
        """Put a value at a certain x,y coordinate.
        @param x: X coordinate
        @param y: Y coordinate
        @type x: int
        @type y: int
        """
        self.grid[y][x] = value
    
    def Get(self, x, y):
        """Return the value of a certain x,y coordinate.
        @param x: X coordinate
        @param y: Y coordinate
        @type x: int
        @type y: int
        @return Value of coordinate
        @rtype int
        """
        return self.grid[y][x]
  
class SimpleLifeGrid(LifeGrid):
    """Life grid that is readable and correct but not excessively fast.

    Speed of a 70x70 grid::
    non-localized: 8 fps
    localized: 10 fps

    @version: 1.0
    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @license: Copyright(c)2005 Joost Molenaar
    """
    def __init__(self, size, survival=[2,3], birth=[3]):
        """Initialize SimpleLifeGrid instance.
        @param size: Size of grid
        @param survival: Number of neighbours required for survival.
        @param birth: Number of neighbours required for birth.
        @type size: int
        @type survival: list of int
        @type birth: list of int
        """
        LifeGrid.__init__(self, size)
        self.survival = survival
        self.birth = birth
        self.Reset()

    def Reset(self):
        """Reset SimpleLifeGrid instance to initial state.
        """
        self.grid = self.EmptyGrid()
        self.count = self.EmptyGrid()

    def CountNeighbours(self):
        """Fill C{self.count} grid with number of neighbours of each cell."""
        idx = lambda c: -int(c==0) + int(c==self.size-1)
        deltas = { -1:[0, 1], 0:[-1, 0, 1], 1:[-1, 0] }
        ## localize instance vars local for speed (gains +1 fps)
        size = self.size
        grid = self.grid
        count = self.count
        ## #
        for y in xrange(size):
            for x in xrange(size):
                count[y][x] = 0
                vertical = deltas[idx(y)]
                horizontal = deltas[idx(x)]
                for dy in vertical:
                    for dx in horizontal:
                        if (dy != 0) or (dx != 0):
                            count[y][x] += grid[y+dy][x+dx]

    def Iterate(self):
        """Advance the life grid by one iteration."""
        self.CountNeighbours()
        ## localize instance vars for speed (gains +1 fps)
        size = self.size
        survival = self.survival
        birth = self.birth
        grid = self.grid
        count = self.count
        ## #
        for y in xrange(size):
            for x in xrange(size):
                if grid[y][x] == 1:
                    if count[y][x] not in survival:
                        grid[y][x] = 0
                elif grid[y][x] == 0:
                    if count[y][x] in birth:
                        grid[y][x] = 1

class UnrolledLifeGrid(LifeGrid):
    """Somewhat faster implementation of life grid.

    Speed of 70x70 grid::
    localized: 29 fps
    non-localized: ???

    @version: 1.0
    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @license: Copyright(c)2005 Joost Molenaar
    """
    def __init__(self, size, survival=[2,3], birth=[3]):
        """Initialize UnrolledLifeGrid instance.
        @param size: Size of grid
        @param survival: Number of neighbours required for survival.
        @param birth: Number of neighbours required for birth.
        @type size: int
        @type survival: list of int
        @type birth: list of int
        """
        LifeGrid.__init__(self, size)
        self.survival = frozenset(survival)
        self.birth = frozenset(birth)
        self.Reset()
    
    def Reset(self):
        """Reset LifeGrid to initial state."""
        self.grid = self.EmptyGrid()
        self.grid2 = self.EmptyGrid()
    
    def Iterate(self): # very ugly, i know
        """Advance grids one iteration."""
        old = self.grid
        new = self.grid2
        size = self.size
        survival = self.survival
        birth = self.birth
        m = size - 1
        
        y = 1
        while y < m:
            x = 1
            while x < m:
                count = old[y-1][x-1] + old[y-1][x] + old[y-1][x+1] + old[y][x-1] \
                        + old[y][x+1] + old[y+1][x-1] + old[y+1][x] + old[y+1][x+1] 
                new[y][x] = int((old[y][x] and count in survival) 
                            or (not old[y][x] and count in birth))
                x += 1
            # N row
            count = old[0][y-1] + old[0][y+1] + old[1][y-1] + old[1][y] + old[1][y+1]
            new[0][y] = int((old[0][y] and count in survival) 
                        or (not old[0][y] and count in birth))
            # S row
            count = old[m][y-1] + old[m][y+1] + old[m-1][y-1] + old[m-1][y] + old[m-1][y+1]
            new[m][y] = int((old[m][y] and count in survival) 
                        or (not old[m][y] and count in birth))
            # W column
            count = old[y-1][0] + old[y+1][0] + old[y-1][1] + old[y][1] + old[y+1][1]
            new[y][0] = int((old[y][0] and count in survival) 
                        or (not old[y][0] and count in birth))
            # E column
            count = old[y-1][m] + old[y+1][m] + old[y-1][m-1] + old[y][m-1] + old[y+1][m-1]
            new[y][m] = int((old[y][m] and count in survival) 
                        or (not old[y][m] and count in birth))
            y += 1
    
        # NW corner
        count = old[0][1] + old[1][1] + old[1][0]
        new[0][0] = int((old[0][0] and count in survival) 
                    or (not old[0][0] and count in birth))
        # NE corner
        count = old[0][m-1] + old[1][m-1] + old[1][m]
        new[0][m] = int((old[0][m] and count in survival)
                    or (not old[0][m] and count in birth))
        # SW corner
        count = old[m][1] + old[m-1][1] + old[m-1][0]
        new[m][0] = int((old[m][0] and count in survival) 
                    or (not old[m][0] and count in birth))
        # SE corner
        count = old[m][m-1] + old[m-1][m-1] + old[m-1][m]
        new[m][m] = int((old[m][m] and count in survival) 
                    or (not old[m][m] and count in birth))

        # switch places of grid
        self.grid = new
        self.grid2 = old
