#!/bin/bash
# bfc.sh - brainfuck compiler
# copyright(c)2004 Joost Mole Naar

mode=c
filename=stdin
outfile=blbl_not_set
interprethash=
strip='strip $outfile'
compilefunction=compile_to_c
language='-+<>[],.'
language="$language$interprethash"
clanguage='MPLRBEIOH'
codesize=0
declare -a code
ip=0

if [ $# -eq 0 ]; then
        echo -n "Usage: $0 "
        cat << EOF
filename [options]
Options:
-h -hash    interpret # symbol to dump data array
-m -mode    select output mode: c (default), elf
-o -outfile output to this file
-s -symbols leave debugging symbols in ELF output file
-oz -optimize optimize sequences like +++
EOF
        exit 1
fi

while [ $# -gt 0 ]; do
  case $1 in
    (-hash | -h)    
      interprethash="#"
      shift
      ;;
    (-mode | -m)
      mode=$2
      shift 2
      ;;
    (-outfile | -o)
      outfile=$2
      shift 2
      ;;
    (-strip | -s)
      strip=true
      shift
      ;;
    (-optimize | -oz )
      compilefunction=compile_to_acceptable_c
      shift
      ;;
    (*)
      filename=$1
      shift
      ;;
  esac
done

if [ $mode = elf -a $outfile = blbl_not_set ]; then
  echo "use -o filename to specify output file name"
  exit 1
fi

source=`cat $filename` || exit 1
for (( i=0; i < ${#source}; i++ )); do
  char=${source:i:1}
  for (( j=0; j < ${#language}; j++ )); do
    if [ "$char" = "${language:j:1}" ]; then
      code[codesize]=$j
      ((codesize++))
      break
    fi
  done
done

compile_to_c() {
  cat << EOF
#define HEADER  main(){int a[30000];int p=29999;while(a[p]>0)a[p--]=0;
#define M       if(a[p]>0)a[p]--;
#define P       if(a[p]<255)a[p]++;
#define L       if(p>0)p--;
#define R       if(p<29999)p++;
#define B       while(a[p]>0) {
#define E       }
#define I       a[p]=(unsigned int)getchar();
#define O       printf("%s",&a[p]);
#define FOOTER  printf("\n");}
EOF
  echo "  HEADER"
  while [ $ip -lt $codesize ]; do
    j=${code[ip]}
    echo "    ${clanguage:j:1}"
    ((ip++))
  done
  echo "  FOOTER"
}

compile_to_acceptable_c() {
  cat << EOF
#define HEADER  main(){int a[30000];int p=29999;while(p>0)a[p--]=0;
#define M(X)    a[p]=a[p]>X?a[p]-X:0;
#define P(X)    a[p]=a[p]<256-X?a[p]+X:255;
#define L(X)    p=p>X?p-X:0;
#define R(X)    p=p<30000-X?p+X:29999;
#define B       while(a[p]>0){
#define E       }
#define I       a[p]=(unsigned int)getchar();
#define O       printf("%s",&a[p]);
#define FOOTER  printf("\n");}
EOF
  echo "  HEADER"
  while [ $ip -lt $codesize ]; do
    j=${code[ip]}
    echo -n "    ${clanguage:j:1}"
    if [ $j -lt 4 ]; then
      count=1
      while [ "${code[ip+1]}" = "${code[ip]}" ]; do
        ((count++, ip++))
      done
      echo "($count)"
    else
      echo
    fi
    ((ip++))
  done
  echo "  FOOTER"
}

case $mode in
  (c)
    if [ $outfile = blbl_not_set ]; then
      $compilefunction
    else
      $compilefunction > $outfile
    fi
    ;;
  (elf)
    $compilefunction > $filename.c &&
    gcc $filename.c -o $outfile &&
    chmod +x $outfile &&
    rm $filename.c &&
    eval $strip
    ;;
esac
