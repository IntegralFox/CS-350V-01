Project: Bayesian Network Probability Calculator
Author: Michael Welborn

Description: This probability calculator is written in C++ and utilizes many
features of the new C++11 spec. As such it requires a relatively recent C++
compiler to be built. It is recommended to use Clang, part of the LLVM project,
to compile the program. In GNU/Linux distributions, a suitable version of Clang
can be had from the repositories using the distribution's package manager:

# apt-get install clang
# yum install clang
# pacman -S clang
or similar

In Mac OSX, Clang is bundled with XCode and can be added as a command line
utility in the Downloads section of the XCode preferences menu.

Building: Requires clang++ and optionally make. While not necessary, this
program can be most easily built using GNU make by running:

$ make

in the root of the project. This will put the binary in the newly created bin
folder.

Alternatively, the program can be built without make by running the following
commands in the root of the project:

$ mkdir bin
$ clang++ -std=c++11 -o bin/probability src/*.cpp

Running: The program can be run by executing the probability binary in the bin
folder:

$ bin/probabilty

Following the interactive prompts will print the result of the calculations
given the provided query and evidence variables.
