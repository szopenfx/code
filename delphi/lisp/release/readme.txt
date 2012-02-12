**********************
* NUMP specification *
**********************


AUTHORS

	Joost Molenaar
	Michel de Jong
	hacked together in 5 days in november 2005



CHANGELOG
	1.0 (November 2005)		Initial release



ABOUT THE LANGUAGE

	NUMP = NUMber Processor (- 'lisp 'list)
	Loosely based on LISP



SUPPORTS

- 16 bit arithmetic
- 10 parameters to functions
- up to 10 nested LOOPs
- pure functional programming
- produced binaries (.COM files) work on any 80286 processor or newer,
  the compiler itself needs WIN32 platform
- featuring unmaintainable code generator
- fully prepared for 32-bit and 64-bit machines



BUILTIN FUNCTIONS

	+ - * / %	addition, substraction, multiplication, division, mod-ation
			example: (+ 1 (* 2 3))			

	& | ^ ~		bitwise and, or, xor, not
			example of nand: (~ (& a b))

	&& || ~~	logical and, or, not
			example of logical nor: (~~ (|| a b))

	< > >= <= = <>	comparison: obvious
			example: (< 1 2) yields 1 (= true)

	<< >> <~ ~>	bit juggling: shift left/right, rotate left/right
			example (~> 255 4) yields 61440

	eat		prevents stack corruption
			example:
				(defun problem (a)
					(+ 1 1)
					(+ 2 2)
					(+ 3 3)
				)
			function result is 6 and a corrupted stack
				(defun solution (a)
					(eat (+ 1 1))
					(eat (+ 2 2))
					(+ 3 3)
				)
			stack is happy, BP will not get garbage, program will not crash



SPECIAL LANGUAGE CONSTRUCTS

	defun		define function
			example:
				(defun function-name (parameterA parameterB)
					...
				)
			last expression is result of function

	if		condition
			example:
				(if	(eq n 5)
					(true-action 12)
					(faction-action 13)
				)

	loop		repeat an expression a number of times
			features assigning the loop counter to a local variable
			example:
				(defun loopy (n)
					(loop 12 n
						(foo n)
					)
				)

	print		who could live without it?
			example:
				(print (~ 0))




COPYRIGHT (C) 2005 De Jong & Molenaar Compiler Construction
