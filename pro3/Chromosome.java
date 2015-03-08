/* Class for a single individual in the population */

import java.util.Random;
import java.util.ArrayList;

class Chromosome {
	/* PRNG to be used by all the chromosomes */
	private static final Random prng = new Random();
	private static final Double mutationProbability = 0.001;
	private static final Integer numRules = 3;
	private static final Integer numOperators = 2;
	private static final char[] rules = { 'm', 's', 'e' };
	private static final char[] operators = { '&', '|' };

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
			if (prng.nextDouble() > mutationProbability) continue; // Skip the Ith character with probability 0.999 to mutate with probability 0.001
			if (i%5 == 0) representation.setCharAt(i, rules[prng.nextInt(numRules)]); // The Ith character is a rule if it is congruent to 0 mod 5
			else if (i%5 == 4) representation.setCharAt(i, operators[prng.nextInt(numOperators)]); // The Ith character is an operator if it is congruent to 4 mod 5
			else representation.setCharAt(i, Character.forDigit(prng.nextInt(10), 10)); // Otherwise the Ith character is a number
		}
	}

	public void calculateFitness(ArrayList<Double> history) {

	}

	/* Creates a random individual representation */
	public static String randomIndividual() {
		StringBuilder r = new StringBuilder();

		/* Create a random chromosome by randomly choosing values for each
		 * character in the chromosome's representation */
		for (int j = 0; j < 3; ++j) {
			r.append( operators[ prng.nextInt( numOperators ) ] );
			r.append( rules[ prng.nextInt( numRules ) ] );
			for (int i = 0; i < 3; ++i) r.append( prng.nextInt(10) );
		}

		/* Remove extraneous beginning operators */
		return r.substring(1);
	}
}
