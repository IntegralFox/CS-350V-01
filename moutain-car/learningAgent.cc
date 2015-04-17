#include <iostream>

#include "mc.h"
#include "krandom.h"

#define NUM_ACTIONS 3
#define NUM_DISCRETIZATIONS 11
#define X_MAX -1.2
#define X_MIN 0.5
#define V_MIN -0.07
#define V_MAX 0.07


int discretize(double, double, double, int);
int eGreedyChoose(double*, double);

int main() {
	int numEpisodes;
	double epsilon, verbosity;

	std::cout << "Enter the number of episodes to train over: " << std::flush;
	std::cin >> numEpisodes;

	std::cout << "Enter the value of epsilon to use in learning: " << std::flush;
	std::cin >> epsilon;

	std::cout << "Enter verbosity: " << std::flush;
	std::cin >> verbosity;

	// Initialize Q[x][v][a] arbitrarily
	double Q[NUM_DISCRETIZATIONS][NUM_DISCRETIZATIONS][NUM_ACTIONS];
	for (int x = 0; x < NUM_DISCRETIZATIONS; ++x)
		for (int v = 0; v < NUM_DISCRETIZATIONS; ++v)
			for (int a = 0; a < NUM_ACTIONS; ++a)
				Q[x][v][a] = choose_random_value();

	// Repeat for each episode
	for (int episode = 0; episode < numEpisodes; ++episode) {
		mcar simulator;

		for (int step = 0; !simulator.reached_goal(); ++step) {
			int v = discretize(simulator.curr_vel(), V_MIN, V_MAX, NUM_DISCRETIZATIONS);
			int x = discretize(simulator.curr_pos(), X_MIN, X_MAX, NUM_DISCRETIZATIONS);
			int action = eGreedyChoose(Q[x][v], epsilon);
			simulator.update_position_velocity(static_cast<ACTION>(action));
		}
	}
}

int discretize(double value, double valueMin, double valueMax, int discretizations) {
	double discSize = (valueMax - valueMin) / discretizations;
	double valueRelative = value - valueMin;
	return static_cast<int>(valueRelative / discSize);
}

int eGreedyChoose(double* state, double epsilon) {
	if (choose_random_value() < epsilon) {
		// Return a random action with epsilon chance
		return choose_random_int_value(2);
	} else {
		// Return best Q action with 1-epsilon chance
		if (state[0] > state[1] && state[0] > state[2]) {
			return 0;
		} else if (state[1] > state[2]) {
			return 1;
		} else {
			return 2;
		}
	}
}
