#include "node.hpp"

Node::Node(const std::string& name) : name {name} {
	CPT.reserve(1);
}

Node::Node(const std::string& name, const std::initializer_list<std::string>& parents) : name {name} {
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
	int index = 0;
	for (const auto& parent : parents) {
		const auto& parentName   = parent.first;
		const auto& parentOffset = parent.second;
		try {
			if (!nodeValues.at(parentName)) index += parentOffset;
		} catch (std::exception& e) {
			throw new std::out_of_range {"Node has has parent " + parentName + " not defined in provided map."};
		}
	}
	CPT[index] = probability;
}

double Node::getTableValue(const std::map<std::string, bool>& nodeValues) const {
	int index = 0;
	for (const auto& parent : parents) {
		const auto& parentName   = parent.first;
		const auto& parentOffset = parent.second;

		try {
			if (!nodeValues.at(parentName)) index += parentOffset;
		} catch (std::exception& e) {
			throw new std::out_of_range {"Node has has parent " + parentName + " not defined in provided map."};
		}
	}
	return nodeValues.at(name) ? CPT[index] : 1 - CPT[index];
}
