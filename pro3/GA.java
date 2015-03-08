/* Genetic Algorithm class to contain the population during evolution */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class GA {
	/* PRNG to be used for random calculations */
	private static final Random prng = new Random();

	/* "Constants" that define the behaviour of chromosomes */
	private static final Integer POPULATION_SIZE = 20;

	/* Member Variables */
	private ArrayList<Chromosome> population;
	private ArrayList<Double> stockHistory;
	private Double populationFitnessSum;

	/* Constructor */
	public GA(ArrayList<Double> sh) {
		stockHistory = sh;
		populationFitnessSum = 0;

		/* Create the initial random population */
		for (int i = 0; i < POPULATION_SIZE; ++i) {
			Chromosome individual = new Chromosome();
			individual.calculateFitnessWith(stockHistory);
			population.add(individual);
			populationFitnessSum += individual.fitness;
		}

		/* Sort the populatin in descending order using the Collections' reverse
		 * order comparator */
		Collections.sort(population, Collections.reverseOrder());
	}

	/* Member Methods */
	public StringBuilder rouletteSelect() {
		Double ball = prng.nextDouble() * populationFitnessSum;
		Iterator<Chromosome> populationIterator = population.listIterator();
		Chromosome currentIndividual = populationIterator.next();

		/* Subtrack fitnesses until the ball "lands" on an individual */
		while (ball - currentIndividual.fitness > 0 && populationIterator.hasNext()) {
			ball -= currentIndividual.fitness;
			currentIndividual = populationIterator.next();
		}

		return currentIndividual.representation;
	}
}
