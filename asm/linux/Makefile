
.PHONY: test dump names debug clean

TARGET=hw
TARGETS=primes short hw

test: test-$(TARGET)

#
# dependencies
#

primes: primes.o linux.o
	ld -o $@ $^

hw: hw.o str.o linux.o
	ld -o $@ $^

short: short.o
	ld -o $@ $^

primes.bin:
	touch $@

linux.inc: linux.inc.sh
	./linux.inc.sh 64 > $@

hw.o: util.inc linux.h str.h
primes.o: util.inc linux.h primes.bin
str.o: util.inc linux.h
linux.o: util.inc linux.inc

#
# running
#

test-primes: primes primes.bin
	./primes

test-hw: hw
	./hw a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20 a21 a22 a23 a24 a25 a26 a27 a28 a29 a30

test-short: short
	./short


#
# build rules
#

%.h: %.s
	sed -n '/^GLOBAL/s/GLOBAL/EXTERN/p' $< > $@

%.o: %.s
	nasm -f elf64 -g $<

#
# utilities
#

all: $(TARGETS)

dump: $(TARGET)
	objdump -M intel -d -t $<

names: $(TARGET)
	nm $<

debug: $(TARGET)
	gdb -q $<

tdebug: $(TARGET)
	gdb -tui $<

clean: clean-primes clean-linux-inc
	rm -f *.o *.h $(TARGETS) 

clean-primes:
	rm -f primes.bin

clean-linux-inc:
	rm -f linux.inc

