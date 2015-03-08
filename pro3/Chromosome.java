/* Class for a single individual in the population */

import java.util.Random;
import java.util.ArrayList;

class Chromosome implements Comparable<Chromosome> {
	/* PRNG to be used by all the chromosomes */
	private static final Random prng = new Random();

	/* "Constants" that define the behaviour of chromosomes */
	private static final Double MUTATION_PROBABILITY = 0.001;
	private static final Integer NUM_RULES = 3;
	private static final Integer NUM_OPERATORS = 2;
	private static final char[] RULES = { 'm', 's', 'e' };
	private static final char[] OPERATORS = { '&', '|' };

	/* Member variables */
	public Double fitness;
	public Double netGain;
	public StringBuilder representation;

	/* Constructors */
	public Chromosome() {
		representation = new StringBuilder(randomIndividual());
	}

	public Chromosome(StringBuilder r) {
		representation = r;
	}

	/* Member Functions */
	public void mutate() {
		for (int i = 0; i < representation.length(); ++i) {
			if (prng.nextDouble() > MUTATION_PROBABILITY) continue; // Skip the Ith character with probability 0.999 to mutate with probability 0.001
			if (i%5 == 0) representation.setCharAt(i, RULES[prng.nextInt(NUM_RULES)]); // The Ith character is a rule if it is congruent to 0 mod 5
			else if (i%5 == 4) representation.setCharAt(i, OPERATORS[prng.nextInt(NUM_OPERATORS)]); // The Ith character is an operator if it is congruent to 4 mod 5
			else representation.setCharAt(i, Character.forDigit(prng.nextInt(10), 10)); // Otherwise the Ith character is a number
		}
	}

	public void calculateFitnessWith(ArrayList<Double> history) {

	}

	/* Creates a random individual representation */
	public static String randomIndividual() {
		StringBuilder r = new StringBuilder();

		/* Create a random chromosome by randomly choosing values for each
		 * character in the chromosome's representation */
		for (int j = 0; j < 3; ++j) {
			r.append( OPERATORS[ prng.nextInt( NUM_OPERATORS ) ] );
			r.append( RULES[ prng.nextInt( NUM_RULES ) ] );
			for (int i = 0; i < 3; ++i) r.append( prng.nextInt(10) );
		}

		/* Remove extraneous beginning operators */
		return r.substring(1);
	}

	/* Comparable Implementation for priority queue */
	public int compareTo(Chromosome m) {
		if (m == null) throw new NullPointerException("Attempted to compare with a null chromosome.");
		if (fitness == null || m.fitness == null) throw new NullPointerException("Fitness of a compared chromosome is null. Did you forget Chromosome::calculateFitness?");
		if (fitness < m.fitness) return -1;
		else if (fitness == m.fitness) return 0;
		else return 1;
	}
}
