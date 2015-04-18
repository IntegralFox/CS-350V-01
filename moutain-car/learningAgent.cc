#include <iostream>
#include <iomanip>

#include "mc.h"
#include "krandom.h"

#define NUM_ACTIONS 3
#define NUM_DISCRETIZATIONS 11
#define X_MIN -1.2
#define X_MAX 0.5
#define V_MIN -0.07
#define V_MAX 0.07
#define EPSILON 0.1
#define GAMMA 0.1

int discretize(double, double, double, int);
int eGreedyChoose(double*, double);
int maxAction(double*);

int main() {
	int numEpisodes, verbosityFrequency;
	double alpha;

	std::cout << "Enter the number of episodes to train over: " << std::flush;
	std::cin >> numEpisodes;

	std::cout << "Enter the value of alpha to use in policy control: " << std::flush;
	std::cin >> alpha;

	verbosityFrequency = 1000;

	// Initialize Q[x][v][a] arbitrarily
	double Q[NUM_DISCRETIZATIONS][NUM_DISCRETIZATIONS][NUM_ACTIONS];
	for (int x = 0; x < NUM_DISCRETIZATIONS; ++x)
		for (int v = 0; v < NUM_DISCRETIZATIONS; ++v)
			for (int a = 0; a < NUM_ACTIONS; ++a)
				Q[x][v][a] = choose_random_value();

	// Learn for N episodes
	for (int episode = 0; episode < numEpisodes; ++episode) {
		mcar simulator;
		int x, x1, v, v1, a, a1;

		if (episode % verbosityFrequency == 0) {
			simulator = mcar(0, 0);
		}

		v = discretize(simulator.curr_vel(), V_MIN, V_MAX, NUM_DISCRETIZATIONS);
		x = discretize(simulator.curr_pos(), X_MIN, X_MAX, NUM_DISCRETIZATIONS);
		a = eGreedyChoose(Q[x][v], EPSILON);

		// Learn for N steps
		int step = 0;
		while (!simulator.reached_goal() && step < 1000) {
			simulator.update_position_velocity(static_cast<ACTION>(a));
			int r = simulator.reward();
			v1 = discretize(simulator.curr_vel(), V_MIN, V_MAX, NUM_DISCRETIZATIONS);
			x1 = discretize(simulator.curr_pos(), X_MIN, X_MAX, NUM_DISCRETIZATIONS);
			a1 = eGreedyChoose(Q[x1][v1], EPSILON);

			int aMax = maxAction(Q[x1][v1]);
			Q[x][v][a] += alpha * (r + GAMMA * Q[x1][v1][aMax] - Q[x][v][a]);

			v = v1;
			x = x1;
			a = a1;

			++step;
		}

		if (episode % verbosityFrequency == 0) {
			std::cout << step << std::endl << std::flush;
		}
	}

	// Run Test starting at bottom of valley
	std::cout << std::endl << "Testing agent on simulation starting at 0 with velocity 0" << std::endl;
	mcar simulator(0, 0);
	int step = 0;

	while (!simulator.reached_goal() && step < 1000) {
		int x = discretize(simulator.curr_pos(), X_MIN, X_MAX, NUM_DISCRETIZATIONS);
		int v = discretize(simulator.curr_vel(), V_MIN, V_MAX, NUM_DISCRETIZATIONS);
		int a = maxAction(Q[x][v]);

		std::cout << "Step: " << std::right << std::setw(3) << step
			<< " | X: " << std::setw(8) << std::setprecision(5) << std::fixed << simulator.curr_pos()
			<< " | V: " << std::setw(8) << std::setprecision(5) << std::fixed << simulator.curr_vel()
			<< " | Action Taken: " << static_cast<ACTION>(a) << std::endl;

		simulator.update_position_velocity(static_cast<ACTION>(a));
		++step;
	}

	if (simulator.reached_goal()) {
		std::cout << "Reached top of hill in " << step << " steps." << std::endl;
	} else {
		std::cout << "Did not reach goal before timeout." << std::endl;
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
		return maxAction(state);
	}
}

int maxAction(double* state) {
	if (state[0] > state[1] && state[0] > state[2]) {
		return 0;
	} else if (state[1] > state[2]) {
		return 1;
	} else {
		return 2;
	}
}
