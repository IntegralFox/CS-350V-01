#include "bn.hpp"

BN::BN() {
	Node pollution {"pollution"};
	pollution.setTableValue(0.1);
	network["pollution"] = std::move(pollution);

	Node smoker {"smoker"};
	smoker.setTableValue(0.3);
	network["smoker"] = std::move(smoker);

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
	bool coversNetwork = true;
	std::string hiddenVariable;

	// Find a hidden variable to sum over (if on exists)
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
		// Base case where all hidden variables have been given a value
		double p = 1;
		for (const auto& n : network) {
			const auto& node = n.second;
			p *= node.getTableValue(nodeValues);
		}
		return p;
	} else {
		// Recursive case where a hidden variable is assigned and results are summed
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
	std::map<std::string, bool> queryAndEvidence;

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

	return probability(queryAndEvidence) / probability(evidence);
}
