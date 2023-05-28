package structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Symbol {
	
	private String name;
	private List<Map<State,State>> listOfStates;
	
	/**
	 * @param name
	 */
	public Symbol(String name) {
		this.name = name;
		this.listOfStates = new ArrayList<Map<State,State>>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the listOfStates
	 */
	public List<Map<State, State>> getListOfStates() {
		return listOfStates;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param listOfStates the listOfStates to set
	 */
	public void setListOfStates(List<Map<State, State>> listOfStates) {
		this.listOfStates = listOfStates;
	}
	
	public void add(State add1, State add2) {
		HashMap<State,State> tmp = new HashMap<State,State>();
		tmp.put(add1,add2);
		if (!this.getListOfStates().contains(tmp)) {
			this.getListOfStates().add(tmp);
			Transitions t = new Transitions(this,add2);
			add1.getTransitions().add(t);
		}
	}
}
