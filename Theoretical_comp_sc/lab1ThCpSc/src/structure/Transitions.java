package structure;

public class Transitions {
	
	private Symbol symbol;
	private State reachState;
	
	/**
	 * @param symbol
	 * @param reachState
	 */
	public Transitions(Symbol symbol, State reachState) {
		this.symbol = symbol;
		this.reachState = reachState;
	}
	
	/**
	 * @return the symbol
	 */
	public Symbol getSymbol() {
		return symbol;
	}
	/**
	 * @return the reachState
	 */
	public State getReachState() {
		return reachState;
	}
	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	/**
	 * @param reachState the reachState to set
	 */
	public void setReachState(State reachState) {
		this.reachState = reachState;
	}
	
	

}
