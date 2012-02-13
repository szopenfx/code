
include macros16.inc

newline: db 13, 10, '$'

;=====
; ROR
;=====
builtin_ror:
	BEGINF
	push cx
	GETP0 ax
	GETP1 cx
	ror ax, cl
	pop cx
	ENDF

;=====
; ROL
;=====
builtin_rol:
	BEGINF
	push cx
	GETP0 ax
	GETP1 cx
	rol ax, cl
	pop cx
	ENDF

;=====
; SHL
;=====
builtin_shl:
	BEGINF
	push cx
	GETP0 ax
	GETP1 cx
	shl ax, cl
	pop cx
	ENDF

;=====
; SHR
;=====
builtin_shr:
	BEGINF
	push cx
	GETP0 ax
	GETP1 cx
	shr ax, cl
	pop cx
	ENDF

;=====
; EAT
;=====
builtin_eat:
	BEGINF
	GETP0 ax
	ENDF

;=====
; ADD
;=====
builtin_add:
        BEGINF
	GETP0 ax
	GETP1 bx
	add ax, bx
        ENDF

;=====
; MUL
;=====
builtin_mul:
        BEGINF
	GETP0 ax
	GETP1 bx
	mul bx
        ENDF

;=====
; SUB
;=====
builtin_sub:
	BEGINF
	GETP0 ax
	GETP1 bx
	sub ax, bx
	ENDF

;=====
; DIV
;=====
builtin_div:
	BEGINF
	GETP0 ax
	GETP1 bx
	mov dx, 0
	div bx
	ENDF

;=====
; MOD
;=====
builtin_mod:
	BEGINF
	GETP0 ax
	GETP1 bx
	mov dx, 0
	div bx
	mov ax, dx
	ENDF

;====
; EQ
;====
builtin_eq:
	BEGINF
	CONDITION_START
	db 74h, 05h	; je +5
	CONDITION_END
	ENDF

;====
; NE
;====
builtin_ne:
	BEGINF
	CONDITION_START
	db 75h, 05h	; jne +5
	CONDITION_END
	ENDF

;====
; GT
;====
builtin_gt:
	BEGINF
	CONDITION_START
	db 7Fh, 05h	; jg +5
	CONDITION_END
	ENDF

;====
; LT
;====
builtin_lt:
	BEGINF
	CONDITION_START
	db 7Ch, 05h	; jl +5
	CONDITION_END
	ENDF

;====
; GE
;====
builtin_ge:
	BEGINF
	CONDITION_START
	db, 7Dh, 05h	; jge +5
	CONDITION_END
	ENDF

;====
; LE
;====
builtin_le:
	BEGINF
	CONDITION_START
	db 7Eh, 05h	; jle +5
	CONDITION_END
	ENDF

;=====
; AND  (bitwise)
;=====
builtin_bw_and:
	BEGINF
	GETP0 ax
	GETP1 bx
	and ax, bx
	ENDF

;====
; OR (bitwise)
;====
builtin_bw_or:
	BEGINF
	GETP0 ax
	GETP1 bx
	or ax, bx
	ENDF

;=====
; NOT
;=====
builtin_bw_not:
	BEGINF
	GETP0 ax
	not ax
	ENDF

;=====
; XOR
;=====
builtin_bw_xor:
	BEGINF
	GETP0 ax
	GETP1 bx
	xor ax, bx
	ENDF

;=====
; AND (logical)
;=====
builtin_log_and:
	BEGINF
	GETP0 ax
	GETP1 bx
	and ax, bx
	cmp ax, 0
	db 7Fh, 05h	; jg +5
	CONDITION_END
	ENDF

;====
; OR (logical)
;====
builtin_log_or:
	BEGINF
	GETP0 ax
	GETP1 bx
	or ax, bx
	cmp ax, 0
	db 7Fh, 05h	; jg +5
	CONDITION_END
	ENDF

;=====
; NOT (logical)
;=====
builtin_log_not:
	BEGINF
	GETP0 ax
	not ax
	cmp ax, 0
	db 7Fh, 05h	; jg +5
	CONDITION_END
	ENDF 

;=======
; PRINT
;=======
builtin_print_ax:
	BEGINF
	mov dx, 0
	mov bx, 10
	div bx
	cmp ax, 0
	je builtin_print_ax_doprint

	push dx
	call builtin_print_ax
	pop dx

builtin_print_ax_doprint:
	add dl, 48
	pusha
	mov ah, 2
	int 21h
	popa
	ENDF

builtin_print:
	BEGINF
	GETP0 ax
	call builtin_print_ax
	mov dx, newline
	mov ah, 9
	int 21h
	ENDF

;====
; IF 
;====
; = 0 : false
; > 0 : true
builtin_if:
	BEGINF
	GETP0 ax
	cmp ax, 0
	je builtin_if_false

builtin_if_true:
	GETP1 ax
	jmp builtin_if_done

builtin_if_false:
	GETP2 ax

builtin_if_done:
	mov [bp+2], ax ; modify ip on stack so ret jumps to right spot
	ENDF
