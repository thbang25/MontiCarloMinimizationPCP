# Makefile for montecarloparallel
#Thabang sambo
#13 Aug 2023

#Directories and package
SRCDIR = src
BINDIR = bin
PACKAGE = /MonteCarloMini

#Compiler
JC = javac
JFLAGS = -d $(BINDIR) -sourcepath $(SRCDIR) -cp $(BINDIR)

#Java source files
SRCS = $(wildcard $(SRCDIR)/$(PACKAGE)/*.java)

#Compiled class files
CLASSES = $(patsubst $(SRCDIR)/%.java,$(BINDIR)/%.class,$(SRCS))

#Default target set rules
all: $(CLASSES)

#Compile Java files
$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JC) $(JFLAGS) $<

# Clean bin files
clean:
	rm -rf $(BINDIR)

.PHONY: all clean

