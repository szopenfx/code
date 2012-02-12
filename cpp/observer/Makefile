include Makefile.inc

TARGETS=dots
SOURCES=${wildcard *.cc}
OBJECTS=${patsubst %.cc,%.o,$(SOURCES)}
DEFINE=

LDFLAGS=-L$(QTDIR)/lib -lqt-mt -lpthread
CPPFLAGS=-O2 -Wall -I$(QTDIR)/include $(DEFINE)
CC=g++

default: all

all: $(TARGETS)

dots: $(OBJECTS)
	g++ $(LDFLAGS) $(OBJECTS) -o $@
$(OBJECTS): %.o : %.h

mainform.o: moc_mainform.h

moc_%.h: %.h
	moc $< -o $@

distclean: clean
	@rm -fv $(TARGETS)

clean:
	@rm -fv ${wildcard *.o} ${wildcard moc_*.h}

test: ${patsubst %, test-%, $(TARGETS)}

test-%: %
	./$<
