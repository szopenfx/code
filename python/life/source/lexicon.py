"""Classes for reading Life patterns from files.

@version: 1.0
@author: Joost Molenaar
@contact: j.j.molenaar at gmail dot com
@license: Copyright(c)2005 Joost Molenaar
"""

import os

class Lexicon:
    """Class for storing Life patterns divided in classes.

    @version: 1.0
    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @license: Copyright(c)2005 Joost Molenaar
    """
    def __init__(self, directory):
        """Read all .txt files in a directory and store them in
        L{lexicon.Patterns} instances.
        @param directory: Directory to look for .txt files
        @type directory: str
        """
        self.names = []
        self.patterns = []
        self.LoadFiles(directory)

    def LoadFiles(self, directory):
        """Find .txt files in a directory and create a L{lexicon.Patterns}
        object for each file.
        @param directory: Directory name
        @type directory: str
        """        
        for root, dirs, files in os.walk(directory):
            for filename in files:
                if filename.endswith('.txt'):
                    self.names.append(filename.split('.')[0].capitalize())
                    self.patterns.append(Patterns(
                        os.path.join(root, filename)))
                    
    def Put(self, lifegrid, x, y, class_, pattern, rotation):
        """Put a pattern of a certain class at a certain place.
        @param lifegrid: LifeGrid instance which will receive the pattern
        @param x: X coordinate for the pattern
        @param y: Y coordinate for the pattern
        @param class_: Number of the class of the pattern (index into
                       C{self.names} and C{self.patterns} lists
        @param pattern: Number of the pattern
        @param rotation: Rotation (0, 1, 2, 3)
        @type lifegrid: L{lifegrid.LifeGrid}
        @type x: int
        @type y: int
        @type class_: int
        @type pattern: int
        @type rotation: int
        """
        self.patterns[class_].Put(lifegrid, x, y, pattern, rotation)

class Patterns:
    """Class for storing a collection of Life patterns.

    @version: 1.0
    @author: Joost Molenaar
    @contact: j.j.molenaar at gmail dot com
    @license: Copyright(c)2005 Joost Molenaar
    """
    def __init__(self, filename):
        """Open a file and read the patterns from it.
        @param filename: Name of file containing patterns
        @type filename: str
        """
        self.rotations = [ '0', '90', '180', '270' ]
        fh = open(filename)
        self.ReadPatterns(fh.readlines())
        fh.close()
    
    def ReadPatterns(self, lines):
        """Read patterns from a list of lines; store the patterns in
        C{self.names} and C{self.patterns}
        @param lines: List of lines read from a file.
        @type lines: list of str
        """
        trans = { '.':0, 'x':1, '0':0, '1':1, 'O':1 }
        self.names = []
        self.patterns = []
        NAME, PATTERN = 1, 2

        mode = NAME
        for line in lines:
            if line.endswith('\n'): 
                line = line[:-1]
            if mode == NAME:
                mode = PATTERN
                current = []
                self.names.append(line)
                self.patterns.append(current)
            elif mode == PATTERN:
                if line == '':
                    mode = NAME
                else:
                    current.append([ trans[ch] for ch in line ])

    def Rotate(self, choice, pattern):
        """Rotate a pattern by a certain amount.
        @param choice: Index into C{self.choices} list (C{[0, 90, 180, 270]})
        @param pattern: Index into C{self.patterns} list
        @type choice: int
        @type pattern: int
        @return Rotated pattern
        @rtype list of list of int
        """
        deg = int(self.rotations[choice])
        if deg == 0:
            result = pattern
        if deg == 90:
            result = []
            for i in xrange(len(pattern[0])):
                result.append([ row[i] for row in pattern[::-1] ])
        if deg == 180:
            result = [ row[::-1] for row in pattern[::-1] ]
        if deg == 270:
            result = []
            for i in xrange(len(pattern[0])-1, -1, -1):
                result.append([ row[i] for row in pattern ])
        return result

    def Put(self, lifegrid, x, y, pattern, rotation):
        """Put a pattern of a certain class at a certain place.
        @param lifegrid: LifeGrid instance which will receive the pattern
        @param x: X coordinate for the pattern
        @param y: Y coordinate for the pattern
        @param pattern: Number of the pattern
        @param rotation: Rotation (0, 1, 2, 3)
        @type lifegrid: L{lifegrid.LifeGrid}
        @type x: int
        @type y: int
        @type pattern: int
        @type rotation: int
        """        
        patternmask = self.Rotate(rotation, self.patterns[pattern])
        for r, row in enumerate(patternmask):
            for c, cell in enumerate(row):
                if r + y < lifegrid.size and c + x < lifegrid.size:
                    current = lifegrid.Get(c + x, r + y)
                    if cell == 1:
                        lifegrid.Put(c + x, r + y, 1 - current)
