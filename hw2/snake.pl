% Snake Classification
% Michael Welborn
% 2015/01/24

% Facts will be added as the program runs via the predicate known/1
:- dynamic(known/1).

whatsnakeis(X,Y) :-
	snake(X,Y);
	not(known(has_rattle(X,Y))),     ask_rattle(X,Z), snake(X,Y);
	not(known(size(X,Y))),           ask_size(X,U),   snake(X,Y);
	not(known(dorsal_pattern(X,Y))), ask_dorsal(X,W), snake(X,Y).

% Define attributes of each snake
snake(X,pygmyRattler):-
       has_rattle(X,yes),
       size(X,small),
       dorsal_pattern(X,none).
snake(X, timberRattler):-
	has_rattle(X, yes),
	size(X, large),
	dorsal_pattern(X, banded).
snake(X, diamondBackRattler):-
	has_rattle(X, yes),
	size(X, large),
	dorsal_pattern(X, diamonds).
snake(X, easternMilk):-
	has_rattle(X, no),
	size(X, medium),
	dorsal_pattern(X, blotched).
snake(X, brownWater):-
	has_rattle(X, no),
	size(X, large),
	dorsal_pattern(X, banded).
snake(X, rainbow):-
	has_rattle(X, no),
	size(X, large),
	dorsal_pattern(X, striped).

% Functions truth values determined by the knowledge in the known/1 predicate
has_rattle(X,Y) :- known(has_rattle(X,Y)).
size(X,Y) :- known(size(X,Y)).
dorsal_pattern(X,Y) :- known(dorsal_pattern(X,Y)).

% These rules will be expanded upon as the program runs
known(has_rattle(bob,yes)).
known(size(bob,large)).
known(dorsal_pattern(bob,diamonds)).
known(has_rattle(chris,yes)).
known(size(kim,small)).
known(dorsal_pattern(ken,stripped)).

% Adds facts to the dynamic knowledgebase via user input
ask_rattle(X,Y):-
       write('does '),
       write(X),
       write(' have a rattle (yes or no)?'),
       nl, nl,
       read(Y),
       asserta(known(has_rattle(X,Y))).
ask_size(X,Y):-
       write('what size is '),
       write(X),
       write('(large, small, medium)?'),
       nl, nl,
       read(Y),
       asserta(known(size(X,Y))).
ask_dorsal(X,Y):-
       write('what dorsal patern does '),
       write(X),
       write(' have ?'),
       nl, nl,
       read(Y),
       asserta(known(dorsal_pattern(X,Y))).












