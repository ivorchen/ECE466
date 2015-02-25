JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -rf *.class

%.class : %.java
	$(JAVAC) $<
