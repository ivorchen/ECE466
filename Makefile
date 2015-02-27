JAVAC=javac
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -rf *.class;rm -f TokenBucket/*.class

%.class : %.java
	$(JAVAC) $<
