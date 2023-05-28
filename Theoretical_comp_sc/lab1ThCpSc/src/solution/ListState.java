package solution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import structure.DFA;
import structure.State;
import structure.Symbol;
import structure.Transitions;
import utils.Parser;

public class ListState {

	private int change;
	private ListState listState;
	private List<List<State>> listOfStates;

	/**
	 * @param change
	 */
	public ListState() {
		this.change = 2;
		this.listOfStates = new ArrayList<List<State>>();
	}

	public void listState() {
		// Clearing all lists
		for (int i = 0; i < this.listOfStates.size(); i++) {
			this.listOfStates.get(i).clear();
		}
		listOfStates.clear();

		// Adding all list
		for (int i = 0; i < listState.listOfStates.size(); i++) {
			this.getListOfStates().add(new ArrayList<State>());
			this.getListOfStates().get(i).addAll(listState.listOfStates.get(i));
		}

		// Clearing all list
		for (int i = 0; i < listState.listOfStates.size(); i++) {
			listState.listOfStates.clear();
		}
		listState.listOfStates.clear();
	}

	/**
	 * @return the change
	 */
	public int isChange() {
		return change;
	}

	/**
	 * @return the listOfStates
	 */
	public List<List<State>> getListOfStates() {
		return listOfStates;
	}

	/**
	 * @param change the change to set
	 */
	public void setChange(int change) {
		this.change = change;
	}

	/**
	 * @param listOfStates the listOfStates to set
	 */
	public void setListOfStates(List<List<State>> listOfStates) {
		this.listOfStates = listOfStates;
	}

	/**
	 * Initiating the list for the minimization algorithm.
	 * 
	 * @param dfa DFA to use.
	 */
	public void init(DFA dfa) {
		// Getting all not accepted states
		ArrayList<State> notAccepted = new ArrayList<State>();
		for (int i = 0; i < dfa.getStates().size(); i++)
			if (!dfa.getAcceptingStates().contains(dfa.getStates().get(i)))
				notAccepted.add(dfa.getStates().get(i));

		// Adding list
		this.getListOfStates().add(new ArrayList<State>());
		this.getListOfStates().add(new ArrayList<State>());

		// Adding all accepting states to the list of states at index 0
		this.getListOfStates().get(0).addAll(dfa.getAcceptingStates());
		// Adding all non accepting states to the list of states at index 1
		this.getListOfStates().get(1).addAll(notAccepted);
	}

	/**
	 * If the 2 given state in parameters are not part of the same list, we return
	 * false.
	 * 
	 * @param s1 1st state to check.
	 * @param s2 2nd state to check.
	 * @return False if the 2 elements are not part of the same list, true otherwise.
	 */
	public boolean isSameList(State s1, State s2) {
		for (int i = 0; i < this.listOfStates.size(); i++) {
			if (this.listOfStates.get(i).contains(s1) && !this.listOfStates.get(i).contains(s2)) {
				this.change = 2;
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes unreachable states from DFA.
	 * 
	 * @param dfa DFA
	 */
	public void removingUnreachableState(DFA dfa) {
		// Adding initial state to the list
		ArrayList<State> tmp = new ArrayList<State>();
		tmp.add(dfa.getInitialState());

		// The list tmp contains all reachable states
		findingReachableStates(tmp, 0);

		removeStateNotContainedInListFromDfa(tmp, dfa);

		// Removing the content of symbols when the state is unreachable and when it appears in hashmap
		for (Symbol s : dfa.getSymbols()) {
			for (Map<State, State> map : s.getListOfStates()) {
				Iterator<?> iterator = map.entrySet().iterator();
				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry mapentry = (Map.Entry) iterator.next();
					if (!tmp.contains(mapentry.getKey()) || !tmp.contains(mapentry.getValue())) {
						map.remove(mapentry.getKey(), mapentry.getValue());
					}
				}
			}
		}
	}

	/**
	 * This method checks if a state is not contained in the dfa, it that is the case
	 * if basically means that the state is unreachable and then it will 
	 * be removed from the list of states of the DFA.
	 * @param tmp List of reachable states.
	 * @param dfa DFA.
	 */
	public void removeStateNotContainedInListFromDfa(ArrayList<State> tmp, DFA dfa) {
		ArrayList<State> tmpState = new ArrayList<State>();

		// Removing from transitions where it appears & from dfa states
		for (State s : dfa.getStates()) {
			ArrayList<Transitions> tmpTransitions = new ArrayList<Transitions>();
			if (!tmp.contains(s)) {
				tmpState.add(s);
			} else {
				for (Transitions t : s.getTransitions()) {
					if (!tmp.contains(t.getReachState())) {
						tmpTransitions.add(t);
					}
				}
				s.getTransitions().removeAll(tmpTransitions);
			}
		}
		
		// To make sure we don't remove a state that is used at the moment of the execution
		dfa.getStates().removeAll(tmpState);
		dfa.getAcceptingStates().removeAll(tmpState);
	}

	/**
	 * This method will delete the element contained in the list tmp from the dfa.
	 * @param tmp List of state to delete. 
	 * @param dfa DFA.
	 * @param toBecome It is used to factorize states, so one state is deleted if only it already
	 * exist (in other words, another state provides the exact same information, so there is no need
	 * to keep both of them)
	 */
	public void removeStateContainedInListFromDfa(ArrayList<State> tmp, DFA dfa, State toBecome) {

		ArrayList<State> tmpState = new ArrayList<State>();

		// Removing from transitions where it appears & from dfa states
		for (State s : dfa.getStates()) {

			ArrayList<Transitions> tmpTransitions = new ArrayList<Transitions>();
			if (tmp.contains(s)) {
				tmpState.add(s);

			} else {
				for (Transitions t : s.getTransitions()) {
					if (tmp.contains(t.getReachState())) {
						tmpTransitions.add(t);
					}
				}
				// To make sure we don't delete transitions which are not existing for a given state
				for (Symbol sym : dfa.getSymbols())
					for (Transitions tmpTransi : tmpTransitions)
						if (tmpTransi.getSymbol() == sym) {
							sym.add(s, toBecome);
						}

				s.getTransitions().removeAll(tmpTransitions);
			}
		}
		// To make sure we don't remove a state that is used at the moment of the execution
		dfa.getStates().removeAll(tmpState);
		dfa.getAcceptingStates().removeAll(tmpState);
	}

	/**
	 * Recursive function which will return an list containing all of the reachable states of the DFA.
	 * @param list List of reachable state.
	 * @param index To make sure we visit all of the reachable state. 
	 * @return The list of reachable states for a given DFA. 
	 */
	public ArrayList<State> findingReachableStates(ArrayList<State> list, int index) {
		if (index < list.size()) {
			for (Transitions t : list.get(index).getTransitions())
				if (!list.contains(t.getReachState()))
					list.add(t.getReachState());

			index++;
			findingReachableStates(list, index);
		} else
			return list;

		return null;
	}

	/**
	 * This method checks if for the state s1 and the state s2 if for a given transitions
	 * we will reach a state contained in the same list or not.
	 * @param s1 First state.
	 * @param s2 Second state.
	 * @param dfa DFA.
	 * @return Number of "same" transitions.
	 */
	public int compareTransitionsState(State s1, State s2, DFA dfa) {
		int nb = dfa.getSymbols().size();
		for (int i = 0; i < s1.getTransitions().size(); i++) {
			if (!isSameList(s1.getTransitions().get(i).getReachState(), s2.getTransitions().get(i).getReachState())) {
				nb--;
			}
		}
		return nb;
	}

	/**
	 * Basically checks if two transitions of two states are in the same list or not.
	 * @param s1 First state.
	 * @param s2 Second state. 
	 * @param dfa DFA. 
	 * @return True if s1's and s2's transitions are part of the same list, false otherwise. 
	 */
	public boolean compareTwoStates(State s1, State s2, DFA dfa) {
		// Number of symbols
		int res = dfa.getSymbols().size();
		int val = compareTransitionsState(s1, s2, dfa);

		// Return true if number or equals, false otherwise
		return res == val;
	}

	/**
	 * Minimization algorithm.
	 * 
	 * @param dfa DFA.
	 */
	public void minimization(DFA dfa) {
		// Removes unreachable states from the dfa
		removingUnreachableState(dfa);

		// Creates two lists, one containing all of the goal states
		// the other one containing all of the other states
		init(dfa);

		// By default this value is equal to true
		// It is set to false if no changes has been made during the iteration of the
		// algorithm
		while (change > 0) {
			// We are creating an empty element to store our new lists into
			// Will be used for the next iteration if there is one
			this.listState = new ListState();
			for (int i = 0; i < this.listOfStates.size(); i++) {
				// By default, we set this value to false
				// If we are modifying one element of a list, it will be set to true
				// While this value is equal to true, we continue the minimization algorithm
				change--;

				// We are adding the first element of the list, which will be compared to the
				// other
				// elements of the list (if they are some)
				ArrayList<State> origin = new ArrayList<State>();
				origin.add(this.listOfStates.get(i).get(0));

				// If the transitions from the first element of origin
				// and another element of its list don't have transitions
				// which are in the same list, then it will be added into this list
				ArrayList<State> newList = new ArrayList<State>();
				if (this.listOfStates.get(i).size() > 1) {
					for (int y = 1; y < this.listOfStates.get(i).size(); y++) {
						// We are comparing one state to another one
						if (!compareTwoStates(this.listOfStates.get(i).get(0), this.listOfStates.get(i).get(y), dfa)) {
							newList.add(this.listOfStates.get(i).get(y));
						} else {
							origin.add(this.listOfStates.get(i).get(y));
						}
					}
				}
				
				// We are making sure we don't add empty lists
				if (origin.size() != 0)
					this.listState.listOfStates.add(origin);
				if (newList.size() != 0)
					this.listState.listOfStates.add(newList);
			}
			// We prepare the list for the next iteration
			// Content of listState.listOfStates is now in listOfStates of the current
			// object
			// And listState.listOfStates is empty
			listState();
		}
	}

	/**
	 * We will basically check if two states are the same, 
	 * in other words, after minimization, we get multiple lists of states, 
	 * if we have more than one state in a list, it means that all of the states 
	 * contained in the list are basically the same. 
	 * We only keep the first state of the list and we will remove all of the other ones. 
	 * @param dfa DFA
	 * @return List of equal states in format state1=state2=...=stateX
	 */
	public ArrayList<String> constructingAndRemovingListOfSameStates(DFA dfa) {
		// Constructing list of equal states
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < this.listOfStates.size(); i++) {
			ArrayList<State> stateTmp = new ArrayList<State>();
			if (this.listOfStates.get(i).size() > 1) {
				String tmp = "";
				for (int y = 0; y < this.listOfStates.get(i).size(); y++) {
					tmp += this.listOfStates.get(i).get(y).getName() + "=";
					// Removing state contained in the dfa
					if (y > 0)
						stateTmp.add(this.listOfStates.get(i).get(y));
				}
				// To make sure we don't have an extra '='
				tmp = tmp.substring(0, tmp.length() - 1);
				// Adding string to the list of equal states (state1=state2=...=stateX)
				res.add(tmp);
			}
			// Remove state that are basically the same
			removeStateContainedInListFromDfa(stateTmp, dfa, this.listOfStates.get(i).get(0));
		}

		// Returning list with equal states
		return res;
	}

	/**
	 * This method is used to compare two symbols.
	 * Basically, we will get all of the transitions where either the symbol s1 or s2 is used,
	 * then, we will compare if for all of the state they go to the same state.
	 * In other words, if 2 symbols have the same information, we will return true.
	 * @param s1 Symbol s1 to check. 
	 * @param s2 Symbol s2 to check. 
	 * @param dfa DFA
	 * @return True if symbol have the same information, false otherwise. 
	 */
	public boolean compareTwoStatesSymbols(Symbol s1, Symbol s2, DFA dfa) {

		int res = dfa.getSymbols().size();
		State s1reached = null;
		State s2reached = null;

		for (State state : dfa.getStates()) {

			for (int i = 0; i < s1.getListOfStates().size(); i++) {
				if (s1.getListOfStates().get(i).get(state) != null
						& dfa.getStates().contains(s1.getListOfStates().get(i).get(state))) {
					s1reached = s1.getListOfStates().get(i).get(state);
				}

			}
			for (int i = 0; i < s2.getListOfStates().size(); i++) {
				if (s2.getListOfStates().get(i).get(state) != null
						& dfa.getStates().contains(s2.getListOfStates().get(i).get(state))) {
					s2reached = s2.getListOfStates().get(i).get(state);
				}
			}
		}

		if (!s1reached.equals(s2reached))
			res--;

		return res == dfa.getSymbols().size();

	}

	/**
	 * Removes from dfa redundant symbols in the list of symbols and in the transitions of each 
	 * states.
	 * @param toRemove List of symbols to remove
	 * @param dfa DFA.
	 */
	public void removingRedundantSymbols(ArrayList<Symbol> toRemove, DFA dfa) {

		ArrayList<Transitions> transitionsToRemove = new ArrayList<Transitions>();

		for (State s : dfa.getStates()) {
			for (Transitions t : s.getTransitions()) {
				if (toRemove.contains(t.getSymbol())) {
					transitionsToRemove.add(t);
				}
			}
			s.getTransitions().removeAll(transitionsToRemove);
		}

		dfa.getSymbols().removeAll(toRemove);

	}

	/**
	 * Check if there is symbols that are basically the same. 
	 * In other words, we will check if for two symbols we have the same transitions 
	 * (reaching the same state) for all of the states.
	 * @param dfa DFA.
	 * @return List of equal symbols in format symbols1=symbols2
	 */
	public ArrayList<String> constructingAndRemovingListOfSameSymbols(DFA dfa) {

		// List of symbols to remove
		ArrayList<Symbol> symbolsToRemove = new ArrayList<Symbol>();

		// Constructing list of equal symbols
		ArrayList<String> res = new ArrayList<String>();

		for (int i = 0; i < dfa.getSymbols().size() - 1; i++) {
			String tmpRes = "";
			for (int y = i + 1; y < dfa.getSymbols().size(); y++) {
				if (compareTwoStatesSymbols(dfa.getSymbols().get(i), dfa.getSymbols().get(y), dfa)) {
					tmpRes += dfa.getSymbols().get(i).getName() + "=" + dfa.getSymbols().get(y).getName()+" ";
					symbolsToRemove.add(dfa.getSymbols().get(y));
				}
			}
			if (tmpRes != "")
				res.add(tmpRes);
		}

		//removingRedundantSymbols(symbolsToRemove, dfa);
		return res;
	}

	/**
	 * Display the minimized DFA.
	 * @param dfa DFA.
	 */
	public void displayMinimized(DFA dfa) {

		String equalStates = "";
		String equalSymbols = "";

		for (String s : constructingAndRemovingListOfSameStates(dfa)) {
			equalStates += s + "\n";
		}
		for (String s : constructingAndRemovingListOfSameSymbols(dfa)) {
			equalSymbols += s + "\n";
		}

		dfa.displayDFA();
		System.out.println("----------");
		System.out.println("Same states:");
		if (equalStates!="")System.out.println(equalStates.substring(0, equalStates.length() - 1));
		//System.out.println("Same symbols:");
		//if (equalSymbols!="")System.out.println(equalSymbols.substring(0, equalSymbols.length() - 1));

	}
	
	/**
	 * Display the DFA before and after minimization.
	 * @param dfa
	 */
	public void displayAll(DFA dfa) {
		dfa.displayDFA();
		minimization(dfa);
		displayMinimized(dfa);
	}
	
	/**
	 * Display the DFA only after minimization.
	 * @param dfa
	 */
	public void displayOnlyMinimized(DFA dfa) {
		minimization(dfa);
		displayMinimized(dfa);
	}

}
