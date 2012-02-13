#!/usr/bin/python

import types

size = 8

class Board(object):
	__slots__ = ['state','links','rings','weights','ui','dest']

	def __init__(self, ui=None):
		#global size
		
		self.ui = ui

		# move generation algorithm
		#self.dest = self.dest_plain
		#self.dest = self.dest_pref
		#self.dest = self.dest_weight
		self.dest = self.dest_weight_pref
		
		# initialize board
#		self.initweights()
		self.initboard()
		self.initlinks()
#		self.initrings()
		
	def __getitem__(self, coord):
		#self.checktype(coord)
		row, col = coord
		return self.state[row][col]

	def __setitem__(self, coord, value):
		#self.checktype(coord)
		row, col = coord
		self.state[row][col] = value
		if value == 0:
#			self.rings[min(min(row, size-row-1), min(col, size-col-1))] -= 1
			self.links[coord] = self.dest(coord)
			for link in self.links[coord]:
				self.links[link].append(coord)
			if self.ui:
				self.ui.remove()
		else:
#			self.rings[min(min(row, size-row-1), min(col, size-col-1))] += 1
			for link in self.links[coord]:
				self.links[link].remove(coord)
			del self.links[coord]
			if self.ui:
				self.ui.put(coord, value)

	def __repr__(self):
		fmt = "%%%dd" % len(str(size ** 2))
		return "\t" + "\n\t".join([
			" ".join([ fmt % square for square in row ])
			for row in self.state[::-1]
		])

	def initboard(self):
		self.state = [ 
			[ 0 for _ in range(size) ]
			for _ in range(size)
		]
	
	def initlinks(self):
		self.links = dict([
			((row, col), self.dest((row, col)))
			for row in range(size)
			for col in range(size)
		])
	
	def initrings(self):
		self.rings = {}
		for y in range(size):
			for x in range(size):
				ring = min(min(y, size-y-1), min(x, size-x-1))
				if self.rings.has_key(ring):
					self.rings[ring] += 1
				else:
					self.rings[ring] = 1
	
	def initweights(self):
		self.weights = dict([
			((row, col), min(row, size-row-1, col, size-col-1))
			for row in range(size)
			for col in range(size)
		])
	
	def dest_plain(self, coord):
		row, col = coord
		return [
			coord for coord in [
				(row + dy * sy, col + dx * sx)
				for dx in [ -1, +1 ]
				for dy in [ -1, +1 ]
				for (sy, sx) in [ (2, 1), (1, 2) ]
			]
			if (0 <= coord[0] < size)
			and (0 <= coord[1] < size)
			and not self[coord]
		]
		
	def dest_pref(self, coord):
		row, col = coord
		py = row < (size / 2) and -1 or +1
		px = col < (size / 2) and -1 or +1
		return [
			coord for coord in [
				(row + py * dy * sy, col + px * dx * sx)
				for dy in [ +1, -1 ]
				for dx in [ +1, -1 ]
				for (sy, sx) in [ (1, 2), (2, 1) ]
			]
			if (0 <= coord[0] < size) 
			and (0 <= coord[1] < size)
			and not self[coord]
		]

	def dest_weight(self, coord):
		row, col = coord
		return sorted([
			coord for coord in [
				(row + dy * sy, col + dx * sx)
				for dy in [ +1, -1 ]
				for dx in [ +1, -1 ]
				for sy, sx in [ (1, 2), (2, 1) ]
			]
			if (0 <= coord[0] < size)
			and (0 <= coord[1] < size)
			and not self[coord]
		], cmp=lambda a, b: cmp(
			min(a[0], size-a[0]-1, a[1], size-a[1]-1),
			min(b[0], size-b[0]-1, b[1], size-b[1]-1)
		))

	def dest_weight_pref(self, coord):
		row, col = coord
		py = row < (size / 2) and -1 or +1
		px = col < (size / 2) and -1 or +1
		return sorted([
			coord for coord in [
				(row + py * dy * sy, col + px * dx * sx)
				for dy in [ +1, -1 ]
				for dx in [ +1, -1 ]
				for sy, sx in [ (1, 2), (2, 1) ]
			]
			if (0 <= coord[0] < size)
			and (0 <= coord[1] < size)
			and not self[coord]
		#], cmp=lambda a, b, w=self.weights: cmp(w[a], w[b]))
		], cmp=lambda a, b: cmp(
			min(a[0], size-a[0]-1, a[1], size-a[1]-1),
			min(b[0], size-b[0]-1, b[1], size-b[1]-1)
		))

	def checktype(self, item):
		if isinstance(item, types.TupleType) and len(item) == 2:
			if 0 <= item[0] < size:
				pass
			else:
				raise "Illegal row coordinate: %s" % str(item)
			if 0 <= item[1] < size:
				pass
			else:
				raise "Illegal column coordinate: %s" % str(item)
		else:
			raise "Illegal argument: %s" % item
