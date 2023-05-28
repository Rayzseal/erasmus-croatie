package structure;

import java.util.ArrayList;

/**
 * 
 * Class state, creates a state defined by an name and its transitions to other states. 
 * @author chloe
 *
 */
public class State {

	private String name;
	private ArrayList<Transitions> transitions;
	
	/**
	 * Constructore of a state, creates a state with a name and an empty list of transitions.
	 * @param name Name of state
	 */
	public State(String name) {
		this.name = name;
		this.transitions = new ArrayList<Transitions>();
	}
	
	/**
	 * Adding a transition to the list of transitions of a state.
	 * @param symbol Symbol read in the string to be accepted.
	 * @param stackRead Stack read.
	 * @param stateReached State reached in the transition.
	 * @param stackReached Stack reached in the transition.
	 */
	public void addTransition(Symbol symbol, StackSymbols stackRead, State stateReached, ArrayList<StackSymbols> stackReached) {
		Transitions transi = new Transitions(symbol,stackRead,stateReached);
		transi.setReachedStack(stackReached);
		this.transitions.add(transi);
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
	public ArrayList<Transitions> getTransitions() {
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
	public void setTransitions(ArrayList<Transitions> transitions) {
		this.transitions = transitions;
	}

	@Override
	public String toString() {
		return "State [name=" + name + ", transitions=" + transitions + "]";
	}
}
