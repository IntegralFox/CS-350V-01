#include "bn.hpp"

BN::BN() {
	Node pollution {};
	pollution.setTableValue(0.1);
	network["pollution"] = std::move(pollution);

	Node smoker {};
	smoker.setTableValue(0.3);
	network["smoker"] = std::move(smoker);

	Node cancer {"pollution", "smoker"};
	std::map<std::string, bool> condition {
		{"pollution", true},
		{"smoker", true}
	};
	cancer.setTableValue(condition, 0.95);
	condition["pollution"] = false;
	cancer.setTableValue(condition, 0.3);
	condition["cancer"] = false;
	cancer.setTableValue(condition, 0.001);
	condition["pollution"] = true;
	cancer.setTableValue(condition, 0.2);
	network["cancer"] = std::move(cancer);

	Node xray {"cancer"};
	Node dysponea {"cancer"};
	condition = std::move(std::map<std::string, bool> {{"cancer", true}});
	xray.setTableValue(condition, 0.9);
	dysponea.setTableValue(condition, 0.65);
	condition["cancer"] = false;
	xray.setTableValue(condition, 0.2);
	xray.setTableValue(condition, 0.30);
}

double BN::probability(const std::map<std::string, bool>& variables) {
	return 1;
}

double BN::conditionalProbability(const std::map<std::string, bool>& query, const std::map<std::string, bool>& evidence) {
	return 0;
}
