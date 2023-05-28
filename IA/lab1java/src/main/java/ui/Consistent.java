package ui;

import java.util.ArrayList;
import java.util.HashMap;

import graph.Graph;
import graph.Node;
import utils.Parser;

/**
 * 
 * This class is used to check if a heuristic is consistent or not.
 * It contains a simple function, "consistent", which will print the results. 
 * @author chloe
 *
 */
public class Consistent {

	/**
	 * Check if a given graph with a given file of heuristics if it is consistent or not.
	 * Print the results.
	 * @param graph Graph to be checked. 
	 */
	public static void consistent(Graph graph)  {
		
		
		// By default we consider the heuristic to be consistent
		// will be set to false if it is not the case
		boolean isConsistent = true;
		
		// Display of the function
		String disp = "";
		
		// For every possible node of the graph
		for (Node current : graph.getStates()) {
			
			// All child nodes
			for (int i = 0; i < current.getTransitions().size(); i++) {
				
				// Child node
				Node child = current.getTransitions().get(i).getDestination();
				
				// Is consistent
				if ( current.getHeuristic() <= child.getHeuristic() + current.getTransitions().get(i).getWeight())
					// Display
					disp += "[CONDITION]: [OK] h("+current.getName()+") <= h("+child.getName()+") + c: "+current.getHeuristic()+" <= "+child.getHeuristic()+" + "+current.getTransitions().get(i).getWeight()+"\n";
				else {
					// Is not consistent
					isConsistent = false;
					// Display
					disp += "[CONDITION]: [ERR] h("+current.getName()+") <= h("+child.getName()+") + c: "+current.getHeuristic()+" <= "+child.getHeuristic()+" + "+current.getTransitions().get(i).getWeight()+"\n";
				}	
			}
		}
		
		// Add the result (NB: if it is consistent or not)
		if (isConsistent)
			disp +="[CONCLUSION]: Heuristic is consistent.";
		else
			disp +="[CONCLUSION]: Heuristic is not consistent.";
		
		// Display the results
		System.out.println(disp);
		
	}
}
