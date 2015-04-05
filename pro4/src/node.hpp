/* Header file for node objects to be used by the BN object */

#ifndef PROB_NODE
#define PROB_NODE

#include <map>
#include <string>
#include <set>
#include <vector>

class Node {
	// Parents are stored as a map of parent names and offsets into the CPT
	// that represents the number of entries before the parent becomes false.
	std::map<std::string, int> parents;
	// The CPT table stores probabilities as a traditional truth table.
	// As such, we can exploit its regular nature to not have to store the
	// parent values.
	std::vector<double> CPT;

public:
	// Constructs a new node given a set of parent identifiers
	Node(const std::set<std::string>&);
	// Set the value of a conditional probability
	void setTableValue(const std::map<std::string, bool>&, const double);
	// Get the value of a conditional probability
	double getTableValue(const bool, const std::map<std::string, bool>&);
};

#endif
