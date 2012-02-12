#!/usr/bin/python

import board

findall = False
	
class Solver(object):
	__slots__ = ['board', 'coord', 'max', 'ui', 'walk']
	
	def __init__(self, ui):
		self.board = board.Board(ui)
		self.ui = ui
		#self.walk = self.walk_r
		self.walk = self.walk_nr
	
	def walk_r(self, coord, n=1):
		board, links = self.board, self.board.links

		p0 = min([ len(d) for d in links.values() ]) == 0
		p1 = False
		futile = p0 or p1

		dest = links[coord]
		board[coord] = n

		if not links:
			if findall:
				self.ui.clone()
			else:
				return True

		elif not futile:
			while dest:
				if self.walk_r(dest.pop(0), n+1):
					return True

		board[coord] = 0
		return False

	def walk_nr(self, coord, n=1):
		board, links = self.board, self.board.links
		stack = [ (coord, n, links[coord]) ]
		while stack:
			coord, n, dest = stack[-1]
			if not board[coord]:
				futile = min([ len(d) for d in links.values() ]) == 0
				board[coord] = n
				if not links:
					if findall:
						self.ui.clone()
						board[coord] = 0
						stack.pop()
						continue
					else:
						return True
				if futile:
					board[coord] = 0
					stack.pop()
					continue
			if dest:
				destcoord = dest.pop(0)
				stack.append((destcoord, n+1, links[destcoord])) 
				continue
			else:
				board[coord] = 0
				stack.pop()
				continue
			
		
	def solve(self, row, col):
		import time
		t1, result, t2 = (time.time(), self.walk((row, col)), time.time())
		d = t2 - t1
		print self.board
		print '%s in %dm %.3fs' % (
			result and 'solved' or 'not solved',
			d / 60,
			d % 60
		)
		return result
