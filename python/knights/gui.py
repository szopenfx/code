#!/usr/bin/python

import Tkinter as tk
import Tkconstants as tkc
import threading
import time
import board

size = 32
delay = None

class KnightGui(object):
	__slots__ = [ 'coords', 'tags', 'squares', 'vcolor', 'acolor', 'ncolor' ] \
		+ [ 'tk', 'canvas' ]

	def __init__(self, start=True):
		self.coords = []
		self.tags = []
		self.squares = {}

		self.vcolor = '#00ff00'
		self.ncolor = '#ff0000'
		self.acolor = '#ffff00'
	
		self.initgui()
		
		if start:
			threading.Thread(target=self.tk.mainloop).start()

	def initgui(self):		
		self.tk = tk.Tk()
		self.canvas = tk.Canvas(self.tk, width=size * board.size, height=size * board.size)
		self.canvas.pack()
		
		#self.initlines()
		self.initsquares()
		
	def initlines(self):
		for i in range(board.size):
			self.canvas.create_line(0, i * size, board.size * size, i * size)
			self.canvas.create_line(i * size, 0, i * size, board.size * size)

	def initsquares(self):
		for x in range(board.size):
			for y in range(board.size):
				self.squares[board.size-y-1,x] = self.canvas.create_rectangle(
					x * size,
					y * size,
					x * size + size,
					y * size + size,
					fill=self.ncolor,
					outline=''
				)
		self.squares = dict([
			((board.size - y - 1, x), self.canvas.create_rectangle(
				x * size, y * size, (x + 1) * size, (y + 1) * size,
				fill=self.ncolor, outline=''					
			))
			for x in range(board.size)
			for y in range(board.size)
		])
			
	def setcolor(self, coord, color):
		self.canvas.itemconfigure(self.squares[coord], fill=color)

	def put(self, coord, _=None):
		# show calculations
		if delay:
			time.sleep(delay)

		if len(self.coords) >= 1:
			# calculate destination coordinate
			ty, tx = coord
			ty = ty * size + size / 2
			tx = tx * size + size / 2
			
			# calculate coordinate of origin
			fy, fx = self.coords[-1]
			fy = fy * size + size / 2
			fx = fx * size + size / 2

			# draw line and save reference			
			self.tags.append(
				self.canvas.create_line(
					fx, size * board.size - fy,
					tx, size * board.size - ty
				)
			)
		
		# save coordinate, set background color of last square to active
		self.coords.append(coord)
		self.setcolor(coord, self.acolor)
		
		# set background color of penultimate square to visited
		if len(self.coords) >= 2:
			self.setcolor(self.coords[-2], self.vcolor)
	
	def remove(self):
		# show calculations
		if delay:
			time.sleep(delay)
		
		# delete line
		if len(self.tags) >= 1:
			self.canvas.delete(self.tags.pop())

		# set background color to unvisited, remove coordinate
		if len(self.coords) >= 1:
			self.setcolor(self.coords.pop(), self.ncolor)
				
		# set background color to active
		if len(self.coords) >= 1:
			self.setcolor(self.coords[-1], self.acolor)

	def clone(self):
		ui = KnightGui(start=False)
		ui.delay = 0
		for coord in self.coords:
			ui.put(coord)

create = KnightGui

if __name__ == '__main__':
	ui = create()
