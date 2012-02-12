#!/bin/bash
# bfish - BrainFuck interpreter in baSH
# copyright(c)2004 Joost Molenaar
# depend: bash printf cat grep awk od

filename=kitty
interprethash=
dbg=no
dbg_dump=false
dbg_pre=false
fastmode=no

if [ $# -eq 0 ]; then
  echo -n "Usage: $0 "
  cat << EOF
filename [options]
Options:
-d -debug       print state after every instruction
-h -hash        interpret # symbol to dump data array
-f -fast	fast mode; requires bfc.sh
EOF
  exit 1
fi

while [ $# -gt 0 ]; do
  case $1 in
    (-debug | -d)
      dbg=yes
      dbg_dump='echo -n "${code[*]}" >&2'
      dbg_pre='printf "\n%3d %2d %3d %s " $ip $ptr ${data[ptr]} "${code[ip]}" >&2'
      shift
      ;;
    (-hash | -h)
      interprethash="#"
      shift
      ;;
    (-fast | -f)
      fastmode=yes
      shift
      ;;
    (*)
      filename=$1
      shift
      ;;
  esac
done

[ -n "$interprethash" -a $dbg = no ] && fixhash='\n'

language='-+<>[],.'
language="$language$interprethash"
codesize=0
ip=0
ptr=0 highest=0
data[0]=0
stack[0]=0
stacksize=0

openingbracket() {
  stack[stacksize]=$codesize
  ((stacksize++))
}

closingbracket() {
  ((stacksize--))
  local gg=${stack[stacksize]}
  bracket[codesize]=$gg
  bracket[gg]=$codesize
}
  
if [ $fastmode = no ]; then
  source=`cat $filename` || exit 1
  for (( i=0; i < ${#source}; i++ )); do
    char=${source:i:1}
    for (( j=0; j < ${#language}; j++ )); do
      if [ "$char" = "${language:j:1}" ]; then
        code[codesize]=$char
        bracket[codesize]=0
        
        [ "$char" = "[" ] && openingbracket
        [ "$char" = "]" ] && closingbracket
        
        ((codesize++))
        break
      fi
    done
  done
fi

interpret() {
  eval $dbg_dump
  while [ $ip -le $codesize ]; do
    eval $dbg_pre
    case "x${code[ip]}" in
      ("x-") [ ${data[ptr]} -gt 0 ] && ((data[ptr]--)); ;;
      ("x+") [ ${data[ptr]} -lt 255 ] && ((data[ptr]++)); ;;
      ("x<") [ $ptr -gt 0 ] && ((ptr--)); ;;
      ("x>") ((ptr++))
        [ $ptr -gt $highest ] && data[ptr]=0 && highest=$ptr; ;;
      ("x[") [ ${data[ptr]} -eq 0 ] && ip=${bracket[ip]}; ;;
      ("x]") [ ${data[ptr]} -gt 0 ] && ip=${bracket[ip]}; ;;
      ("x,")
        IFS=$'\n\n\n' read -n 1 -s -r
        data[ptr]=`echo "$REPLY" | od -d -N 1 | grep 0000000 | awk '{print $2}'`
        ;;
      ("x.") echo -en "\\`printf %o ${data[ptr]}`"; ;;
      ("x#") echo -en " ${data[@]}$fixhash"; ;;
    esac
    ((ip++))
  done
}

if [ $fastmode = no ]; then
  interpret
else
  if [ -x ./bfc.sh ]; then
    ./bfc.sh $filename > $filename.c
    gcc $filename.c
    rm $filename.c
    ./a.out
    rm a.out
  else
    echo "$0: ./bfc.sh not found, fast mode not available"
    exit 1
  fi
fi

echo
