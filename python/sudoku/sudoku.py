#!/usr/bin/python
"""
Sudoko.py (c) 2005-2008 Joost Molenaar.
Generates and solves 9 x 9 sudoko puzzles.

Modification history:
     5/9/2005 19:00-01:00 Created most data structures and manipulations
     6/9/2005 11:45-14:00 Created analysis functions
     7/9/2005 23:30-00:15 Speeded up code by performing more analysis
     8/9/2005 10:00-14:00 Transformed to Object Oriented code
     9/9/2005 16:00-18:00 Created GUI class
    10/9/2005 23:00-00:00 Made GUI multithreaded
    18/4/2007             Option for finding all solutions
                          GUI now judges difficulty
    23/2/2008             Added loading facility for scripting/command line
                          GUI now severely inadequate! (Always has been)
    24/2/2008             Added diagonal sudoku, shape sudoku, mixed sudoku
                          (nearly no modifications to board and stats code)
                          (particularly, none to the solver) 
"""

__author__ = 'Joost Molenaar'

import Tkinter as tk
from threading import Thread
from board import Board
from time import time

class GUI(object):
    'Interface for solving Sudoku puzzles.'
    
    def __init__(self):
        'Initialize GUI class.'
        self.board = Board()
        self.restore = None
        self.current = None
        self.createGUI()
        self.setEnabled(True)
        try:
            self.root.mainloop()
        except KeyboardInterrupt:
            self.board.terminate()

    def createGUI(self):
        'Create a TK interface.'
        self.root = tk.Tk()
        self.root.wm_title('Sudoku')
        
        self.frame = tk.Frame(self.root)
        self.frame.pack()

        self.frLeft = tk.LabelFrame(self.frame)
        self.frLeft.grid(row=0, column=0)

        self.frRight = tk.LabelFrame(self.frame)
        self.frRight.grid(row=0, column=1, sticky=tk.N)
                
        self.entries = []
        for r in range(9):
            ex = []
            for c in range(9):
                e = tk.Entry(self.frLeft, width=2)
                e.grid(row=r, column=c)
                ex.append(e)
            self.entries.append(ex)

        self.time = tk.StringVar()
        self.update = tk.BooleanVar()
        self.findall = tk.BooleanVar()
        
        self.lblTime = tk.Label(self.frame, textvariable=self.time)
        self.lblTime.grid(row=1, columnspan=2)
        self.time.set('suji wa dokushin ni kagiru')
        
        self.edtGivens = tk.Entry(self.frRight, width=2)
        self.edtGivens.insert(tk.END, '23')
        self.edtGivens.grid(row=1, column=0, sticky=tk.E)
        
        self.btnRandom = tk.Button(self.frRight, text="Random", command=self.onRandom)
        self.btnRandom.grid(row=1, column=1, sticky=tk.EW)

        self.btnEmpty = tk.Button(self.frRight, text="Empty", command=self.onEmpty)
        self.btnEmpty.grid(row=2, column=0, sticky=tk.EW)

        self.btnRestore = tk.Button(self.frRight, text="Restore", command=self.onRestore)
        self.btnRestore.grid(row=2, column=1, sticky=tk.EW)

        self.btnNext = tk.Button(self.frRight, text="Next", command=self.onNext)
        self.btnNext.grid(row=3, column=0, sticky=tk.EW)

        self.btnPrevious = tk.Button(self.frRight, text="Previous", command=self.onPrevious)
        self.btnPrevious.grid(row=3, column=1, sticky=tk.EW)
        
        self.chkFindAll = tk.Checkbutton(self.frRight, text="Find all", command=self.onFindAll, variable=self.findall)
        self.chkFindAll.grid(row=6, column=1, sticky=tk.EW)
        
        self.chkUpdate = tk.Checkbutton(self.frRight, text="Update", command=self.onUpdate, variable=self.update)
        self.chkUpdate.grid(row=7, column=1, sticky=tk.EW)

        self.btnStop = tk.Button(self.frRight, text="Stop", command=self.onStop)
        self.btnStop.grid(row=7, column=0, sticky=tk.EW)

        self.btnSolve = tk.Button(self.frRight, text="Solve", command=self.onSolve)
        self.btnSolve.grid(row=8, column=0, sticky=tk.EW, columnspan=2)
        
    def onUpdate(self):
        self.board.updatefunc = self.update.get() \
            and self.scatterBoard \
            or None

    def onRestore(self):
        self.board.copy(self.restore)
        self.scatterBoard()
    
    def onNext(self):
        if self.current == None and len(self.board.results) > 0:
            self.current = 0
        elif (self.current + 1) < len(self.board.results):
            self.current += 1
        else:
            return
        self.board.copy(self.board.results[self.current])
        self.scatterBoard()
    
    def onPrevious(self):
        if self.current == None and len(self.board.results) > 0:
            self.current = len(self.board.results) - 1
        elif self.current > 0:
            self.current -= 1
        else:
            return
        self.board.copy(self.board.results[self.current])
        self.scatterBoard()
    
    def onFindAll(self):
        pass
    
    def onStop(self):
        self.board.terminate()
        
    def onSolve(self):
        'Solve the puzzle when btnSolve is clicked.'
        
        def threadStart():
            timestr = { True:'', False:'not ' }
            start, result, end = time(), self.board.solve(self.findall.get()), time()
            length = end - start
            opinion = (length < 1) and 'trivial' \
                or (length < 5) and 'easy' \
                or (length < 30) and 'interesting' \
                or (length < 60) and 'nasty' \
                or (length < 120) and 'bloody awful' \
                or 'you heartless bastard'
            self.time.set('%ssolved in %.3fs. %s.' % (timestr[result], length, opinion))
            self.root.after_idle(threadEnd)
            
        def threadEnd():
            self.scatterBoard()
            self.setEnabled(True)

        self.board.stop = False
        self.board.results = []
        self.board.resultfound = self.tellResults

        self.time.set('solving')
        self.restore = self.board.clone()
        self.setEnabled(False)
        self.gatherBoard()
        
        Thread(target=threadStart).start()

    def onRandom(self):
        'Generate a random board when btnRandom is clicked.'
        self.board.random(int(self.edtGivens.get()))
        self.scatterBoard()

    def onEmpty(self):
        'Empty board when btnEmpty is clicked.'
        self.board.empty()
        self.scatterBoard()

    def scatterBoard(self):
        'Put values of Board object into Entry widgets.'
        for (entryRow, boardRow) in zip(self.entries, self.board.board):
            for (entryCell, boardCell) in zip(entryRow, boardRow):
                entryCell.delete(0, tk.END)
                if boardCell != None:
                    entryCell.insert(tk.END, str(boardCell))

    def gatherBoard(self):
        'Put values of Entry widgets into Board object.'
        self.board.empty()
        for (r, row) in enumerate(self.entries):
            for(c, cell) in enumerate(row):
                value = cell.get()
                if value != '':
                    self.board.put(r, c, int(value))
                    self.board.analysis.put(r, c, int(value))

    def setEnabled(self, enabled):
        'Enable or disable the window.'
        state = { True:tk.NORMAL, False:tk.DISABLED }
        for widget in [self.btnSolve, self.edtGivens, self.btnRandom,
            self.btnEmpty, self.btnRestore, self.btnNext,
            self.btnPrevious, self.chkFindAll ]:
                widget['state'] = state[enabled]
        self.btnStop['state'] = state[not enabled]

    def tellResults(self):
        self.time.set('solving; %d results so far' % len(self.board.results))

if __name__ == '__main__':
    gui = GUI()
        