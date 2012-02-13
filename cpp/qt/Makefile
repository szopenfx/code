include Makefile.inc

TARGETS=qt1 qt2 qt3 qt4 qt5
LDFLAGS=-L$(QTDIR)/lib -lqt-mt -lpthread
CPPFLAGS=-O2 -Wall -I$(QTDIR)/include
CC=g++

default: all

all: $(TARGETS)

#qt1: qt1.o
#qt2: qt2.o
#qt3: qt3.o
#qt4: qt4.o
#qt5: qt5.o

clean:
	@rm -fv ${wildcard *.o}

distclean: clean
	@rm -fv $(TARGETS)

test: ${subst qt,test-,$(TARGETS)}

test-%: qt%
	./$<
