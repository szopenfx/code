#!/usr/bin/python

import sys, threading, getopt, time, Tkinter
import board
import solver
import gui as interface

def ex(e, usage=False):
	if e:
		print '%s: %s' % (e.__class__.__name__, e[0])
	if not usage:
		return
	print 'usage: %s [-g] [-t] [-d n] [-a] [-s n] coord' % (sys.argv[0])
	print
	print '	coord	like a3 (for boards up to size 8)'
	print '		or like 2 0 (row, column, for bigger boards, zero-based)'
	print '	-g	graphical user interface'
	print '	-t	textual interface (needs ANSI escape sequence support)'
	print '	-a	find all solutions, not just the first'
	print '	-s n	size in pixels of one square for use with -g'
	print '	-d n	delay in seconds between updates (default: 0.0)'
	print '	-b n	board size'
	print

def main(*argv):
	global interface
	debug = False
	b, s, ui = None, None, None
	try:
		options = 'Dngtd:as:b:'
		opts, args = getopt.getopt(argv, options)

		for opt, val in opts:
			if opt == '-D':
				debug = True
			elif opt == '-n':
				interface = None
			elif opt == '-g':
				import gui as interface
			elif opt == '-t':
				import text as interface
			elif opt == '-d':
				interface.delay = float(val)
			elif opt == '-a':
				solver.findall = True
			elif opt == '-s':
				interface.size = int(val)
			elif opt == '-b':
				board.size = int(val)
	
		if len(args) == 0:
			raise ValueError("No starting square specified")
			
		elif len(args) == 1:
			try:
				col = 'abcdefgh'.index(args[0][0])
				row = '12345678'.index(args[0][1])
			except ValueError:
				raise ValueError('Not a chess coordinate')
				
		elif len(args) == 2:
			row, col = map(int, args)
		
		# start the show
		ui = interface and interface.create()
		s = solver.Solver(ui)
		b = s.board

		if debug:
			solve = lambda: s.solve(row, col)
			threading.Thread(None, solve, 'SolverThread').start()
		else:
			s.solve(row, col)

	except ValueError, e:
		ex(e, usage=True)

	except getopt.GetoptError, e:
		ex(e, usage=True)

	except KeyboardInterrupt, e:
		pass

	except Tkinter.TclError, e:
		ex(e)

	#except Exception, e:
	#	print '%s: %s' % (e.__class__.__name__, e)

	return b, s, ui


if __name__ == '__main__':
	b, s, ui = main(*sys.argv[1:])
