#include <iostream>

#include "mc.h"
#include "krandom.h"

#define NUM_ACTIONS 3
#define NUM_DISCRETIZATIONS 11
#define X_MAX -1.2
#define X_MIN 0.5
#define V_MIN -0.07
#define V_MAX 0.07

int main() {
	int numEpisodes;
	double epsilon;

	std::cout << "Enter the number of episodes to train over: " << std::flush;
	std::cin >> numEpisodes;

	std::cout << "Enter the value of epsilon to use in learning: " << std::flush;
	std::cin >> epsilon;

	double Q[NUM_DISCRETIZATIONS][NUM_DISCRETIZATIONS][NUM_ACTIONS];
	for (int x = 0; x < NUM_DISCRETIZATIONS; ++x)
		for (int v = 0; v < NUM_DISCRETIZATIONS; ++v)
			for (int a = 0; a < NUM_ACTIONS; ++a)
				Q[x][v][a] = choose_random_value();
}
