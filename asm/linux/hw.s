%include "util.inc"
%include "linux.h"
%include "str.h"

GLOBAL _start
GLOBAL error

SECTION .data

    mesg            db "Hello, world!"
                    db 10, 0

    arg_count_mesg  db "Argument count: "
                    db 0

SECTION .text

_start:
    ; print message
    mov     rdi, mesg
    call    print_strz
    
    mov     rdi, 65
    call    print_chr
    
    mov     rdi, 10
    call    print_chr

t1:
    mov     rdi, 1234
    call    print_num
    
    mov     rdi, 10
    call    print_chr

    ; print number of args
    mov     rdi, arg_count_mesg
    call    print_strz
    
    mov     rdi, [rsp]
    call    print_num
    
    mov     rdi, 10
    call    print_chr
    
    ;call_2  print_num_radix, [rsp], 16
    ;call_1  print_chr, 10

    ;call_2  print_num_radix, 0xFEDCBA9876543210, 16
    ;call_1  print_chr, 10

    ;call_1  lx_mmap, 0x70000000
    ;push    rax
    ;mov     rbx, 16
    ;pop     rax
    ;call    print_num_radix
    ;call_1  print_chr, 10
    ;call_2  print_num_radix, 0x70000000, 10
    ;call_1  print_chr, 10

    ; print first arg
    ;call_1  print_strz, [rsp+8]
    ;call_1  print_chr, 10

    ; exit with code 0
    mov     rax, 0
    call    lx_exit

error:
    ; bail out on error, set error code as return value
    call    lx_exit

