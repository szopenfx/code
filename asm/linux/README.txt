
=====================================
CDECL CALLING CONVENTION (32-BIT ABI)
=====================================

Caller:

	push P2
	push P1
	call Callee
	add esp, 8		; deallocate two params

Callee:

	push ebp
	mov ebp, esp
	sub esp, 4		; allocate 1 local

	mov eax, [ebp+8]	; P1
	mov ecx, [ebp+12]	; P2
	add eax, ecx
	mov [ebp-4], eax	; L1

	add esp, 4		; deallocate 1 local
	pop ebp
	ret

Scratch registers: eax, ecx, edx
Return value: eax

========================================
REGISTER CALLING CONVENTION (64-BIT ABI)
========================================

Params userland: rdi rsi rdx rcx r8 r9
Params syscalls: rdi rsi rdx r10 r8 r9
Scratch: rax rcx rdx rsi rdi r8 r9 r10 
Safe: rbx rbp rsp r12 r13 r14 r15

