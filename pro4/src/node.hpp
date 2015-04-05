/* Header file for node objects to be used by the BN object */

#ifndef PROB_NODE
#define PROB_NODE

#include <map>
#include <string>
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
	// Constructs a node with no parents
	Node();

	// Constructs a new node given a set of parent identifiers
	Node(const std::initializer_list<std::string>&);

	// Copy and Move constructor
	Node(const Node& n) : parents(n.parents), CPT(n.CPT) {};
	Node(Node&& n) : parents(std::move(n.parents)), CPT(std::move(n.CPT)) {};

	// Copy and Move assignment operators
	Node& operator=(const Node&);
	Node& operator=(Node&&);

	// Set the value of a conditional probability
	void setTableValue(const double);
	void setTableValue(const std::map<std::string, bool>&, const double);

	// Get the value of a conditional probability
	double getTableValue(const bool);
	double getTableValue(const bool, const std::map<std::string, bool>&);
};

#endif