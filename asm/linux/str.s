; Params userland: rdi rsi rdx rcx r8 r9
; Params syscalls: rdi rsi rdx r10 r8 r9

; Scratch: rax rcx rdx rsi rdi r8 r9 r10 
; Safe:    rbx rbp rsp r12 r13 r14 r15

%include "util.inc"
%include "linux.h"

EXTERN error

GLOBAL print_strz
GLOBAL print_chr
GLOBAL print_num
GLOBAL print_num_radix
GLOBAL str_len

SECTION .bss

    buf_size    equ 0x400
    buf         resb buf_size

SECTION .text

print_strz:
    ;-> rdi : ptr to asciiz string
    push    rdi
    call    str_len
    mov     rbx, rax
    pop     rax
    call    lx_write
    ret

print_chr:
    ;-> rdi : character to print
    mov     [buf], rdi
    mov     rax, buf
    mov     rbx, 1
    call    lx_write
    ret

print_num:
    ;-> rdi : number to print
    mov     rsi, buf
    mov     rdx, 10
    call    format_number
    mov     rdi, rax
    call    print_strz
    ret

print_num_radix:
    ;-> rax : number to print
    ;-> rbx : radix
    mov     rcx, rbx
    mov     rbx, buf
    call    format_number
    call    lx_write
    ret

str_len:
    ;-> rdi : ptr to asciiz string
    ;<- rax : length of ascii string
    mov     rdx, rdi
    xor     al, al
    xor     rcx, rcx
    not     rcx
    repne
    scasb
    sub     rdi, rdx
    dec     rdi
    mov     rax, rdi
    ret

format_number:
    ;-> rdi : number
    ;-> rsi : address of buffer
    ;-> rdx : radix
    ;<- rax : address of buffer
    mov     r8, 0x30
    mov     r9, 0x37
    mov     r10, rdx
    push    r11
    push    rsi
    mov     rax, rdi
    call    .recurse
    pop     rax
    pop     r11
    ret
.recurse:
    xor     rdx, rdx
    div     r10
    test    rax, rax
    jz      .done
    push    rdx
    call    .recurse
    pop     rdx
.done:
    cmp     dl, 0x0a
    cmovb   r11, r8
    cmovae  r11, r9
    add     rdx, r11
    mov     [rsi], dl
    inc     rsi
    ret
