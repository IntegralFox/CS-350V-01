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
	int numEpisodes, verbosityFrequency;
	double alpha, epsilon, gamma, verbosity;

	std::cout << "Enter the number of episodes to train over: " << std::flush;
	std::cin >> numEpisodes;

	std::cout << "Enter the value of epsilon to use in learning: " << std::flush;
	std::cin >> epsilon;

	std::cout << "Enter the value of gamma to use in learning: " << std::flush;
	std::cin >> gamma;

	std::cout << "Enter the value of alpha to use in learning: " << std::flush;
	std::cin >> alpha;

	std::cout << "Enter verbosity: " << std::flush;
	std::cin >> verbosity;
	verbosityFrequency = numEpisodes * (1-verbosity);

	// Initialize Q[x][v][a] arbitrarily
	double Q[NUM_DISCRETIZATIONS][NUM_DISCRETIZATIONS][NUM_ACTIONS];
	for (int x = 0; x < NUM_DISCRETIZATIONS; ++x)
		for (int v = 0; v < NUM_DISCRETIZATIONS; ++v)
			for (int a = 0; a < NUM_ACTIONS; ++a)
				Q[x][v][a] = choose_random_value();

	std::cout << "Agent is learning for " << numEpisodes << " episodes." << std::endl
		<< "Progress is printed every " << verbosityFrequency << " episodes." << std::endl
		<< "Steps taken:" << std::endl;

	// Learn for N episodes
	for (int episode = 0; episode < numEpisodes; ++episode) {
		mcar simulator;
		int x, x1, v, v1, a, a1;

		v = discretize(simulator.curr_vel(), V_MIN, V_MAX, NUM_DISCRETIZATIONS);
		x = discretize(simulator.curr_pos(), X_MIN, X_MAX, NUM_DISCRETIZATIONS);
		a = eGreedyChoose(Q[x][v], epsilon);

		// Learn for N steps
		int step = 0;
		while (!simulator.reached_goal() && step < 1000) {
			simulator.update_position_velocity(static_cast<ACTION>(a));
			int reward = simulator.reward();
			v1 = discretize(simulator.curr_vel(), V_MIN, V_MAX, NUM_DISCRETIZATIONS);
			x1 = discretize(simulator.curr_pos(), X_MIN, X_MAX, NUM_DISCRETIZATIONS);
			a1 = eGreedyChoose(Q[x1][v1], epsilon);

			Q[x][v][a] += alpha * (reward + gamma * Q[x1][v1][a1] - Q[x][v][a]);

			v = v1;
			x = x1;
			a = a1;

			++step;
		}

		if (episode % verbosityFrequency == 0) {
			std::cout << step << std::endl;
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
