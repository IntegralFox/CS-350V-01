/* Main wrapper to handle file inputs and status output */

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

class StockAlgorithm {
	public static void main(String[] args) {
		/* Error if no csv file was provided */
		if (args.length == 0) {
			System.out.println("StockAlgorithm: Usage: java StockAlgorithm <stock_csv_file>");
			return;
		}

		/* Create a BufferedReader with the file */
		try {
			BufferedReader stockHistoryCSV = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			System.out.print("StockAlgorithm: ");
			System.out.println(e.getMessage());
			return;
		}
	}
}
