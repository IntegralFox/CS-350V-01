#include "node.hpp"
#include <iostream>

Node::Node() {
	CPT.reserve(1);
}

Node::Node(const std::initializer_list<std::string>& parents) {
	int offset = 1;
	for (const auto& p : parents) {
		this->parents[p] = offset;
		offset *= 2;
	}
	CPT.reserve(offset);
}

Node& Node::operator=(const Node& n) {
	parents = n.parents;
	CPT = n.CPT;
	return *this;
}

Node& Node::operator=(Node&& n) {
	parents = std::move(n.parents);
	CPT = std::move(n.CPT);
	return *this;
}

void Node::setTableValue(const double probability) {
	CPT[0] = probability;
}

void Node::setTableValue(const std::map<std::string, bool>& parentValues, const double probability) {
	int index = 0;
	for (const auto& p : parentValues) {
		try {
			if (!p.second) index += parents.at(p.first);
		} catch (std::exception& e) {
			throw new std::out_of_range {"Node has no parent called " + p.first};
		}
	}
	CPT[index] = probability;
}

double Node::getTableValue(const bool nodeValue) {
	return nodeValue ? CPT[0] : 1 - CPT[0];
}

double Node::getTableValue(const bool nodeValue, const std::map<std::string, bool>& parentValues) {
	int index = 0;
	for (const auto& p : parentValues) {
		try {
			if (!p.second) index += parents.at(p.first);
		} catch (std::exception& e) {
			throw new std::out_of_range {"Node has no parent called " + p.first};
		}
	}
	return nodeValue ? CPT[index] : 1 - CPT[index];
}
