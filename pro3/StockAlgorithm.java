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

		/* Read in the CSV into an array of doubles */
		ArrayList<Double> stockHistory = new ArrayList<Double>();
		String line;
		while ((line = stockHistoryCSV.readLine()) != null) {
			String[] values = line.split(",");
			for (String v : values) {
				stockHistory.add(new Double(v));
			}
		}

		/* Create a Genetic Algorithm object and evolve an answer */
		GA solver = new GA(stockHistory);
		solver.evolve();

		/* Output the solution */
		Chromosome solution = solver.fittest();
		System.out.println("Best rule found: " + solution.getRepresentation());
		System.out.println("Net gain: $" + solution.getNetGain());
	}
}
