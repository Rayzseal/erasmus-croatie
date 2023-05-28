package ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.Graph;
import graph.Node;
import graph.Transitions;

/**
 * 
 * This class is used to compute the algorithm AStar used to find the shortest path from 
 * an initial node to one (there could be multiple) goal states. 
 * @author chloe
 *
 * Source used 
 * Basically nothing more than the slides since Astar is almost the same as UCS, 
 * it just add the heuristic value. 
 *
 */
public class AStar {

	public static Node targetReached;

	/**
	 * Initialize the initial node with the values contained in the graph.
	 * 
	 * @param graph graph to be used.
	 */
	static void initialize(Graph graph) {
		for (int i = 0; i < graph.getStates().size(); i++) {
			if (graph.getStates().get(i).getName().equals(graph.getInitialState().getName())) {
				graph.getInitialState().setTransitions(graph.getStates().get(i).getTransitions());
				graph.getInitialState().setHeuristic(graph.getStates().get(i).getHeuristic());
			}
		}
	}

	/**
	 * Compute the Astar algorithm.
	 * 
	 * @param graph Graph with an initial node and possibly multiple ending node(s). 
	 */
	static void ASTAR(Graph graph, boolean display) {

		// Initialize the initial node
		initialize(graph);
		
		// All the node have an infinite cost value
		graph.getStates().forEach(node -> node.setCost(Integer.MAX_VALUE));

		// The initial node has a value equal to 0 
		graph.getInitialState().setCost(0);

		// Empty sorted set of nodes
		ArrayList<Node> sortedset = new ArrayList<Node>();

		// Adding the initial node to the sorted set
		sortedset.add(graph.getInitialState());
		
		// Creating an empty set of explored nodes
		Set<Node> explored = new HashSet<Node>();
		
		// Will be equal to false if no solution has been found for the given graph in parameter
		boolean found = false;

		do {
			
			Collections.sort(sortedset);

			// Get the first node
			Node currend = sortedset.get(0);
			
			// Removes the current node
			sortedset.remove(currend);
			
			// Check if the current node is in the list of goal states
			for (int i = 0; i < graph.getGoalStates().size(); i++) {
				// Node in the goal state
				if (currend.getName().equals(graph.getGoalStates().get(i).getName())) {
					// Used for recreating the path
					targetReached = currend;
					// Terminates the algorithm
					found = true;
					// Display the result
					if (display)
						System.out.println(print(found,explored.size())); 
					break;
				}
			}
			
			// Adding the node to the list of explored nodes
			explored.add(currend);

			// For all neighbors of the current node
			for (Transitions adj : currend.getTransitions()) {
				
				// One of the child node(s) of the current node
				Node child = adj.getDestination();

				// If the child node isn't contained in the queue or the explored list
				if (!sortedset.contains(child) && !explored.contains(child)) {
					// The new cost of the node is set
					child.setCost(currend.getCost() + adj.getWeight(), child.getHeuristic());
					// Parent node is set
					child.setParent(currend);
					// Adding the child node to the queue
					sortedset.add(child);
				} // If the node is already contained in the queue but the new cost of the node is superior than the last cost
				else if ((sortedset.contains(child)) && (child.getCost() > (currend.getCost()+ adj.getWeight()))) {
					// Actualize set
					child.setCost(currend.getCost() + adj.getWeight(), child.getHeuristic());
					// We add the node the to queue
					sortedset.add(child);
				}
			}
		} while (sortedset.size() != 0 && !found);
	}

	/**
	 * Display the result.
	 */
	public static String print(boolean found, int cpt) {
		
		String disp = "";
		if (found)
			disp +="[FOUND_SOLUTION]: yes\n";
		else
			disp +="[FOUND_SOLUTION]: no\n";
		
		
		// Retrace the path
		List<Node> path = new ArrayList<Node>();
		for (Node node = targetReached; node != null; node = node.getParent()) {
			path.add(node);
		}
		
		disp +="[STATES_VISITED]: "+cpt+"\n";
		disp +="[PATH_LENGTH]: "+path.size()+"\n";
		disp +="[TOTAL_COST]: "+targetReached.getCost()+"\n";
		disp +="[PATH]: ";
		
		// Path in correct order
		Collections.reverse(path);
		for (int i = 0; i < path.size(); i++) {
			if (i != path.size() - 1)
				disp +=path.get(i).getName()+" => ";
			else
				disp +=path.get(i).getName();
		}
		
		return disp;
	}
}
