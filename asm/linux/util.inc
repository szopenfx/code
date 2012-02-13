%macro call_1 2
    mov rax, %2
    call %1
%endmacro

%macro call_2 3
    mov rax, %2
    mov rbx, %3
    call %1
%endmacro

%macro call_3 4
    mov rax, %2
    mov rbx, %3
    mov rcx, %4
    call %1
%endmacro

%macro check 2
    test %1, %1
    js %2
%endmacro

%macro zero 1
    xor %1, %1
%endmacro

%macro push2 2
    push %1
    push %2
%endmacro

%macro push3 3
    push2 %1 %2
    push %3
%endmacro

%macro push4 4
    push3 %1 %2 %3
    push %4
%endmacro

%macro pop2 2
    pop %1
    pop %2
%endmacro

%macro pop3 3
    pop2 %1 %2
    pop %3
%endmacro

%macro pop4 4
    pop3 %1 %2 %3
    pop %4
%endmacro