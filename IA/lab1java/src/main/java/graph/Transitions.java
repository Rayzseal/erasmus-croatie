package graph;

import java.util.Comparator;

public class Transitions {
	
	private float weight;
	private Node destination;

	public Transitions(Node dest, float w) {
		this.destination = dest;
		this.weight = w;
	}

	/**
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @return the destination
	 */
	public Node getDestination() {
		return destination;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Node destination) {
		this.destination = destination;
	}
	
	
};
