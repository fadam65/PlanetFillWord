#       makefile for package FillWord5, assignment 3 for CSC 243,
#       Spring 2013, Dr. Dale Parson

all:		build

PACKAGE = FillWord5
TARGET = FillWord5
# This is the Java makefile.
# PLEASE SEE COMMENTS PRECEDING RULE logdata: BELOW.

DEBUG = 1

include ./makelib

TARGETS = $(CLASSFILES)

# STUDENTS: Run "gmake optout" from the command line to opt out of
# voluntary data collection. Once you opt out, you cannot opt back in
# for that assignment. Leaving automatic data collection on earns 1%
# bonus point for the assignment, where the assignment is worth 100%.
# Completing the survey in file survey.txt using a text editor
# earns another 1% bonus point.
# USING "gmake optout" removes the call to ./logdata.sh below.
logdata:
		@/bin/chmod +x ./logdata.sh
		@./logdata.sh $(STUDENT) "$$LOG_ACTION" $(JAVAFILES)    # LOG_DATA_TAG

optout:
		/bin/rm -f datamine/* datamine.zip
		grep -v LOG_DATA_TAG makefile > makefile.backup.txt
		cp makefile.backup.txt makefile

build:	logdata $(TARGETS) FillWord5.jar
		@LOG_ACTION="BUILT" $(MAKE) logdata

clean:		subclean
		/bin/rm -f *.class *.out *.dif testsave.dat *.dat FillWord5.jar

test:	build
		@LOG_ACTION="TESTING" $(MAKE) logdata
		@echo ASSIGNMENT1 TESTS STARTED
		java FillWord5.FillWordTest FillWord5.FillWordBasic 10 english.lower.txt 5 < testjava.txt > testjava.out 2> testjava.err.out
		diff testjava.out testjava.ref > testjava.dif
		diff testjava.err.out testjava.err.ref > testjava.err.dif
		@echo ASSIGNMENT1 TESTS PASS
		@echo ASSIGNMENT2 TESTS STARTED
		java FillWord5.FillWordTest FillWord5.FillWordGrows 10 english.lower.txt 5 < testjava2.txt > testjava2.out 2> testjava2.err.out
		diff testjava2.out testjava2.ref > testjava2.dif
		diff testjava2.err.out testjava2.err.ref > testjava2.err.dif
		@echo ASSIGNMENT2 TESTS PASS
		@echo ASSIGNMENT3 TESTS STARTED
		java FillWord5.FillWordTest FillWord5.FillWordGrows 10 english.lower.txt 5 < testjava3.txt > testjava3.out 2> testjava3.err.out
		diff testjava3.out testjava3.ref > testjava3.dif
		diff testjava3.err.out testjava3.err.ref > testjava3.err.dif
		@echo ASSIGNMENT3 TESTS PASS
		@echo ASSIGNMENT3MORE TESTS STARTED
		java FillWord5.FillWordTest FillWord5.FillWordGrows 10 english.lower.txt 5 < testjava3more.txt > testjava3more.out 2> testjava3more.err.out
		diff testjava3more.out testjava3more.ref > testjava3more.dif
		diff testjava3more.err.out testjava3more.err.ref > testjava3more.err.dif
		@echo ASSIGNMENT3MORE TESTS PASS
		@echo ASSIGNMENT4 TESTS STARTED
		java FillWord5.FillWordTest FillWord5.FillWordGrows 10 english.lower.txt 17 < testjava4.txt > testjava4.out 2> testjava4.err.out
		diff testjava4.out testjava4.ref > testjava4.dif
		diff testjava4.err.out testjava4.err.ref > testjava4.err.dif
		@echo ASSIGNMENT4 TESTS PASS
		@LOG_ACTION="TESTED" $(MAKE) logdata

testplay:	build
		java FillWord5.FillWordTest FillWord5.FillWordGrows 10 english.lower.txt -1

FillWord5.jar:	$(CLASSFILES)
			cd .. && jar vcfm FillWord5/FillWord5.jar FillWord5/manifest.txt FillWord5
