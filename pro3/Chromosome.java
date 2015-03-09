/* Class for a single individual in the population */

import java.util.Random;
import java.util.ArrayList;

class Chromosome implements Comparable<Chromosome> {
	/* PRNG to be used by all the chromosomes */
	private static final Random prng = new Random();

	/* "Constants" that define the behaviour of chromosomes */
	private static final Double MUTATION_PROBABILITY = 0.001;
	private static final Integer NUM_STOCKS = 5;
	private static final Integer NUM_RULES = 3;
	private static final Integer NUM_OPERATORS = 2;
	private static final char[] RULES = { 'm', 's', 'e' };
	private static final char[] OPERATORS = { '&', '|' };

	/* Member variables */
	private Double fitness;
	private Double netGain;
	private StringBuilder representation;

	/* Getter Methods */
	public Double getFitness() { return fitness; }
	public Double getNetGain() { return netGain; }
	public StringBuilder getRepresentation() { return representation; }

	/* Constructors */
	public Chromosome() {
		representation = randomIndividual();
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

	private Double ruleEMA(ArrayList<Double> history, Integer current, Integer length) {
		Double alpha = 1 - (2d / (length + 1));
		Double numerator = 0d;
		Double denominator = 0d;

		for (int i = 0; i < length; ++i, current += NUM_STOCKS) {
			Double alphaPow = Math.pow(alpha, i);
			numerator += history.get(current) * alphaPow;
			denominator += alphaPow;
		}

		return numerator / denominator;
	}

	private Double ruleSMA(ArrayList<Double> history, Integer current, Integer length) {
		Double sum = 0d;

		for (int i = 0; i < length; ++i, current += NUM_STOCKS) {
			sum += history.get(current);
		}

		return sum / length;
	}

	private Double ruleMAX(ArrayList<Double> history, Integer current, Integer length) {
		Double max = history.get(current);
		current += NUM_STOCKS;

		for (int i = 1; i < length; ++i, current += NUM_STOCKS) {
			if (max < history.get(current)) max = history.get(current);
		}

		return max;
	}

	/* Creates a random individual representation */
	public static StringBuilder randomIndividual() {
		StringBuilder r = new StringBuilder();

		/* Create a random chromosome by randomly choosing values for each
		 * character in the chromosome's representation */
		for (int j = 0; j < 3; ++j) {
			r.append( OPERATORS[ prng.nextInt( NUM_OPERATORS ) ] );
			r.append( RULES[ prng.nextInt( NUM_RULES ) ] );
			for (int i = 0; i < 3; ++i) r.append( prng.nextInt(10) );
		}

		/* Remove extraneous beginning operators */
		return new StringBuilder(r.substring(1));
	}

	/* Comparable Implementation for priority queue */
	public int compareTo(Chromosome m) {
		if (m == null) throw new NullPointerException("Attempted to compare with a null chromosome.");
		if (fitness == null || m.fitness == null) throw new NullPointerException("Fitness of a compared chromosome is null. Did you forget Chromosome::calculateFitnessWith?");
		if (fitness < m.fitness) return -1;
		else if (fitness == m.fitness) return 0;
		else return 1;
	}
}
