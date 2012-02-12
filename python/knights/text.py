class KnightText(object):
	__slots__ = ['coords', 'm']
	
	def __init__(self):
		self.coords = []
		self.m = 0
	
	def put(self, coord, n):
		if n > self.m: self.m = n
		self.coords.append(coord)
		row, col = coord[0] + 1, coord[1] + 1
		pos = lambda row, col: '\x1b[%d;%dH' % (row, col)
		print '%s%2d' % (pos(8 - row + 1, col * 3), n)
		print '%s%d (%d)' % (pos(8, 30), n, self.m)

	def remove(self):
		if len(self.coords) >= 1:
			pos = lambda row, col: '\x1b[%d;%dH' % (row, col)
			coord = self.coords[-1]
			row, col = coord[0] + 1, coord[1] + 1
			print '%s  ' % (pos(8 - row + 1, col * 3))
			self.coords.pop()

create = KnightText
