CC=clang++
CFLAGS=-c -Wall
LFLAGS=-o bin/probability

all: bin obj bin/probability

bin:
	mkdir bin

obj:
	mkdir obj

bin/probability: obj/probability.o obj/bn.o obj/node.o
	$(CC) $(LFLAGS) obj/probability.o obj/bn.o obj/node.o

obj/probability.o: src/probability.cpp src/bn.hpp src/node.hpp
	$(CC) $(CFLAGS) -o obj/probability.o src/probability.cpp

obj/bn.o: src/bn.cpp src/bn.hpp src/node.hpp
	$(CC) $(CFLAGS) -o obj/bn.o src/bn.cpp

obj/node.o: src/node.cpp src/node.hpp
	$(CC) $(CFLAGS) -o obj/node.o src/node.cpp

clean:
	rm -r bin obj
