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
	private ArrayList<ArrayList<Double>> stockHistory;
	private Double populationFitnessSum;

	/* Constructor */
	public GA(ArrayList<ArrayList<Double>> sh) {
		population = new ArrayList<Chromosome>();
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
		/* Produce N Generations */
		for (int generation = 0; generation < NUM_GENERATIONS; ++generation) {
			/* Select two parents */
			StringBuilder mother = rouletteSelectParent();
			StringBuilder father = rouletteSelectParent();

			/* Select a crossover point in the range [1,13] */
			Integer crossoverIndex = prng.nextInt(13) + 1;

			/* Produce two children with crossover */
			Chromosome daughter = crossParents(mother, father, crossoverIndex);
			Chromosome son = crossParents(father, mother, crossoverIndex);

			/* Apply random mutation */
			daughter.mutate();
			son.mutate();

			/* Calculate the fitness of the children */
			daughter.calculateFitnessWith(stockHistory);
			son.calculateFitnessWith(stockHistory);

			/* Add childrent to population and update the fitness sum */
			population.add(daughter);
			population.add(son);
			populationFitnessSum += daughter.getFitness();
			populationFitnessSum += son.getFitness();

			/* Remove the two least fit individuals and remove their fitnesses
			 * from the population fitness sum */
			Collections.sort(population);
			populationFitnessSum -= population.remove(0).getFitness();
			populationFitnessSum -= population.remove(0).getFitness();
			Collections.reverse(population);

			if (generation % 10 == 0) {
				// ANSI control sequence interrupt to clear the terminal and reset the cursor position
				// http://en.wikipedia.org/wiki/ANSI_escape_code
				System.out.print("\33[H\33[J");
				System.out.print("Evolving Population [");
				for (int i = 0; i < 20; ++i) System.out.print(i < generation*20/NUM_GENERATIONS ? "#" : "-");
				System.out.print(String.format("] %3d%%", generation * 100 / NUM_GENERATIONS));

				// Ouput information about this generation
				System.out.println("\n\n\nFittest individuals:");
				for (int i = 0; i < 5; ++i) {
					Chromosome individual = population.get(i);
					System.out.print(individual.getRepresentation());
					System.out.print(" net: " + String.format("$%.2f", individual.getNetGain()));
					System.out.println("   fitness: " + String.format("%.0f", individual.getFitness()));
				}
			}
		}
	}

	public Chromosome crossParents(StringBuilder a, StringBuilder b, Integer c) {
		StringBuilder child = new StringBuilder();
		child.append(a.substring(0, c));
		child.append(b.substring(c));
		return new Chromosome(child);
	}

	public Chromosome fittest() {
		return population.get(0);
	}

	public StringBuilder rouletteSelectParent() {
		Double ball = prng.nextDouble() * populationFitnessSum;
		ListIterator<Chromosome> populationIterator = population.listIterator();
		Chromosome currentIndividual = populationIterator.next();

		/* Subtrack individuals' fitnesses until the ball "lands" on an individual */
		while (ball - currentIndividual.getFitness() > 0 && populationIterator.hasNext()) {
			ball -= currentIndividual.getFitness();
			currentIndividual = populationIterator.next();
		}

		return currentIndividual.getRepresentation();
	}
}
