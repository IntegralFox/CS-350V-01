/* Genetic Algorithm class to contain the population during evolution */

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

class GA {
	/* PRNG to be used for random calculations */
	private static final Random prng = new Random();

	/* "Constants" that define the behaviour of chromosomes */
	private static final Integer POPULATION_SIZE = 20;

	/* Member Variables */
	private PriorityQueue<Chromosome> population;
	private ArrayList<Double> stockHistory;

	/* Constructors */
	public GA(ArrayList<Double> sh) {
		stockHistory = sh;

		/* Create the initial random population */
		for (int i = 0; i < POPULATION_SIZE; ++i) {
			Chromosome individual = new Chromosome();
			individual.calculateFitnessWith(stockHistory);
			population.add(individual);
		}
	}
}
