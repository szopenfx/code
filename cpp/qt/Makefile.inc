PLATFORM=freebsd

ifeq ($(PLATFORM),freebsd)
MAKE=gmake
QTDIR=/usr/X11R6
endif

ifeq ($(PLATFORM),linux)
MAKE=make
QTDIR=/usr/lib/qt
endif

export MAKE QTDIR