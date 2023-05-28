package structure;

import java.util.ArrayList;

/**
 * 
 * Class transitions, used to create a transition defined by a symbol, a stack symbol, a reached state, and a list of 
 * next stack symbol.
 * @author chloe
 *
 */
public class Transitions {
	
	private Symbol symbol;
	private StackSymbols stack;
	private State reachState;
	private ArrayList<StackSymbols> reachedStack;
	
	/**
	 * Constructor of a transition, a transition is defined by a symbol and a stack symbol which lead to another state 
	 * and other stack symbols.
	 * @param symbol
	 * @param stack
	 * @param reachState
	 */
	public Transitions(Symbol symbol, StackSymbols stack, State reachState) {
		this.symbol = symbol;
		this.stack = stack;
		this.reachState = reachState;
		this.reachedStack = new ArrayList<StackSymbols>();
	}

	/**
	 * @return the symbol
	 */
	public Symbol getSymbol() {
		return symbol;
	}

	/**
	 * @return the stack
	 */
	public StackSymbols getStack() {
		return stack;
	}

	/**
	 * @return the reachState
	 */
	public State getReachState() {
		return reachState;
	}

	/**
	 * @return the reachedStack
	 */
	public ArrayList<StackSymbols> getReachedStack() {
		return reachedStack;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	/**
	 * @param stack the stack to set
	 */
	public void setStack(StackSymbols stack) {
		this.stack = stack;
	}

	/**
	 * @param reachState the reachState to set
	 */
	public void setReachState(State reachState) {
		this.reachState = reachState;
	}

	/**
	 * @param reachedStack the reachedStack to set
	 */
	public void setReachedStack(ArrayList<StackSymbols> reachedStack) {
		this.reachedStack = reachedStack;
	}
	
	public String displayStack() {
		String res = "";
		for (int i = 0; i < this.reachedStack.size(); i++)
			res += this.reachedStack.get(i).getName();
		return res;
	}

	@Override
	public String toString() {
		return ","+symbol.getName() + "," + stack.getName() + ") = {(" + reachState.getName() + ","
				+ displayStack() + ")}";
	}
}
