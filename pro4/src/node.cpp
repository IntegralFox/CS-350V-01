#include "node.hpp"

Node::Node(const std::set<std::string>& parents) {
	int offset = 1;
	for (const auto& p : parents) {
		this->parents[p] = offset;
		offset *= 2;
	}
}

void Node::setTableValue(const std::map<std::string, bool>& parentValues, const double probability) {
	int index = 0;
	for (const auto& p : parentValues) {
		if (!p.second) index += parents.at(p.first);
	}
	CPT[index] = probability;
}

double Node::getTableValue(const bool nodeValue, const std::map<std::string, bool>& parentValues) {
	int index = 0;
	for (const auto& p : parentValues) {
		if (!p.second) index += parents.at(p.first);
	}
	return nodeValue ? CPT[index] : 1 - CPT[index];
}
