'''
Description: Breadth First Search to solve the knapsack problem.
Author: Michael Welborn
Date: 2015/2/1
'''

# Include the utilities in nodeManagement.py and the queue data structure deque
from nodeManagement import *
from collections import deque
import sys


def breadthFirstSearch(root, maxWeight):
	best = root
	open = deque([root])

	while len(open):
		current = open.popleft()

		# Discard and skip an item combination if it exceeds the weight limit
		if current.weight > maxWeight:
			continue

		# Set this node as best if it has a greater value than the current best
		if current.value > best.value:
			best = current

		# Add children of the current node to the open set
		open.extend(genChildrenFor(current))

		# Current node is not placed in a closed set and is instead
		# garbage collected for space efficiency

	return best


# The filename is the last argument in the argument list
argc = len(sys.argv)
filename = sys.argv[argc - 1]

file = open(filename)  # Open the file for parsing
maxWeight = int(file.readline())  # The first line is the max length

# loop through the rest of the lines and parse the items
items = []
for line in file:
	# Split on the first two spaces, separating the weight, value, and
	# description
	lineItem = line.strip().split(None, 2)

	weight = int(lineItem[0])
	value = int(lineItem[1])
	description = lineItem[2]

	items.append(Item(weight, value, description))

# Create a root node with value 0, weight 0, composed of no items
root = Node(0, 0, [], items)

solution = breadthFirstSearch(root, maxWeight)

printSolution(solution)
