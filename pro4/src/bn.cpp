#include "bn.hpp"

BN::BN() {
	// Create a pollution node
	Node pollution {"pollution"};
	pollution.setTableValue(0.1);
	network["pollution"] = std::move(pollution);

	// Create a smoker node
	Node smoker {"smoker"};
	smoker.setTableValue(0.3);
	network["smoker"] = std::move(smoker);

	// Create a cancer node that depends on pollution and smoker
	Node cancer {"cancer", {"pollution", "smoker"}};
	std::map<std::string, bool> condition {
		{"pollution", true},
		{"smoker", true}
	};
	cancer.setTableValue(condition, 0.95);
	condition["pollution"] = false;
	cancer.setTableValue(condition, 0.3);
	condition["smoker"] = false;
	cancer.setTableValue(condition, 0.001);
	condition["pollution"] = true;
	cancer.setTableValue(condition, 0.2);
	network["cancer"] = std::move(cancer);

	// Create xray and dysponea nodes that depend on cancer
	Node xray {"xray", {"cancer"}};
	Node dysponea {"dysponea", {"cancer"}};
	condition = std::move(std::map<std::string, bool> {{"cancer", true}});
	xray.setTableValue(condition, 0.9);
	dysponea.setTableValue(condition, 0.65);
	condition["cancer"] = false;
	xray.setTableValue(condition, 0.2);
	dysponea.setTableValue(condition, 0.30);
	network["xray"] = std::move(xray);
	network["dysponea"] = std::move(dysponea);
}

double BN::probability(const std::map<std::string, bool>& nodeValues) {
	// This function performs summation by assigning truth values to hidden
	// variables using recursion. The base case is when every variable has been
	// assigned a value, as determined by the coversNetwork boolean.

	bool coversNetwork = true;
	std::string hiddenVariable;

	// Find a hidden variable to sum over with this iteration (if one exists)
	// by looking for a node in the network that has not been assigned a value
	// in the passed node values
	for (const auto& n : network) {
		const auto& nodeName = n.first;
		try {
			nodeValues.at(nodeName);
		} catch (std::exception e) {
			coversNetwork = false;
			hiddenVariable = nodeName;
		}
	}

	if (coversNetwork) {
		// Base case where all hidden variables have been given a value.
		// Compute the probability of this case by multiplying all of the
		// conditional probabilities in the network.
		double p = 1;
		for (const auto& n : network) {
			const auto& node = n.second;
			p *= node.getTableValue(nodeValues);
		}
		return p;
	} else {
		// Recursive case where a hidden variable is summed out by recursively
		// computing the probabilities given all the possible values of the node
		// and returning the sum.

		// Create copy of the passed variable values to add the hidden variable to
		std::map<std::string, bool> additionalNode {nodeValues};
		double sum = 0;

		additionalNode[hiddenVariable] = true;
		sum += probability(additionalNode);
		additionalNode[hiddenVariable] = false;
		sum += probability(additionalNode);

		return sum;
	}
}

double BN::conditionalProbability(const std::map<std::string, bool>& query, const std::map<std::string, bool>& evidence) {
	// Computes the conditional probability of a query given evidence by dividing
	// the probability of the query and evidence by the probability of the evidence
	std::map<std::string, bool> queryAndEvidence;

	// Combine the query and evidence into one container
	for (const auto& variable : query) {
		const auto& variableName = variable.first;
		const auto& variableValue = variable.second;
		queryAndEvidence[variableName] = variableValue;
	}
	for (const auto& variable : evidence) {
		const auto& variableName = variable.first;
		const auto& variableValue = variable.second;
		queryAndEvidence[variableName] = variableValue;
	}

	// Compute and return the value
	return probability(queryAndEvidence) / probability(evidence);
}
