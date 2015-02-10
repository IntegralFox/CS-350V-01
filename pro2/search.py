# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to
# http://inst.eecs.berkeley.edu/~cs188/pacman/pacman.html
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return [s, s, w, s, w, w, s, w]


def depthFirstSearch(problem):
    # Containers to hold closed states, open states, and paths to those states
    closedStates = []
    openStates = util.Stack()
    openPaths = util.Stack()

    # Add the root node to the open states set and the way to get there to the
    # open paths set
    openStates.push(problem.getStartState())
    openPaths.push([])

    # Depth first search algorithm
    while not openStates.isEmpty():
        # Get the next state
        currentState = openStates.pop()
        currentPath = openPaths.pop()

        # Add the state to the set of closed states
        closedStates.append(currentState)

        if problem.isGoalState(currentState):
            # If the state is the goal state, return the path to it
            return currentPath
        else:
            # Otherwise, expand its children
            children = problem.getSuccessors(currentState)
            for child in children:
                childState, childAction, childCost = child

                # If a child is already in the open states or closed states
                # sets, do not add it to the open states
                if childState in closedStates or childState in openStates.list:
                    continue

                # The path to the child is the path to the parent plus the
                # action to get to the child from the parent
                childPath = currentPath[:]
                childPath.append(childAction)

                # Add the child state and its path to their respective sets
                openStates.push(childState)
                openPaths.push(childPath)


def breadthFirstSearch(problem):
    # Containers to hold closed states, open states, and paths to those states
    closedStates = []
    openStates = util.Queue()
    openPaths = util.Queue()

    # Add the root node to the open states set and the way to get there to the
    # open paths set
    openStates.push(problem.getStartState())
    openPaths.push([])

    # Breadth first search algorithm
    while not openStates.isEmpty():
        # Get the next state
        currentState = openStates.pop()
        currentPath = openPaths.pop()

        # Add the state to the set of closed states
        closedStates.append(currentState)

        if problem.isGoalState(currentState):
            # If the state is the goal state, return the path to it
            return currentPath
        else:
            # Otherwise, expand its children
            children = problem.getSuccessors(currentState)
            for child in children:
                childState, childAction, childCost = child

                # If a child is already in the open states or closed states
                # sets, do not add it to the open states
                if childState in closedStates or childState in openStates.list:
                    continue

                # The path to the child is the path to the parent plus the
                # action to get to the child from the parent
                childPath = currentPath[:]
                childPath.append(childAction)

                # Add the child state and its path to their respective sets
                openStates.push(childState)
                openPaths.push(childPath)


def uniformCostSearch(problem):
    # Containers to hold closed states and open states
    closedStates = []
    openStates = util.PriorityQueue()

    # Add the root node to the open states set with cost 0
    openStates.push([problem.getStartState(), [], 0], 0)

    # Uniform Cost Traversal
    while not openStates.isEmpty():
        # Get the next state
        currentState, currentPath, currentCost = openStates.pop()
        closedStates.append(currentState)

        if problem.isGoalState(currentState):
            # If the state is the goal state, return the path to it
            return currentPath
        else:
            # Otherwise, expand its children
            children = problem.getSuccessors(currentState)
            for child in children:
                childState, childAction, childCost = child

                # If a child is already in the open states or closed states
                # sets, do not add it to the open states
                if childState in closedStates:
                    continue

                # Update the current cost and path with the child's cost and
                # action to create the child's path and cost
                childPath = currentPath[:]
                childPath.append(childAction)
                childCost += currentCost

                # Add the child state and its path to the priority queue
                openStates.push([childState, childPath, childCost], childCost)


def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the
    nearest goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0


def aStarSearch(problem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
