package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * Class graph, creates a graph with all contained nodes, but also all the goal states 
 * and the initial state.
 * 
 * @author chloe
 *
 */
public class Graph {
	
	private List<Node> states;
	
	private Node initialState;
	private List<Node> goalStates;
	
	/**
	 * By default, we will create an empty graph. 
	 * We only have to create empty list to store the states that will be read by the parser shortly after.
	 */
	public Graph() {
		//Creating empty lists
		this.states = new ArrayList<Node>();
		this.goalStates = new ArrayList<Node>();
	}

	/**
	 * @return the states
	 */
	public List<Node> getStates() {
		return states;
	}

	/**
	 * @return the initialState
	 */
	public Node getInitialState() {
		return initialState;
	}

	/**
	 * @return the goalStates
	 */
	public List<Node> getGoalStates() {
		return goalStates;
	}

	/**
	 * @param states the states to set
	 */
	public void setStates(List<Node> states) {
		this.states = states;
	}

	/**
	 * @param initialState the initialState to set
	 */
	public void setInitialState(Node initialState) {
		this.initialState = initialState;
	}

	/**
	 * @param goalStates the goalStates to set
	 */
	public void setGoalStates(List<Node> goalStates) {
		this.goalStates = goalStates;
	}
	
	/**
	 * Test if a given string respects the expected format for a node name. 
	 * @param line Name to test
	 * @return The name if it is correcyly formated
	 * @throws Exception Throw an excpetion otherwise
	 */
	public String readCity(String line) throws Exception {
		
		boolean match = Pattern.matches("^[A-Za-z0-9_]", line);
			
		if (match) 
			return line;
		else
			throw new IllegalArgumentException("Incorrect name format found in file");
		
	}
	
	/**
	 * Adding a new node.
	 * @param name Name of node. 
	 */
	public void addNewNode(String name) {
		Node n = new Node(name);
		this.getStates().add(n);
	}
	
	/**
	 * Adding a new node.
	 * @param name Name of node. 
	 * @param heur Heuristic value of node. 
	 */
	public void addNewNodeHeur(String name, float heur) {
		Node n = new Node(name,heur);
		this.getStates().add(n);
	}
	
	public Node checkIfExistByName(String name) {
		for (int i = 0; i < this.states.size(); i++) 
			if (this.states.get(i).getName().equalsIgnoreCase(name)) 
				return this.states.get(i);
		return null;	
	}
	
	/**
	 * Checks if a city exists with a given name, or creates it if it does not exist. 
	 * @param name Name we are looking for. 
	 * @return The node if the city exists, the node just created otherwise. 
	 */
	public Node cityExists(String name) {
		
		//If the list is empty, we won't find a node, so we already add the node
		if (this.states.size() == 0)
			addNewNode(name);
		
		for (int i = 0; i < this.states.size(); i++) {
			if (this.states.get(i).getName().equalsIgnoreCase(name)) {
				return this.states.get(i);
			}
			else {

				addNewNode(name);
				int val = this.getStates().size() - 1;
				return this.getStates().get(val);
			}
		}
		return null;
	}
}
