/* Class for a single individual in the population */

import java.util.Random;
import java.util.ArrayList;

class Chromosome implements Comparable<Chromosome> {
	/* PRNG to be used by all the chromosomes */
	private static final Random prng = new Random();

	/* "Constants" that define the behaviour of chromosomes */
	private static final Double MUTATION_PROBABILITY = 0.001d;
	private static final Double INITIAL_ACCOUNT_BALANCE = 20000d;
	public static final Integer NUM_STOCKS = 5;
	private static final Integer NUM_RULES = 3;
	private static final Integer NUM_OPERATORS = 2;
	private static final Integer FITNESS_DAYS = 30;
	private static final Double TRANSACTION_COST = 7d;
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

	public void calculateFitnessWith(ArrayList<ArrayList<Double>> history) {
		Double[] account = new Double[NUM_STOCKS];
		Integer[] shares = new Integer[NUM_STOCKS];
		boolean traded = false;

		// Initialize accounts with $20,000 and 0 shares
		for (int i = 0; i < NUM_STOCKS; ++i) {
			account[i] = INITIAL_ACCOUNT_BALANCE;
			shares[i] = 0;
		}

		// Loop for a specified number of days, trading shares based on the rule
		for (int day = history.get(0).size() - FITNESS_DAYS - 1; day < history.get(0).size(); ++day) {
			for (int company = 0; company < NUM_STOCKS; ++company) {
				Double daysClosingPrice = history.get(company).get(day);
				if (day == history.get(0).size() - 1) {
					// Sell all shares if it's the last day
					if (shares[company] > 0) {
						Integer shareCount = shares[company];
						account[company] += shareCount * daysClosingPrice - TRANSACTION_COST;
						shares[company] -= shareCount;
					}
				} else if (ruleSaysBuy(representation, history.get(company), day)) {
					traded = true;
					Double buyBudget = account[company] * 0.25d; // Buy quarter
					Integer shareCount = new Double(buyBudget / daysClosingPrice).intValue();
					shares[company] += shareCount;
					account[company] -= shareCount * daysClosingPrice + TRANSACTION_COST;
				} else {
					Integer shareCount = shares[company] / 4; // Sell quarter
					if (shareCount < 5) shareCount = shares[company]; // unless we have only 10 shares, then sell all
					account[company] += shareCount * daysClosingPrice - TRANSACTION_COST;
					shares[company] -= shareCount;
				}
			}
		}

		// Penalize individuals that do not trade by half their account balance
		if (!traded) for (int i = 0; i < NUM_STOCKS; ++i) account[i] /= 2;

		// Add up the gains from the 5 accounts
		netGain = 0d;
		for (int i = 0; i < NUM_STOCKS; ++i) netGain += account[i] - INITIAL_ACCOUNT_BALANCE;

		// Convert net gain to a positive monotonic fitness
		fitness = restrictRange(netGain);
	}

	public static boolean ruleSaysBuy(StringBuilder representation, ArrayList<Double> history, Integer day) {
		boolean buy;
		Double closingPrice = history.get(day);

		// Get the characteristics of the first rule in the set
		Integer length = new Integer(representation.substring(1, 4));
		Integer startDay = day - length - 1;
		String op = representation.substring(0, 1);

		if (op.equals("e")) {
			buy = ruleEMA(history, startDay, length) < closingPrice;
		} else if (op.equals("s")) {
			buy = ruleSMA(history, startDay, length) < closingPrice;
		} else {
			buy = ruleMAX(history, startDay, length) < closingPrice;
		}

		// Get the characteristics of the second rule in the set
		length = new Integer(representation.substring(6, 9));
		startDay = day - length - 1;
		op = representation.substring(5, 6);

		if (representation.substring(4, 5).equals("&")) {
			if (op.equals("e")) {
				buy &= ruleEMA(history, startDay, length) < closingPrice;
			} else if (op.equals("s")) {
				buy &= ruleSMA(history, startDay, length) < closingPrice;
			} else {
				buy &= ruleMAX(history, startDay, length) < closingPrice;
			}
		} else {
			if (op.equals("e")) {
				buy |= ruleEMA(history, startDay, length) < closingPrice;
			} else if (op.equals("s")) {
				buy |= ruleSMA(history, startDay, length) < closingPrice;
			} else {
				buy |= ruleMAX(history, startDay, length) < closingPrice;
			}
		}

		// Get the characteristics of the third rule in the set
		length = new Integer(representation.substring(11));
		startDay = day - length - 1;
		op = representation.substring(10, 11);

		if (representation.substring(9, 10).equals("&")) {
			if (op.equals("e")) {
				buy &= ruleEMA(history, startDay, length) < closingPrice;
			} else if (op.equals("s")) {
				buy &= ruleSMA(history, startDay, length) < closingPrice;
			} else {
				buy &= ruleMAX(history, startDay, length) < closingPrice;
			}
		} else {
			if (op.equals("e")) {
				buy |= ruleEMA(history, startDay, length) < closingPrice;
			} else if (op.equals("s")) {
				buy |= ruleSMA(history, startDay, length) < closingPrice;
			} else {
				buy |= ruleMAX(history, startDay, length) < closingPrice;
			}
		}

		return buy;
	};

	public static Double ruleEMA(ArrayList<Double> history, Integer current, Integer length) {
		Double alpha = 1 - (2d / (length + 1));
		Double numerator = 0d;
		Double denominator = 0d;

		for (int i = 0; i < length; ++i, ++current) {
			Double alphaPow = Math.pow(alpha, i);
			numerator += history.get(current) * alphaPow;
			denominator += alphaPow;
		}

		return numerator / denominator;
	}

	public static Double ruleSMA(ArrayList<Double> history, Integer current, Integer length) {
		Double sum = 0d;

		for (int i = 0; i < length; ++i, ++current) {
			sum += history.get(current);
		}

		return sum / length;
	}

	public static Double ruleMAX(ArrayList<Double> history, Integer current, Integer length) {
		Double max = history.get(current++);

		for (int i = 1; i < length; ++i, ++current) {
			if (max < history.get(current)) max = history.get(current);
		}

		return max;
	}

	private Double restrictRange(Double preimage) {
		/* This function maps the domain of all possible net gains for stock
		 * market trading (-infinity, infinity) to a positive, monotonically
		 * increasing range that is usable as a fitness for the chromosome
		 * (0, infinity). */
		Double image;
		if (preimage < -1d) {
			// (-infinity, -1) -> (0, 1)
			image = -1d / preimage;
		} else if (preimage > 0d) {
			// (0, infinity) -> (1, infinity+1)
			image = preimage + 1d;
		} else {
			// [-1, 0] -> {1}
			image = 1d;
		}
		return image;
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
		return fitness.compareTo(m.fitness);
	}
}
