package ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import graph.Graph;
import graph.Node;
import graph.Transitions;

/**
 * 
 * This class is used to compute the algorithm BFS used to find the a path from 
 * an initial node to one (there could be multiple) goal states. 
 * @author chloe
 * 
 * Source used (pseudo code of the algorithm)
 * https://www.hackerearth.com/practice/algorithms/graphs/breadth-first-search/tutorial/
 * 
 * Understanding java queue : 
 * https://www.geeksforgeeks.org/queue-interface-java/
 * 
 * Understanding java comparator : 
 * https://www.geeksforgeeks.org/comparator-interface-java/
 *
 */
public class BFS {
	
	public static Node targetReached;
	
	/**
	 * Comparator used to sort in alphabetical order. 
	 */
	static final Comparator<Transitions> ALPHABETICAL_ORDER = new Comparator<Transitions>() {
		public int compare(Transitions t1, Transitions t2) {
			 return t1.getDestination().getName().compareToIgnoreCase(t2.getDestination().getName());
		}
	};
	
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
	 * Execute the BFS algorithm.
	 * Basically, this algorithm chooses the next node the be added to the path by sorting 
	 * alphabetically the nodes contained in the list of opened nodes. 
	 * @param graph Graph with an initial node and possibly multiple ending node(s). 
	 */
	static void BFS(Graph graph) {
		
		initialize(graph);
		
		boolean result = false;
				
		Queue<LinkedList<Node>> open = new LinkedList<LinkedList<Node>>();
		LinkedList<Node> visited = new LinkedList<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		
		// Adding initial state element
		path.add(graph.getInitialState());
		open.offer(path);
		visited.add(graph.getInitialState());
		
		Node current = null;
		
		while (open.size() != 0 && !result) {
			
			// Next node
			// Get the head of the queue and removes it from the list
			path = open.poll(); 
			// Get last element of queue
			current = path.get(path.size() - 1); 
			
			// Check if it's equal to on of the possible goal states
			for (int i = 0; i < graph.getGoalStates().size(); i++) {
				if (current.getName().equals(graph.getGoalStates().get(i).getName())) {
					// Used for recreating the path
					targetReached = current;
					// Terminates the algorithm
					result = true;
					// Display the result
					System.out.println(print(result,visited.size(),path,graph)); 
					break;
				}
			}
			
			// Sorting elements 
			Collections.sort(current.getTransitions(),ALPHABETICAL_ORDER);
			
			for (int i = 0; i < current.getTransitions().size(); i++) {
				
				boolean contained = false;
				int y = 0; 
				
				// Checks if the node has already been visited 
				while (!contained && y < visited.size()) {
					if (visited.get(y).getName().equals(current.getTransitions().get(i).getDestination().getName())) 
						contained = true;
					else 
						y++;
				}
					
				// If not, it can be added to the open list of nodes to visit, and the nodes
				// is marked as visited. 
				if (!contained) {
					List<Node> nPath = new LinkedList<Node>(path);
					nPath.add(current.getTransitions().get(i).getDestination());	
					visited.add(current.getTransitions().get(i).getDestination());
					// Adding element to the new list of neighbors to explore
					open.offer((LinkedList<Node>) nPath);
				}
			}
		}
		print(result,visited.size(),path,graph);
	}
	
	/**
	 * Display the result.
	 */
	public static String print(boolean found, int cpt, LinkedList<Node> path, Graph graph) {
		
		// To compute the cost of the path
		float cost = 0;
		
		// Compute the cost
		for (int i = 0; i < path.size() - 1; i++) {
			for (int y = 0; y < path.get(i).getTransitions().size(); y++) {
				if (path.get(i + 1).getName().equals(path.get(i).getTransitions().get(y).getDestination().getName()))
					cost += path.get(i).getTransitions().get(y).getWeight();
			}
		}
		
		// Display the result
		String disp = "";
		if (found)
			disp +="[FOUND_SOLUTION]: yes\n";
		else
			disp +="[FOUND_SOLUTION]: no\n";
		
		disp +="[STATES_VISITED]: "+cpt+"\n";
		disp +="[PATH_LENGTH]: "+path.size()+"\n";
		disp +="[TOTAL_COST]: "+cost+"\n";
		disp +="[PATH]: ";
		
		
		// Adding the path to 
		for (int i = 0; i < path.size(); i++) {
			if (i != path.size() - 1)
				disp +=path.get(i).getName()+" => ";
			else
				disp +=path.get(i).getName();
		}
		
		// Result to be displayed
		return disp;
	}
}
