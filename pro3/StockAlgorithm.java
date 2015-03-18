/* Main wrapper to handle file inputs and status output */

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

class StockAlgorithm {
	public static void main(String[] args) throws IOException {
		/* Error if no csv file was provided */
		if (args.length == 0) {
			System.out.println("StockAlgorithm: Usage: java StockAlgorithm <stock_csv_file>");
			return;
		}

		/* Create a BufferedReader with the file */
		BufferedReader stockHistoryCSV;
		try {
			stockHistoryCSV = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			System.out.print("StockAlgorithm: ");
			System.out.println(e.getMessage());
			return;
		}

		/* Read in the CSV into a 2D array of doubles */
		ArrayList<ArrayList<Double>> stockHistory = new ArrayList<ArrayList<Double>>();
		for (Integer i = 0; i < Chromosome.NUM_STOCKS; ++i) stockHistory.add(new ArrayList<Double>());
		String line;
		while ((line = stockHistoryCSV.readLine()) != null) {
			String[] values = line.split(",");
			for (int i = 0; i < Chromosome.NUM_STOCKS; ++i) {
				stockHistory.get(i).add(new Double(values[i]));
			}
		}

		/* Create a Genetic Algorithm object and evolve an answer */
		GA solver = new GA(stockHistory);
		solver.evolve();

		/* Output the solution */
		Chromosome solution = solver.fittest();

		// ANSI control sequence interrupt to clear the terminal and reset the cursor position
		// http://en.wikipedia.org/wiki/ANSI_escape_code
		System.out.print("\33[H\33[J");
		System.out.print("Evolving Population [####################] 100%");
		System.out.println("\n\n\nBest rule found: " + solution.getRepresentation());
		System.out.println("Net gain: " + String.format("$%.2f", solution.getNetGain()));
	}
}
