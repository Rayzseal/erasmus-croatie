package solution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import structure.DPDA;
import structure.StackSymbols;
import structure.State;
import structure.Symbol;
import structure.Transitions;
import utils.Parser;

/**
 * 
 * Class pushdown automata, basically executes the algorithm of the pushdown automata on a given file. 
 * @author chloe
 *
 */
public class PushdownAutomata {
	
	private Stack<StackSymbols> queue;
	private State currentState;
	private ArrayList<Symbol> currentSymbol;
	private DPDA dpda;

	/**
	 * Constructor of a pushdown automata. 
	 * @param file File given to create the structure of the pushdown automata.
	 * @throws IOException Exceptions can occur while reading the input file. 
	 * 
	 */
	public PushdownAutomata(String file) throws IOException {
		this.queue =  new Stack<StackSymbols>();
		Parser p = new Parser();
		p.read(file);
		this.dpda = p.getDpda();
		this.queue.push(this.dpda.getInitialStack());
		this.currentState = this.dpda.getInitialState();
		this.currentSymbol = new ArrayList<Symbol>();
		this.currentSymbol.addAll(this.dpda.getAcceptingString());
	}
	
	/**
	 * Checks if a transition can be done, if it can be done, it will be executed.
	 * @return Returns true if a transitions can be done, false otherwise.
	 */
	public boolean transitionCanBeDone() {
		int i = 0;
		// If the queue is empty, we can not do anything
		if (this.queue.empty())
				return false;
		
		while (i < currentState.getTransitions().size()) {
			// Check if the transition is using the actual string & stack symbol
			// If that's the case, we will execute this transition
			// Otherwise, we will continue to look for other transitions that could maybe be executed
			if (currentState.getTransitions().get(i).getStack().equals(this.queue.firstElement()) && currentState.getTransitions().get(i).getSymbol().equals(this.currentSymbol.get(0))) {
				// Print the transition executed in the console
				System.out.println(currentState.getName()+"\t"+displayInput()+"\t"+displayStack()+"\t("+currentState.getName()+""+currentState.getTransitions().get(i));
				transition(currentState.getTransitions().get(i));
				return true;
			}
			i++;
		}
		return false;
	}

	/**
	 * Does the transition from one state to another.
	 * @param transition Transition to be executed.
	 */
	public void transition(Transitions transition) {
		
		// Deleting head of the queue
		this.queue.remove(0);
		
		// Adding elements to the queue
		for (int i = transition.getReachedStack().size()-1; i >= 0; i--)
			if (!transition.getReachedStack().get(i).getName().equals("€"))
				this.queue.add(0,transition.getReachedStack().get(i));
		
		// Next state
		this.currentState = transition.getReachState();
		
		// Deleting symbol
		this.currentSymbol.remove(0);
		
		// To make sure if we have transitions on empty string that they could be executed
		if (this.currentSymbol.isEmpty())
			this.currentSymbol.add(new Symbol("€"));
		
	}
	
	/**
	 * Method to check if we only have empty symbols in the string read
	 * @return True if there is only empty symbols, false otherwise.
	 */
	public boolean checkOnlyContainsEmptySymbol() {
		boolean res = true;
		for (int i = 0; i < this.currentSymbol.size(); i++)
			if (!this.currentSymbol.get(i).getName().equals("€"))
				res=false;
		return res;
	}
	
	/**
	 * Method to check if we only have empty symbols in the queue
	 * @return True if there is only empty symbols, false otherwise.
	 */
	public boolean checkOnlyContainsEmptySymbolQueue() {
		boolean res = true;
		for (int i = 0; i < this.queue.size(); i++)
			if (!this.queue.get(i).getName().equals("€"))
				res = false;
		return res;
	}
	
	/**
	 * Display the actual state of the string.
	 * @return Return the actual state of the string.
	 */
	public String displayInput() {
		String res = "";
		for (int i = 0; i < this.currentSymbol.size(); i++)
			res += this.currentSymbol.get(i).getName();
		return res;
	}
	
	/**
	 * Display the actual state of the stack.
	 * @return Return the actual state of the stack.
	 */
	public String displayStack() {
		String res = "";
		for (int i = 0; i < this.queue.size(); i++)
			res += this.queue.get(i).getName();
		return res;
	}
	
	/**
	 * Display the entire string.
	 * @return Return the entire string.
	 */
	public String displayString() {
		String res = "";
		for (int i = 0; i < this.dpda.getAcceptingString().size(); i++)
			res += this.dpda.getAcceptingString().get(i).getName();
		return res;
	}
	
	/**
	 * Print the final result of the automaton, wether if the string has been accepted or not.
	 */
	public void printResult() {
		if (this.dpda.isAcceptByEmptyStack()) {
			if ((this.queue.isEmpty() || checkOnlyContainsEmptySymbolQueue()) && (this.currentSymbol.isEmpty() || checkOnlyContainsEmptySymbol())) 
				System.out.println("The string "+displayString()+" is accepted");
			else
				System.out.println("The string "+displayString()+" is not accepted");
		} else if (this.dpda.isAcceptByFinalState()) {
			if (this.dpda.getAcceptingStates().contains(currentState) && (this.currentSymbol.isEmpty() || checkOnlyContainsEmptySymbol())) 
				System.out.println("The string "+displayString()+" is accepted");
			else
				System.out.println("The string "+displayString()+" is not accepted");
		}
	}
	
	/**
	 * Run the algorithm of the pushdown automata.
	 */
	public void pushdownAuto() {
		while (this.transitionCanBeDone());
		printResult();
	}


	/**
	 * Main of the class, starts the algorithm.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String nameFile = "task_23_accepting.txt";
		PushdownAutomata pdau = new PushdownAutomata(nameFile);
		pdau.pushdownAuto();
	}
}
