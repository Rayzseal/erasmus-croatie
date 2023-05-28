package structure;

import java.util.ArrayList;
import java.util.List;

public class State {
	
	private String name;
	private List<Transitions> transitions;
	
	/**
	 * @param name
	 */
	public State(String name) {
		this.name = name;
		this.transitions = new ArrayList<Transitions>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * @param transitions the transitions to set
	 */
	public void setTransitions(List<Transitions> transitions) {
		this.transitions = transitions;
	}
	
	

}
