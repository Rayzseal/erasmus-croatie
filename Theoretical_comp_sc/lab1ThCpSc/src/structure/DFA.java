package structure;

import java.util.ArrayList;
import java.util.List;

public class DFA {
	
	private List<State> states;
	private List<Symbol> symbols;
	private List<State> acceptingStates;
	private State initialState;
	
	/**
	 * 
	 */
	public DFA() {
		this.states = new ArrayList<State>();
		this.acceptingStates = new ArrayList<State>();
		this.symbols = new ArrayList<Symbol>();
	}

	/**
	 * @return the states
	 */
	public List<State> getStates() {
		return states;
	}

	/**
	 * @return the symbols
	 */
	public List<Symbol> getSymbols() {
		return symbols;
	}

	/**
	 * @return the acceptingStates
	 */
	public List<State> getAcceptingStates() {
		return acceptingStates;
	}

	/**
	 * @return the initialState
	 */
	public State getInitialState() {
		return initialState;
	}

	/**
	 * @param states the states to set
	 */
	public void setStates(List<State> states) {
		this.states = states;
	}

	/**
	 * @param symbols the symbols to set
	 */
	public void setSymbols(List<Symbol> symbols) {
		this.symbols = symbols;
	}

	/**
	 * @param acceptingStates the acceptingStates to set
	 */
	public void setAcceptingStates(List<State> acceptingStates) {
		this.acceptingStates = acceptingStates;
	}

	/**
	 * @param initialState the initialState to set
	 */
	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}
	
	public State getStateByString(String name) {
		for (int i = 0; i < this.getStates().size(); i++)
			if (this.getStates().get(i).getName().equals(name))
				return this.getStates().get(i);
		return null;
	}
	
	public Symbol getSymbolByString(String name) {
		for (int i = 0; i < this.getSymbols().size(); i++)
			if (this.getSymbols().get(i).getName().equals(name))
				return this.getSymbols().get(i);
		return null;
	}
	
	public void addSymbol(Symbol s) {
		if (!this.getSymbols().contains(s)) 
			this.getSymbols().add(s);
	}
	
	public void addState(State s) {
		if (!this.getStates().contains(s))
			this.getStates().add(s);
	}
	
	public void addAcceptingStates(State s) {
		if (this.getStates().contains(s)) 
			if (!this.getAcceptingStates().contains(s)) 
				this.getAcceptingStates().add(s);	
	}
	
	public void displayDFA() {
		// States
		String states = "States: ";
		for (State s : this.getStates())
			states += s.getName()+",";
		states = states.substring(0,states.length()-1);
		System.out.println(states);
		
		// Symbols
		String symbols = "Symbols: ";
		for (Symbol s : this.getSymbols())
			symbols += s.getName()+",";
		symbols = symbols.substring(0,symbols.length()-1);
		System.out.println(symbols);
		
		// Accepting states
		String accstates = "Accepting states: ";
		for (State s : this.getAcceptingStates())
			accstates += s.getName()+",";
		accstates = accstates.substring(0,accstates.length()-1);
		System.out.println(accstates);
		
		// Initial state
		System.out.println("Initial state: "+this.getInitialState().getName());
		
		// Transitions
		System.out.println("Transitions: ");
		for (int i = 0; i < this.getStates().size(); i++) {
			for (int j = 0; j < this.getStates().get(i).getTransitions().size(); j++) {
				System.out.println(this.getStates().get(i).getName()+","+this.getStates().get(i).getTransitions().get(j).getSymbol().getName()+"->"+this.getStates().get(i).getTransitions().get(j).getReachState().getName());
			}
		}
	}
}
