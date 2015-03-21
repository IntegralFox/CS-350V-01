import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

class ClosingPriceCSVCreator {
	public static void main(String[] args) throws IOException {
		/* Error if no csv file was provided */
		if (args.length < 3) {
			System.out.println("CSVCreator: Usage: java ClosingPriceCSVCreator <yahooCSVFile0> ... <yahooCSVFileN> <combinedCSVFile>");
			return;
		}

		/* Create a BufferedReader with the files */
		BufferedReader[] csv = new BufferedReader[args.length - 1];
		try {
			for (int i = 0; i < args.length - 1; ++i) {
				csv[i] = new BufferedReader(new FileReader(args[i]));
			}
		} catch (FileNotFoundException e) {
			System.out.println("CSVCreator: " + e.getMessage());
			return;
		}

		PrintWriter combinedCSV = new PrintWriter(args[args.length - 1]);

		// Output headings for each stock
		for (int i = 0; i < args.length - 1; ++i) combinedCSV.write(" " + args[i] + ",");
		combinedCSV.write("\n");

		// Skip first header line of every file
		for (int i = 0; i < args.length - 1; ++i) csv[i].readLine();

		/* Output closing prices into a new CSV */
		boolean endOfAFile = false;
		while (true) {
			String[] line = new String[args.length - 1];
			for (int i = 0; i < args.length - 1; ++i)
				if ((line[i] = csv[i].readLine()) == null)
					endOfAFile = true;
			if (endOfAFile) break;
			for (int i = 0; i < args.length - 1; ++i) line[i] = line[i].split(",")[4];
			for (int i = 0; i < args.length - 1; ++i) combinedCSV.write(" " + line[i] + ",");
			combinedCSV.write("\n");
		}

		// Flush the output file
		combinedCSV.flush();
	}
}
