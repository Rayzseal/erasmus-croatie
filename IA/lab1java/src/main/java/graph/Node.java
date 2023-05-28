package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * The class Node is used to create new nodes to add to the graph. 
 * It contains the name of the node, the heuristic value and also (if there is any) transitions for this node. 
 * 
 * @author chloe
 *
 */
public class Node implements Comparable<Node>{
	
	private String name; 
	
	private float cost;
	private float hCost;
	private Node parent;
	
	private float heuristic;
	private List<Transitions> transitions;

	/**
	 * Creating a new node, defined by a name.
	 * @param name Name of the node.
	 * @param heur Heuristic value of this node. 
	 */
	public Node(String name, float heur) {
		// Asserting name and heuristic value
		this.name = name;
		this.heuristic = heur;
		// Creating an empty list of transitions 
		this.transitions = new ArrayList<Transitions>();
	}
	
	/**
	 * Creating a new node, defined by a name.
	 * @param name Name of the node.
	 */
	public Node(String name) {
		// Asserting name value
		this.name = name;
		// Creating an empty list of transitions 
		this.transitions = new ArrayList<Transitions>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the heuristic
	 */
	public float getHeuristic() {
		return heuristic;
	}

	/**
	 * @return the transitions
	 */
	public List<Transitions> getTransitions() {
		return transitions;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param heuristic the heuristic to set
	 */
	public void setHeuristic(float heuristic) {
		this.heuristic = heuristic;
	}

	/**
	 * @param transitions the transitions to set
	 */
	public void setTransitions(List<Transitions> transitions) {
		this.transitions = transitions;
	}
	
	/**
	 * @return the cost
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(float cost) {
		this.cost = cost;
		this.sethCost(cost);
	}
	
	/**
	 * @param cost the cost to set
	 */
	public void setCost(float cost, float heur) {
		this.cost = cost;
		this.sethCost(cost + heur);
	}

	@Override
	public String toString() {
		return "Node [name=" + name + ", cost=" + cost;
	}

	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * Add a transition to the node.
	 * @param n Destination node. 
	 * @param weight Weight of the transition.
	 */
	public void addTransition(Node n, float weight) {
		Transitions t = new Transitions(n, weight);
		
		this.transitions.add(t);
	}
	
	/**
	 * @return the hCost
	 */
	public float gethCost() {
		return hCost;
	}

	/**
	 * @param hCost the hCost to set
	 */
	public void sethCost(float hCost) {
		this.hCost = hCost;
	}

	@Override
	public int compareTo(Node n2) {
		if (this.gethCost() > n2.gethCost() )
			return 1;
		else if (this.gethCost() < n2.gethCost() )
			return -1;
		else
			return this.getName().compareTo(n2.getName());
	}
}
