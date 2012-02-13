(defun fib (n)
	(if	
		(<= n 2)		
		(eat 1)
		(eat
			(+
				(fib (- n 1))
				(fib (- n 2))
			)
		)
	)
)

(defun dofib (n)
	(loop 24 n
		(print 
			(fib (- 25 n))
		)
	)
)

(dofib 65536)
