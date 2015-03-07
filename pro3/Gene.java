/* Class for a single individual in the population */

import java.util.Vector;

class Chromosome {
	/* Member variables */
	public Double fitness;
	public Double netGain;
	public String representation;

	/* Member functions */
	public Chromosome(String r) {
		representation = r;
		fitness = Double.NaN;
		netGain = Double.NaN;
	}

	public void calculateFitness(Vector<Double> history) {

	}
}
