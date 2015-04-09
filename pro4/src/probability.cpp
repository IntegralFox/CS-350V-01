/* Project: Bayesian Network for Diagnosis
 * Author: Michael Welborn
 * Date: 2014-04-05
 *
 * Main file to interact with user and create and query a BN object. */

#include <iostream>
#include <string>

#include "bn.hpp"

int main(int argc, char** argv) {
	BN bayesianNetwork {};
	std::map<std::string, bool> query, evidence;
	std::string line, variable;
	std::string clearConsole = "\33[H\33[J";

	// Prompt for the query variable
	do {
		std::cout << clearConsole << "Enter a query variable [pollution,smoker,cancer,xray,dysponea]: " << std::flush;
		std::cin >> line;
	} while (
		line != "pollution" &&
		line != "smoker" &&
		line != "cancer" &&
		line != "xray" &&
		line != "dysponea");
	variable = line;

	do {
		std::cout << std::endl << "Enter a value for " << variable << " [t,f]: " << std::flush;
		std::cin >> line;
	} while (line != "t" && line != "f");

	query[variable] = (line == "t") ? true : false;

	// Prompt for the evidences
	while (true) {
		do {
			std::cout << clearConsole << "Enter an evidence variable [pollution,smoker,cancer,xray,dysponea]\nor ctrl+d if done: " << std::flush;
			std::cin >> line;
		} while (
			line != "pollution" &&
			line != "smoker" &&
			line != "cancer" &&
			line != "xray" &&
			line != "dysponea" &&
			std::cin);
		variable = line;

		if (!std::cin) break;

		do {
			std::cout << std::endl << "Enter a value for " << variable << " [t,f]: " << std::flush;
			std::cin >> line;
		} while (line != "t" && line != "f");

		evidence[variable] = (line == "t") ? true : false;
	}

	std::cout << clearConsole << "The probability of ";
	for (const auto& variable : query) std::cout << variable.first << "=" << (variable.second ? "true" : "false");
	std::cout << " given" << std::endl;
	for (const auto& variable : evidence) std::cout << variable.first << "=" << (variable.second ? "true" : "false") << std::endl;
	std::cout << "is " << bayesianNetwork.conditionalProbability(query, evidence) << std::endl;
}
