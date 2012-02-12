__author__ = 'Joost Molenaar'

import random
from analysis import Analysis

class Board:
	'Represents a board configuration and provides methods for manipulation.'
	
	def __init__(self, createanalysis=True):
		self.updatefunc = None
		if createanalysis:
			self.analysis = createanalysis and Analysis(self) or None
		self.empty()
		self.results = []
		self.stop = False

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
		self.board = [ [ None for x in range(9) ] for y in range(9) ]
		if hasattr(self, 'analysis'):
			self.analysis.analyze()

	def random(self, givens = random.randint(0, 81)):
		'Generate a board with random values.'
		self.empty()
		for n in range(givens):
			empty = self.analysis.calcEmpty()
			row, col = empty[random.randint(0, len(empty) - 1)]
			possible = list(self.analysis.possible(row, col))
			if len(possible) > 0:
				possible_idx = random.randint(0, len(possible) - 1)
				self.put(row, col, possible[possible_idx])
				self.analysis.put(row, col, possible[possible_idx])

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
		self.analysis.analyze()

	def copy(self, board):
		'Set board to value of another board.'
		self.board = [ row[:] for row in board.board ]

	def clone(self):
		'Return a clone of this board.'
		result = Board(False)
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
		'Enter a particular value into the board.'
		board_row = self.board[row]
		board_row[col] = value

	def get(self, row, col):
		'Retrieve a value from the board.'
		board_row = self.board[row]
		board_row[col] = value

	def solve(self, all=False):
		'Solve a sudoku board.'
		if self.updatefunc:
			self.updatefunc()
		if self.stop:
			return False
		count_coord = self.analysis.calcCountCoord()
		if len(count_coord) == 0:
			if all:
				self.results.append(self.clone())
				self.resultfound()
				# print 'result #%d' % (len(self.results)-1)
				# print self
				return False
			else:
				return True
		for (count, row, col) in count_coord:
			possible = list(self.analysis.possible(row, col))
			possible.sort()
			# print '%d%d %s' % (row, col, possible)
			if count == 0:
				return False
			for value in possible:
				# print '%d%d +%d' % (row, col, value)
				self.put(row, col, value)
				self.analysis.put(row, col, value)
				if self.solve(all):
					return True
				else:
					# print '%d%d -%d' % (row, col, value)
					self.put(row, col, None)
					self.analysis.remove(row, col, value)
			return False
		return False
		
	def terminate(self):
		self.stop = True

if __name__ == '__main__':
	b = Board()
