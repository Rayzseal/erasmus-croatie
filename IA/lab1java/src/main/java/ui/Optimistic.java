package ui;

import java.util.HashMap;

import graph.Graph;
import graph.Node;
import utils.Parser;

/**
 * 
 * This class is used to check if a heuristic is optimistic or not.
 * It contains a simple function, "optimistic", which will print the results. 
 * @author chloe
 *
 */
public class Optimistic {
	
	/**
	 * Check if a given graph with a given file of heuristics if it is optimistic or not.
	 * Print the results.
	 * @param graph Graph to be checked. 
	 */
	public static void optimistic(Graph graph)  {
		
		// Used to store all the results
		HashMap<Node,Float> explored = new HashMap<Node,Float>();
		
		// By default we consider the heuristic to be optimistic
		// will be set to false if it is not the case
		boolean isOptimistic = true;
		
		// Display of the function
		String disp = "";
		
		// We start from the goal nodes to get to all nodes of the graph
		for (Node current : graph.getGoalStates()) {

			// For every possible node of the graph
			for (Node graphToExplore : graph.getStates()) {
				
				// Creates an empty graph 
				Graph tmp = new Graph();
				// Get all the possible states of the graph
				tmp.setStates(graph.getStates());
				// Set the initial state to one of the goal states of the original graph
				tmp.setInitialState(current);
				// Set the goal states to one of the nodes of the graph
				tmp.getGoalStates().add(graphToExplore);
				
				// To get the result value for each state, we use AStar to compute the result
				AStar.ASTAR(tmp,false);
				float res = tmp.getGoalStates().get(0).getCost(); 
				
				// The node was never explored before
				if (!explored.containsKey(tmp.getGoalStates().get(0))) {
					// Add the node to the explored nodes
					explored.put(tmp.getGoalStates().get(0), res);
					// The condition is not okay, we add the error message
					if (tmp.getGoalStates().get(0).getHeuristic() > res) {
						disp += "[CONDITION]: [ERR] h("+tmp.getGoalStates().get(0).getName()+") <= h*: "+tmp.getGoalStates().get(0).getHeuristic()+" <= "+res+"\n";
						isOptimistic = false;
					}
					else // Condition is okay
						disp += "[CONDITION]: [OK] h("+tmp.getGoalStates().get(0).getName()+") <= h*: "+tmp.getGoalStates().get(0).getHeuristic()+" <= "+res+"\n";
				}
				// If a node is already contained in the graph, and the new value inferior to the previous one stored
				else if (res < explored.get(tmp.getGoalStates().get(0))) {
					// Add the node to the explored nodes
					explored.put(tmp.getGoalStates().get(0), res);
					// The condition is not okay, we add the error message
					isOptimistic = false;
					disp += "[CONDITION]: [ERR] h("+tmp.getGoalStates().get(0).getName()+") <= h*: "+tmp.getGoalStates().get(0).getHeuristic()+" <= "+res+"\n";
				} 
				// The node was never explored before
				//TODO check if it's used
				/**
				else {
					disp += "[CONDITION]: [OK] h("+tmp.getGoalStates().get(0).getName()+") <= h*: "+tmp.getGoalStates().get(0).getHeuristic()+" <= "+res+"\n";
				}
				**/
				
			}
			
		}
		
		// Add the result (NB: if it is optimistic or not)
		if (isOptimistic)
			disp +="[CONCLUSION]: Heuristic is optimistic.";
		else
			disp +="[CONCLUSION]: Heuristic is not optimistic.";
		
		// Display the results
		System.out.println(disp);
		
	}
}
