package structure;

import java.util.ArrayList;

public class DPDA {
	
	private ArrayList<State> states;
	private ArrayList<Symbol> symbols;
	private ArrayList<StackSymbols> stackSymbols;
	private ArrayList<State> acceptingStates;
	private ArrayList<Symbol> acceptingString;
	private State initialState;
	private StackSymbols initialStack;
	private boolean acceptByFinalState;
	private boolean acceptByEmptyStack;
	
	/**
	 * Creates a new DPDA, with empty lists.
	 */
	public DPDA() {
		this.states = new ArrayList<State>();
		this.symbols = new ArrayList<Symbol>();
		this.stackSymbols = new ArrayList<StackSymbols>();
		this.acceptingStates = new ArrayList<State>();
		this.acceptingString = new ArrayList<Symbol>();
	}
	
	public void addState(State state) {
		if (!this.states.contains(state)) {
			this.states.add(state);
		}
	}
	
	public void addSymbol(Symbol symbol) {
		if (!this.symbols.contains(symbol)) {
			this.symbols.add(symbol);
		}
	}
	
	public void addStack(StackSymbols stack) {
		if (!this.stackSymbols.contains(stack)) {
			this.stackSymbols.add(stack);
		}
	}
	
	public void addAcceptingState(State acceptingState) {
		if (this.states.contains(acceptingState) && !this.acceptingStates.contains(acceptingState)) {
			this.acceptingStates.add(acceptingState);
		}
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
	
	public StackSymbols getStackByString(String name) {
		for (int i = 0; i < this.getStackSymbols().size(); i++)
			if (this.getStackSymbols().get(i).getName().equals(name))
				return this.getStackSymbols().get(i);
		return null;
	}

	/**
	 * @return the states
	 */
	public ArrayList<State> getStates() {
		return states;
	}

	/**
	 * @return the symbols
	 */
	public ArrayList<Symbol> getSymbols() {
		return symbols;
	}

	/**
	 * @return the stackSymbols
	 */
	public ArrayList<StackSymbols> getStackSymbols() {
		return stackSymbols;
	}

	/**
	 * @return the acceptingStates
	 */
	public ArrayList<State> getAcceptingStates() {
		return acceptingStates;
	}

	/**
	 * @return the initialState
	 */
	public State getInitialState() {
		return initialState;
	}

	/**
	 * @return the acceptingString
	 */
	public ArrayList<Symbol> getAcceptingString() {
		return acceptingString;
	}

	/**
	 * @param states the states to set
	 */
	public void setStates(ArrayList<State> states) {
		this.states = states;
	}

	/**
	 * @param symbols the symbols to set
	 */
	public void setSymbols(ArrayList<Symbol> symbols) {
		this.symbols = symbols;
	}

	/**
	 * @param stackSymbols the stackSymbols to set
	 */
	public void setStackSymbols(ArrayList<StackSymbols> stackSymbols) {
		this.stackSymbols = stackSymbols;
	}

	/**
	 * @param acceptingStates the acceptingStates to set
	 */
	public void setAcceptingStates(ArrayList<State> acceptingStates) {
		this.acceptingStates = acceptingStates;
	}

	/**
	 * @param initialState the initialState to set
	 */
	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	/**
	 * @param acceptingString the acceptingString to set
	 */
	public void setAcceptingString(ArrayList<Symbol> acceptingString) {
		this.acceptingString = acceptingString;
	}

	/**
	 * @return the acceptByFinalState
	 */
	public boolean isAcceptByFinalState() {
		return acceptByFinalState;
	}

	/**
	 * @return the acceptByEmptyStack
	 */
	public boolean isAcceptByEmptyStack() {
		return acceptByEmptyStack;
	}

	/**
	 * @param acceptByFinalState the acceptByFinalState to set
	 */
	public void setAcceptByFinalState(boolean acceptByFinalState) {
		this.acceptByFinalState = acceptByFinalState;
	}

	/**
	 * @param acceptByEmptyStack the acceptByEmptyStack to set
	 */
	public void setAcceptByEmptyStack(boolean acceptByEmptyStack) {
		this.acceptByEmptyStack = acceptByEmptyStack;
	}

	/**
	 * @return the initialStack
	 */
	public StackSymbols getInitialStack() {
		return initialStack;
	}

	/**
	 * @param initialStack the initialStack to set
	 */
	public void setInitialStack(StackSymbols initialStack) {
		this.initialStack = initialStack;
	}

	@Override
	public String toString() {
		return "DPDA [states=" + states + ", symbols=" + symbols + ", stackSymbols=" + stackSymbols
				+ ", acceptingStates=" + acceptingStates + ", initialState=" + initialState.getName() + ", acceptingString="
				+ acceptingString + ", acceptByFinalState=" + acceptByFinalState + ", acceptByEmptyStack="
				+ acceptByEmptyStack + "]";
	}
}
