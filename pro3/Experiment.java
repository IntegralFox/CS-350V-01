/* Expriment Evaluations */

import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

class Experiment {
	static final Double INITIAL_ACCOUNT_BALANCE = 100000d;
	static final Integer EVALUATION_PERIOD = 60;
	static PrintWriter resultFile;
	static StringBuilder chromosome;
	static ArrayList<ArrayList<Double>> stockHistory;

	public static void main(String[] args) throws IOException {
		chromosome = new StringBuilder();
		String csvFilePath = "";
		String resultFilePath = "";

		// Validate that there are enough args
		if (args.length < 6) {
			System.out.println("Experiment: Usage: java Experiment [options]");
			System.out.println("\t-c <chromosome>\t\tChromosome representation");
			System.out.println("\t-i <inputCSV>\t\tInput CSV Stocks");
			System.out.println("\t-o <outputCSV>\t\tOutput results CSV");
			return;
		}

		// Parse arugments
		for (int i = 0; i < args.length; i += 2) {
			if (args[i].equals("-c")) chromosome = new StringBuilder(args[i+1]);
			if (args[i].equals("-o")) resultFilePath = args[i+1];
			if (args[i].equals("-i")) csvFilePath = args[i+1];
		}

		// Open input csv
		BufferedReader csvFile;
		try {
			csvFile = new BufferedReader(new FileReader(csvFilePath));
		} catch (FileNotFoundException e) {
			System.out.println("Experiment: " + e.getMessage());
			return;
		}

		// Open output file
		resultFile = new PrintWriter(resultFilePath);

		// Parse headers
		String line = csvFile.readLine();
		String[] columnHeader = line.split(",");
		resultFile.write("Chromosome,");
		for (String header : columnHeader) resultFile.write(" " + header + ",");
		resultFile.write(" Mean,\n");

		// Parse Stocks
		stockHistory = new ArrayList<ArrayList<Double>>();
		for (Integer i = 0; i < columnHeader.length; ++i) stockHistory.add(new ArrayList<Double>());
		while ((line = csvFile.readLine()) != null) {
			String[] values = line.split(",");
			for (int i = 0; i < columnHeader.length; ++i) {
				stockHistory.get(i).add(new Double(values[i]));
			}
		}

		experiment1();

		resultFile.flush();
	}

	static void experiment1() {
		Double mean = 0d;
		// Evaluate Chromosome
		resultFile.write(chromosome + ",");
		for (ArrayList<Double> history : stockHistory) {
			Double result = calculateNetGain(new StringBuilder(chromosome), history);
			mean += result;
			resultFile.write(" " + String.format("$%.2f", result) + ",");
		}
		mean /= stockHistory.size();
		resultFile.write(" " + String.format("$%.2f", mean) + ",\n");

		// Evaluate each rule in the chromosome on its own
		for (int i = 0; i < 3; ++i) {
			mean = 0d;
			String component = chromosome.substring(i*5, (i*5)+4);
			resultFile.write(component + ",");
			for (ArrayList<Double> history : stockHistory) {
				Double result = calculateNetGain(
					new StringBuilder(component+"|"+component+"|"+component),
					history
				);
				mean += result;
				resultFile.write(" " + String.format("$%.2f", result) + ",");
			}
			mean /= stockHistory.size();
			resultFile.write(" " + String.format("$%.2f", mean) + ",\n");
		}
	}

	static Double calculateNetGain(StringBuilder chromosome, ArrayList<Double> history) {
		Double account = INITIAL_ACCOUNT_BALANCE;
		Integer shares = 0;

		// Loop for a specified number of days, trading shares based on the rule
		for (int day = EVALUATION_PERIOD; day >= 0; --day) {
				Double daysClosingPrice = history.get(day);
				if (day == 0) {
					// Sell all shares if it's the last day
					if (shares > 0) {
						Integer shareCount = shares;
						account += shareCount * daysClosingPrice - Chromosome.TRANSACTION_COST;
						shares -= shareCount;
					}
				} else if (Chromosome.ruleSaysBuy(chromosome, history, day)) {
					Double buyBudget = account * 0.25d; // Buy quarter
					Integer shareCount = new Double(buyBudget / daysClosingPrice).intValue();
					shares += shareCount;
					account -= shareCount * daysClosingPrice + Chromosome.TRANSACTION_COST;
				} else {
					Integer shareCount = shares / 4; // Sell quarter
					if (shareCount < 5) shareCount = shares; // unless we have only 10 shares, then sell all
					account += shareCount * daysClosingPrice - Chromosome.TRANSACTION_COST;
					shares -= shareCount;
				}
		}

		return account - INITIAL_ACCOUNT_BALANCE;
	}
}
