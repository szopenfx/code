
; 32 syscall:eax params=ebx,ecx,edx,esi,edi
; 64 syscall:rax params=rdi,rsi,rdx,rcx/r10,r8,r9

EXTERN error

GLOBAL sys_write

GLOBAL lx_open_rw
GLOBAL lx_close
GLOBAL lx_write
GLOBAL lx_write_fd
GLOBAL lx_lseek
GLOBAL lx_mmap
GLOBAL lx_msync
GLOBAL lx_munmap
GLOBAL lx_madvise_sequential
GLOBAL lx_madvise_normal
GLOBAL lx_mremap
GLOBAL lx_exit
GLOBAL lx_fsync

GLOBAL STDIN_FILENO
GLOBAL STDOUT_FILENO
GLOBAL STDERR_FILENO

SECTION .data

    %include "util.inc"
    %include "linux.inc"

SECTION .text

lx_open_rw:
    ;-> rax : ptr to filename
    ;<- rax : fd
    mov     rbx, O_RDWR

lx_open:
    ;-> rax : ptr to filename
    ;-> rbx : flags
    ;<- rax : fd
    mov     rdi, rax
    mov     rsi, rbx
    mov     rdx, 0
    mov     rax, sys_open
    syscall
    check   rax, error
    ret

lx_close:
    ;-> rax : fd
    mov     rdi, rax
    mov     rax, sys_close
    syscall
    check   rax, error
    ret

lx_lseek:
    ;-> rax : fd
    ;-> rbx : offset
    mov     rdi, rax
    mov     rsi, rbx
    mov     rdx, SEEK_SET
    mov     rax, sys_lseek
    syscall
    check   rax, error
    ret

lx_write:
    ;-> rax : pointer to string
    ;-> rbx : length of string
    mov     rcx, STDOUT_FILENO

lx_write_fd:
    ;-> rax : pointer to string
    ;-> rbx : length of string
    ;-> rcx : fd
    mov     rdi, rcx
    mov     rsi, rax
    mov     rdx, rbx
    mov     rax, sys_write
    syscall
    check   rax, error
    ret

lx_fsync:
    ;-> rax : fd
    ;<- rax : ??
    mov     rdi, rax
    mov     rax, sys_fsync
    syscall
    check   rax, error
    ret

lx_mmap:
    ;-> rax : size of buffer
    ;-> rbx : fd of backing file (-1 for no file)
    ;<- rax : pointer to start of buffer
    mov     r14, MAP_SHARED
    mov     r15, MAP_SHARED|MAP_ANONYMOUS
    mov     rdi, 0x0000100000000000
    mov     rsi, rax
    mov     rdx, PROT_READ|PROT_WRITE
    cmp     rbx, -1
    cmovne  r10, r14
    cmove   r10, r15
    mov     r8, rbx
    mov     r9, 0
    mov     rax, sys_mmap
    syscall
    check   rax, error
    ret

lx_msync:
    ;-> rax : address of buffer
    ;-> rbx : size of buffer
    mov     rdi, rax
    mov     rsi, rbx
    mov     rdx, MS_SYNC
    mov     rax, sys_msync
    syscall
    check   rax, error
    ret

lx_mremap:
    ;-> rax : address of buffer
    ;-> rbx : current size of buffer
    ;-> rcx : new size of buffer
    ;<- rax : new address of buffer
    mov     rdi, rax
    mov     rsi, rbx
    mov     rdx, rcx
    mov     rax, sys_mremap
    mov     r10, MREMAP_MAYMOVE
    syscall
    check   rax, error
    ret

lx_munmap:
    ;-> rax : address of buffer
    ;-> rbx : new size of buffer
    mov     rdi, rax
    mov     rsi, rbx
    mov     rax, sys_munmap
    syscall
    check   rax, error
    ret

lx_madvise_normal:
    mov     rcx, MADV_NORMAL
    jmp     lx_madvise

lx_madvise_sequential:
    ;-> rax : address of buffer
    ;-> rbx : size of buffer
    mov     rcx, MADV_SEQUENTIAL

lx_madvise:
    ;-> rax : address of buffer
    ;-> rbx : size of buffer
    ;-> rcx : advice (MADV_*)
    mov     rdi, rax
    mov     rsi, rbx
    mov     rdx, rcx
    mov     rax, sys_madvise
    syscall
    check   rax, error
    ret

lx_exit:
    mov     rdi, rax
    mov     rax, sys_exit
    syscall


