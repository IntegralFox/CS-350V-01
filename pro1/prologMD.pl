% Disease Classification
% Michael Welborn
% 2015/01/29

% Facts will be added as the program runs via the predicate known/1
:- dynamic(known/1).

% Determine the disease from the symptoms and prompt if fact is not in the
% knowledge base
whatDiseaseHas(X,Y) :-
	disease(X,Y);
	not(known(fever(X,Y))),      ask_fever(X,Z),      disease(X,Y);
	not(known(headache(X,Y))),   ask_headache(X,Z),   disease(X,Y);
	not(known(pain(X,Y))),       ask_pain(X,Z),       disease(X,Y);
	not(known(fatigue(X,Y))),    ask_fatigue(X,Z),    disease(X,Y);
	not(known(exhaustion(X,Y))), ask_exhaustion(X,Z), disease(X,Y);
	not(known(congestion(X,Y))), ask_congestion(X,Z), disease(X,Y);
	not(known(sneezing(X,Y))),   ask_sneezing(X,Z),   disease(X,Y);
	not(known(soreThroat(X,Y))), ask_soreThroat(X,Z), disease(X,Y);
	not(known(cough(X,Y))),      ask_cough(X,Z),      disease(X,Y);
	not(known(wateryEyes(X,Y))), ask_wateryEyes(X,Z), disease(X,Y).

% Define diseases based on their attributes
disease(X,flu):-
	fever(X,high),
	headache(X,prominent),
	pain(X,severe),
	fatigue(X,severe),
	exhaustion(X,prominent),
	congestion(X,sometimes),
	sneezing(X,sometimes),
	soreThroat(X,sometimes),
	cough(X,severe),
	wateryEyes(X,none).
disease(X,cold):-
	fever(X,rare),
	headache(X,rare),
	pain(X,slight),
	fatigue(X,mild),
	exhaustion(X,none),
	congestion(X,common),
	sneezing(X,usual),
	soreThroat(X,common),
	cough(X,mild),
	wateryEyes(X,rare).
disease(X,allergy):-
	fever(X,never),
	headache(X,never),
	pain(X,never),
	fatigue(X,mild),
	exhaustion(X,none),
	congestion(X,common),
	sneezing(X,none),
	soreThroat(X,sometimes),
	cough(X,sometimes),
	wateryEyes(X,common).
disease(X,ebola):-
	fever(X,severe),
	headache(X,severe),
	pain(X,severe),
	fatigue(X,severe),
	exhaustion(X,severe),
	congestion(X,none),
	sneezing(X,none),
	soreThroat(X,none),
	cough(X,none),
	wateryEyes(X,none).
disease(X,bubonicPlague):-
	fever(X,high),
	headache(X,high),
	pain(X,high),
	fatigue(X,high),
	exhaustion(X,high),
	congestion(X,none),
	sneezing(X,none),
	soreThroat(X,none),
	cough(X,none),
	wateryEyes(X,none).

% Functions truth values determined by the knowledge in the known/1 predicate
fever(X,Y)      :- known(fever(X,Y)).
headache(X,Y)   :- known(headache(X,Y)).
pain(X,Y)       :- known(pain(X,Y)).
fatigue(X,Y)    :- known(fatigue(X,Y)).
exhaustion(X,Y) :- known(exhaustion(X,Y)).
congestion(X,Y) :- known(congestion(X,Y)).
sneezing(X,Y)   :- known(sneezing(X,Y)).
soreThroat(X,Y) :- known(soreThroat(X,Y)).
cough(X,Y)      :- known(cough(X,Y)).
wateryEyes(X,Y) :- known(wateryEyes(X,Y)).

% Adds facts to the dynamic knowledgebase via user input
ask_fever(X,Y):-
	write('Does '),
	write(X),
	write(' have a fever (never, rare, high, severe)?'),
	nl, nl,
	read(Y),
	asserta(known(fever(X,Y))).
ask_headache(X,Y):-
	write('Does '),
	write(X),
	write(' have a headache (never, rare, prominent, high, severe)?'),
	nl, nl,
	read(Y),
	asserta(known(headache(X,Y))).
ask_pain(X,Y):-
	write('Is '),
	write(X),
	write(' in pain (never, slight, high, severe)?'),
	nl, nl,
	read(Y),
	asserta(known(pain(X,Y))).
ask_fatigue(X,Y):-
	write('Is '),
	write(X),
	write(' fatigued (mild, high, severe)?'),
	nl, nl,
	read(Y),
	asserta(known(fatigue(X,Y))).
ask_exhaustion(X,Y):-
	write('Is '),
	write(X),
	write(' exhausted (none, prominent, high, severe)?'),
	nl, nl,
	read(Y),
	asserta(known(exhaustion(X,Y))).
ask_congestion(X,Y):-
	write('Is '),
	write(X),
	write(' congested (none, sometimes, common)?'),
	nl, nl,
	read(Y),
	asserta(known(congestion(X,Y))).
ask_sneezing(X,Y):-
	write('Is '),
	write(X),
	write(' sneezing (none, sometimes, usual)?'),
	nl, nl,
	read(Y),
	asserta(known(sneezing(X,Y))).
ask_soreThroat(X,Y):-
	write('Does '),
	write(X),
	write(' have a sore throat (none, sometimes, common)?'),
	nl, nl,
	read(Y),
	asserta(known(soreThroat(X,Y))).
ask_cough(X,Y):-
	write('Does '),
	write(X),
	write(' have a cough (none, sometimes, mild, severe)?'),
	nl, nl,
	read(Y),
	asserta(known(cough(X,Y))).
ask_wateryEyes(X,Y):-
	write('Does '),
	write(X),
	write(' have watery eyes (none, rare, common)?'),
	nl, nl,
	read(Y),
	asserta(known(wateryEyes(X,Y))).
