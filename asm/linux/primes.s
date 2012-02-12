; data aan eind van bestand schrijven om allocatie te forceren
; msync aanroepen wanneer we klaar zijn
; printf bedenken
; --> ook checken wat er gebeurt als die de buffer uit wandelt
; --> ook checken wat er gebeurt als die te veel of te weinig argumenten mee krijgt

%include "linux.h"
%include "util.inc"

GLOBAL _start
GLOBAL error

SECTION .bss

    fd          resq 1
    primes      resq 1
    primes_sz   resq 1

SECTION .data

    filename        db "primes.bin"
    null            db 0

SECTION .text


allocate_buffer:
    ; open file
    mov rax, filename
    call lx_open_rw

    ; save fd
    mov [fd], rax

    ; grow file to buf_sz
    call grow_file

    ; map file into memory
    mov rax, [primes_sz]
    shl rax, 3
    mov rbx, [fd]
    call lx_mmap

    ; save address
    mov [primes], rax

    ret

cleanup_buffer:
    ; unmap file
    mov rax, [primes]
    mov rbx, [primes_sz]
    call lx_munmap

    ; close file
    mov rax, [fd]
    call lx_close

    ret

grow_file:
    ; seek to end of file
    mov rax, [fd]
    mov rbx, [primes_sz]
    shl rbx, 3
    sub rbx, 1
    call lx_lseek

    ; write 0 to end of file
    mov rax, null
    mov rbx, 1
    mov rcx, [fd]
    call lx_write_fd

    ret

double_buffer:
    mov rax, [primes]
    mov rbx, [primes_sz]
    call lx_madvise_normal

    ; save old size on stack
    mov rax, [primes_sz]
    push rax

    ; make file bigger
    shl rax, 1
    mov [primes_sz], rax
    call grow_file

    ; remap file
    mov rax, [primes]
    pop rbx
    mov rcx, [primes_sz]
    shl rbx, 3
    shl rcx, 3
    call lx_mremap

    mov [primes], rax

    ; advise sequential reads through buffer
    call lx_madvise_sequential

    ret

find_primes:
    mov rdi, [primes]
    mov qword [rdi], 2
    mov qword [rdi+8], 3

    mov r8, 2               ; step
    mov r13, 5              ; candidate
    mov r12, 2              ; next prime

.test_next_candidate:
    zero r9                 ; index
    mov r10, [primes_sz]    ; count left

.test_next_prime:
    mov rax, [rdi+r9*8]     ; check prime@index < sqrt(candidate)
    mul rax
    cmp rax, r13
    ja .check_buffer_size

    mov rax, r13            ; check candidate mod prime@index > 0
    zero rdx
    div qword [rdi+r9*8]
    test rdx, rdx
    jz .next_candidate

    inc r9
    dec r10
    jnz .test_next_prime

.check_buffer_size:
    cmp r12, [primes_sz]
    jb .is_prime

    call double_buffer
    mov rdi, [primes]

.is_prime:
    mov [rdi+r12*8], r13
    inc r12

.next_candidate:
    prefetcht0 [rdi]
    add r13, r8
    jc .done
    xor r8, 6
    jmp .test_next_candidate

.done:
    ret

_start:
    mov qword [primes_sz], 512
    call allocate_buffer
    call find_primes
    call cleanup_buffer
    mov rax, 0
    call lx_exit

error:
    neg rax
    call lx_exit
