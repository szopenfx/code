; The bucket of evil -- More fucked up than the primordial soup -- Up yours, JVM!

BEGINF MACRO
push bp
mov bp, sp
#EM

ENDF MACRO
pop bp
ret
#EM

QUIT MACRO
mov ah, 4ch
int 21h
#EM

PUSHCONST MACRO
mov ax, #1
push ax
#EM

PUSHRESULT MACRO push ax #EM

LOOPCOUNT MACRO mov cx, #1 #EM

CONDITION_START MACRO
	GETP0 ax
	GETP1 bx
	cmp ax, bx
#EM

CONDITION_END MACRO
	mov ax, 0
	db 0EBh, 03h	; jmp +3
	mov ax, 1
#EM

GETP0 MACRO mov #1, [bp+4] #EM
GETP1 MACRO mov #1, [bp+6] #EM
GETP2 MACRO mov #1, [bp+8] #EM
GETP3 MACRO mov #1, [bp+10] #EM
GETP4 MACRO mov #1, [bp+12] #EM
GETP5 MACRO mov #1, [bp+14] #EM
GETP6 MACRO mov #1, [bp+16] #EM
GETP7 MACRO mov #1, [bp+18] #EM
GETP8 MACRO mov #1, [bp+20] #EM
GETP9 MACRO mov #1, [bp+22] #EM

CLEAR1 MACRO add sp, 2 #EM
CLEAR2 MACRO add sp, 4 #EM
CLEAR3 MACRO add sp, 6 #EM
CLEAR4 MACRO add sp, 8 #EM
CLEAR5 MACRO add sp, 10 #EM
CLEAR6 MACRO add sp, 12 #EM
CLEAR7 MACRO add sp, 14 #EM
CLEAR8 MACRO add sp, 16 #EM
CLEAR9 MACRO add sp, 18 #EM

SETCOUNTER0 MACRO mov [bp+4], cx #EM
SETCOUNTER1 MACRO mov [bp+6], cx #EM
SETCOUNTER2 MACRO mov [bp+8], cx #EM
SETCOUNTER3 MACRO mov [bp+10], cx #EM
SETCOUNTER4 MACRO mov [bp+12], cx #EM
SETCOUNTER5 MACRO mov [bp+14], cx #EM
SETCOUNTER6 MACRO mov [bp+16], cx #EM
SETCOUNTER7 MACRO mov [bp+18], cx #EM
SETCOUNTER8 MACRO mov [bp+20], cx #EM
SETCOUNTER9 MACRO mov [bp+22], cx #EM

CALL0 MACRO
	call #1
#EM
CALL1 MACRO
	call #1
	CLEAR1
#EM
CALL2 MACRO
	call #1
	CLEAR2
#EM
CALL3 MACRO
	call #1
	CLEAR3
#EM
CALL4 MACRO
	call #1
	CLEAR4
#EM
CALL5 MACRO
	call #1
	CLEAR5
#EM
CALL6 MACRO
	call #1
	CLEAR6
#EM
CALL7 MACRO
	call #1
	CLEAR7
#EM
CALL8 MACRO
	call #1
	CLEAR8
#EM
CALL9 MACRO
	call #1
	CLEAR9
#EM

