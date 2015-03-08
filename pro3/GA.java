/* Genetic Algorithm class to contain the population during evolution */

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Random;

class GA {
	/* PRNG to be used for random calculations */
	private static final Random prng = new Random();

	/* "Constants" that define the behaviour of chromosomes */
	private static final Integer POPULATION_SIZE = 20;
	private static final Integer NUM_GENERATIONS = 200;

	/* Member Variables */
	private ArrayList<Chromosome> population;
	private ArrayList<Double> stockHistory;
	private Double populationFitnessSum;

	/* Constructor */
	public GA(ArrayList<Double> sh) {
		stockHistory = sh;
		populationFitnessSum = 0d;

		/* Create the initial random population */
		for (int i = 0; i < POPULATION_SIZE; ++i) {
			Chromosome individual = new Chromosome();
			individual.calculateFitnessWith(stockHistory);
			population.add(individual);
			populationFitnessSum += individual.getFitness();
		}

		/* Sort the populatin in descending order using the Collections' reverse
		 * order comparator */
		Collections.sort(population, Collections.reverseOrder());
	}

	/* Member Methods */
	public void evolve() {

	}

	public Chromosome fittest() {
		return population.get(0);
	}

	public StringBuilder rouletteSelect() {
		Double ball = prng.nextDouble() * populationFitnessSum;
		ListIterator<Chromosome> populationIterator = population.listIterator();
		Chromosome currentIndividual = populationIterator.next();

		/* Subtrack fitnesses until the ball "lands" on an individual */
		while (ball - currentIndividual.getFitness() > 0 && populationIterator.hasNext()) {
			ball -= currentIndividual.getFitness();
			currentIndividual = populationIterator.next();
		}

		return currentIndividual.getRepresentation();
	}
}
