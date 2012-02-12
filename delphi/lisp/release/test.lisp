(defun foo (a b)
	(+ a b)
)

(defun bar (a b)
	(* a b)
)

(defun baz? (a b)
	(+
		(foo a b)
		(bar a b)
	)
)



(print
	(+
		(if 1 46 23)
		(if 0 23 46)
	)
)

(print 6)
	
(print 
	(eat (+ 2 3))
	(+ 1 2)
)

(print (baz? 3 4))

(print 
	(foo 1 2)
)

(print 
	(bar 3 (foo 4 5))
)

(print
	(baz? 12 13)
)

(print (+ 2 3))
(print (- 5 4))
(print (* 6 7))
(print (/ 15 3))
(print (% 17 3))
(print (& 3 6))
(print (| 3 6))
(print (~ 1))
(print (^ 5 1))
(print (&& 1 1))
(print (|| 1 0))
(print (~~ 2))
(print (= 1 1))
(print (< 1 2))
(print (> 1 2))
(print (<= 1 2))
(print (>= 1 2))
(print (<< 4 2))
(print (>> 256 2))
(print (<~ 255 4))
(print (~> 61440 12))

