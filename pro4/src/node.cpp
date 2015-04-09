#include "node.hpp"

Node::Node(const std::string& name) : name {name} {
	CPT.reserve(1);
}

Node::Node(const std::string& name, const std::initializer_list<std::string>& parents) : name {name} {
	// Each parent's offset is a power of two, representing how often the truth
	// value switches in the truth table.
	int offset = 1;
	for (const auto& p : parents) {
		this->parents[p] = offset;
		offset *= 2;
	}

	// The last offset*2 is also how many elements the CPT table needs
	CPT.reserve(offset);
}

Node& Node::operator=(const Node& n) {
	parents = n.parents;
	CPT = n.CPT;
	name = n.name;
	return *this;
}

Node& Node::operator=(Node&& n) {
	parents = std::move(n.parents);
	CPT = std::move(n.CPT);
	name = std::move(n.name);
	return *this;
}

void Node::setTableValue(const double probability) {
	CPT[0] = probability;
}

void Node::setTableValue(const std::map<std::string, bool>& nodeValues, const double probability) {
	// Set the probability in the truth table when the parents have specified values
	int index = 0;

	// Compute the index for the assigned values of the parents
	for (const auto& parent : parents) {
		const auto& parentName   = parent.first;
		const auto& parentOffset = parent.second;
		try {
			if (!nodeValues.at(parentName)) index += parentOffset;
		} catch (std::exception& e) {
			throw new std::out_of_range {"Node has has parent " + parentName + " not defined in provided map."};
		}
	}

	// Store the probability
	CPT[index] = probability;
}

double Node::getTableValue(const std::map<std::string, bool>& nodeValues) const {
	// Return the probability given the parents have specified values and this
	// node has a specified value
	int index = 0;

	// Compute the index
	for (const auto& parent : parents) {
		const auto& parentName   = parent.first;
		const auto& parentOffset = parent.second;

		try {
			if (!nodeValues.at(parentName)) index += parentOffset;
		} catch (std::exception& e) {
			throw new std::out_of_range {"Node has has parent " + parentName + " not defined in provided map."};
		}
	}

	// Return the probability if this variable is true or 1-probability if false
	return nodeValues.at(name) ? CPT[index] : 1 - CPT[index];
}
