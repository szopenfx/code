GLOBAL _start

SECTION .text

_start:
    mov rax, 0x3c
    mov rdi, 0x00
    syscall
