#!/bin/bash

if [ $# -eq 1 -a "$1" = "32" ]; then
    unistd_arch_h='/usr/include/asm/unistd_32.h'
elif [ $# -eq 1 -a "$1" = "64" ]; then
    unistd_arch_h='/usr/include/asm/unistd_64.h'
else
    echo "usage: $0 32|64" >&2
    exit 1
fi

unistd_h='/usr/include/unistd.h'
mman_h='/usr/include/bits/mman.h'
fcntl_h='/usr/include/bits/fcntl.h'

parse_num() {
    n=$2
    echo "$1 $[n]"
}

get_def() {
    sed -nr "s/.*#\\s*define\\s+$1\s+$2.*/\\1 \\2/p" $3
}

get_dec() {
    get_def "$1" '([0-9]+)' $2
}

get_hex() {
    get_def "$1" '(0x\w+)' $2 | while read LINE; do
        n=`echo $LINE | cut -d ' ' -f 1`
        f=`echo $LINE | cut -d ' ' -f 2`
        echo "$n $[f]"
    done
}

get_oct() {
    get_def "$1" '(0[0-9]+)' $2 | while read LINE; do
        n=`echo $LINE | cut -d ' ' -f 1`
        f=`echo $LINE | cut -d ' ' -f 2`
        echo "$n $[f]"
    done
}

title() {
    echo
    echo '    ;'
    echo "    ; $1"
    echo '    ;'
}
main() {
    title 'file descriptors for stdin, stdout and stderr'
    get_dec '(STD\w+_FILENO)' $unistd_h | xargs printf '    %-32s equ 0x%02x\n'

    title 'flags for open(2) and friends'
    get_oct '(O_\w+)' $fcntl_h | xargs printf '    %-32s equ %08o\n'
    echo
    get_dec '(SEEK_\w+)' $unistd_h | xargs printf '    %-32s equ 0x%02x\n'

    title 'flags for mmap(2) and friends'
    get_hex '(PROT_\w+)' $mman_h | xargs printf '    %-32s equ 0x%08x\n'
    echo
    get_hex '(MAP_\w+)' $mman_h | xargs printf '    %-32s equ 0x%08x\n'
    echo
    get_dec '(MREMAP_\w+)' $mman_h | xargs printf '    %-32s equ 0x%02x\n'
    echo
    get_dec '(MS_\w+)' $mman_h | xargs printf '    %-32s equ 0x%02x\n'
    echo
    get_dec '(MADV_\w+)' $mman_h | xargs printf '    %-32s equ 0x%02x\n'

    title 'syscall numbers'
    get_dec '__NR_(\w+)' $unistd_arch_h | xargs printf '    sys_%-28s equ 0x%04x\n'
}

main