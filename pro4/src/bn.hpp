/* Header file for the Bayesian Network object */

#ifndef PROB_BN
#define PROB_BN

#include <map>
#include <string>

#include "node.hpp"

class BN {
	std::map<std::string, Node> network;

public:
	// Construct a Bayesian Network with the default values specified in the project description
	BN();
	// Compute the probability of a conjunction of variable values summing over the omitted values
	double probability(const std::map<std::string, bool>&);
	// Compute the conditional probability of a query and evidence sets
	double conditionalProbability(const std::map<std::string, bool>&, const std::map<std::string, bool>&);
};

#endif
