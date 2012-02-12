#!/usr/bin/python

class KnightMoves(object):
	def __init__(self):
		self.sep = ' '
		self.moves = []

	def __getitem__(self, item):
		return self.moves[item]
	
	def __setitem__(self, item, value):
		self.moves[item] = value
		return self
	
	def __len__(self):
		return len(self.moves)
	
	def __str__(self):
		return self.sep.join([
			'abcdefgh'[col] + '12345678'[row]
			for (row, col) in self.moves
		])
	
	def append(self, item):
		return self.moves.append(item)
	
	def pop(self):
		return self.moves.pop()