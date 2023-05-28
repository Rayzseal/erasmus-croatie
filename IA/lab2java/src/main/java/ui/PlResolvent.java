package ui;

import java.util.Collections;
import java.util.LinkedList;

import struct.Clause;
import struct.Element;

/**
 * 
 * Class to compute the refutation algorithm. 
 * It contains all kinds of method used to compute efficiently this algorithm.
 * @author chloe
 *
 */
public class PlResolvent {

	/**
	 * Refutation algorithm on a given set of clauses with a given goal state(s).
	 * @param element Set of clauses. 
	 * @param goal Goal state(s). 
	 * @return True or false depending on the fact that we found a solution. 
	 */
	public static boolean plResolution(Element element, String goal) {
		LinkedList<Clause> newC = new LinkedList<Clause>();
		LinkedList<Clause> clauses = new LinkedList<Clause>();
		
		// Negate the goal state
		negatingGoalState(element, goal);
		
		// Adding all the clauses to the list of clauses
		for (Clause c : element.getClauses()) 
			clauses.add(c);
		
		// Removes tautologies from the list of clauses
		removeTautologies(clauses);
		
		while (true) {
			for (int i = 0; i < clauses.size(); i++) {
				// SOS algorithm states that at least one
				// parent clause comes from SoS (= negated clause(s) and subsided clauses of
				// negated clause(s))
				if (clauses.get(i).isUsedSOS()) {
					LinkedList<Clause> resolvents = new LinkedList<Clause>();
					for (int y = 0; y < clauses.size(); y++) {
						if (y != i) {
							if (plResolve(clauses.get(i), clauses.get(y)) != null) {
								resolvents.add(plResolve(clauses.get(i), clauses.get(y)));
							}
							
							// It contains NIL
							if (resolvents.size() > 0 && resolvents.get(resolvents.size() - 1) != null
									&& resolvents.get(resolvents.size() - 1).getContained().equals("")) {
								displayResult(true,element,resolvents);
								return true;
							}
						}
					}
					// Union of the resolvents and new list
					newC = union(newC, resolvents);
				}
			}
			// There is no solution to this problem : 
			if (checkingIfToAreListEquals(newC, clauses) ) {
				displayResult(false,element,null);
				return false;
			}
			// Union of the clauses and new list
			clauses = union(clauses, newC);
		}
	}

	/**
	 * Recursive function used to show the useful constraints that has been created to find a solution 
	 * to the refutation algorithm. 
	 * @param list List of clauses to return. 
	 * @param parentsToAdd Parent of the clause that will be added to the list of clauses. 
	 */
	public static void addingToList(LinkedList<Clause> list, Clause parentsToAdd) {
		if (parentsToAdd.getC1() != null && !list.contains(parentsToAdd.getC1())) {
			list.add(parentsToAdd.getC1());
			addingToList(list, parentsToAdd.getC1());
		}

		if (parentsToAdd.getC2() != null && !list.contains(parentsToAdd.getC2())) {
			list.add(parentsToAdd.getC2());
			addingToList(list, parentsToAdd.getC2());
		}

	}

	/**
	 * Display the result of the refutation algorithm. 
	 * It can either be the solution found, or that no solution has been solution. 
	 * " xx is unknown". 
	 * @param result What kind of display we want (solution found, or no solution has been found)
	 * @param element Original clause that has been negated & set of original clauses. 
	 * @param resolvents Clauses derived during the refutation algorithm. 
	 */
	public static void displayResult(boolean result, Element element, LinkedList<Clause> resolvents) {
		if (result) {
			for (int a = 0; a < element.getClauses().size(); a++)
				System.out.println(
						element.getClauses().get(a).getId() + ". " + element.getClauses().get(a).getContained());
			System.out.println("===============");
			LinkedList<Clause> dispClauses = new LinkedList<Clause>();
			dispClauses.add(resolvents.get(resolvents.size() - 1));
			addingToList(dispClauses, resolvents.get(resolvents.size() - 1));
			for (int a = dispClauses.size() - 1; a > 0; a--)
				if (dispClauses.get(a).getC1() != null)
					System.out.println(dispClauses.get(a).getId() + ". " + dispClauses.get(a).getContained() + " ("
							+ dispClauses.get(a).getC1().getId() + ", " + dispClauses.get(a).getC2().getId() + ")");
			System.out.println(resolvents.get(resolvents.size() - 1).getId() + ". NIL ("
					+ resolvents.get(resolvents.size() - 1).getC1().getId() + ", "
					+ resolvents.get(resolvents.size() - 1).getC2().getId() + ")");
			System.out.println("===============");
			System.out.println("[CONCLUSION]: " + element.original.getContained() + " is true");
		} else {
			System.out.println("[CONCLUSION]: " + element.original.getContained() + " is unknown");
		}
	}

	/**
	 * Negate the goal state for the refutation algorithm. 
	 * @param element Set of clauses. 
	 * @param goal Goal clause to be negated and added to the set of clauses. 
	 */
	public static void negatingGoalState(Element element, String goal) {
		String parsed[] = goal.split(" v ");
		Clause original = new Clause(goal);
		element.original = original;
		for (int i = 0; i < parsed.length; i++) {
			// It is either a negated clause or a simple clause
			int index = parsed[i].indexOf("~");
			// It is a negated clause
			if (index != -1) {
				// Deleting the negated symbol
				String contains = parsed[i].substring(index + 1);
				Clause c = new Clause(contains);
				c.setUsedSOS(true);
				// Adding the clause to the list of clauses and goal clauses
				if (i == 0) {
					element.getClauses().get(element.getClauses().size() - 1).setContained(contains);
					element.getClauses().get(element.getClauses().size() - 1).setUsedSOS(true);
					element.getGoals().add(element.getClauses().get(element.getClauses().size() - 1));
				} else {
					element.getClauses().add(c);
					element.getGoals().add(c);
				}
			} else { // It is not a negated clause
				// Adding the negated symbol
				String contains = "~" + parsed[i];
				Clause c = new Clause(contains);
				c.setUsedSOS(true);
				// Adding the clause to the list of clauses and goal clauses
				if (i == 0) {
					element.getClauses().get(element.getClauses().size() - 1).setContained(contains);
					element.getClauses().get(element.getClauses().size() - 1).setUsedSOS(true);
					element.getGoals().add(element.getClauses().get(element.getClauses().size() - 1));
				} else {
					element.getClauses().add(c);
					element.getGoals().add(c);
				}
			}
		}
	}
	
	/**
	 * Function to remove tautologies from a given list of clauses. 
	 * @param list List of clauses. 
	 */
	public static void removeTautologies(LinkedList<Clause> list) {
		// Going through the clause
		for (int i = 0; i < list.size(); i++) {
			// Getting all symbols of a clause
			String data[] = list.get(i).getContained().split(" v ");
			
			// Will be set to true if a tautology has been found, so that we will 
			// check the next clause of the list. 
			boolean found = false;
			int y = 0;
			
			while (!found && y < data.length) {
				// We are trying to find an argument that is not negated in the clause 
				// and which has the same content of the clause data[y]
				if (data[y].indexOf("~") != -1) {
					// A tautology has been found
					if (checkIfIsInTwiceInString(list.get(i).getContained(), data[y],
							data[y].substring(data[y].indexOf("~") + 1))) {
						found = true;
						// It is removed from the list
						list.remove(list.get(i));
						// Since we removed an element, we have to decrement the index of the list to 
						// next check the next element of the list
						// If we don't do that, we will skip one element of the list
						i--;
					}
				// We are trying to find an argument that is negated in the clause 
				// and which has the same content of the clause data[y]
				} else {
					// A tautology has been found
					if (checkIfIsInTwiceInString(list.get(i).getContained(), data[y], "~" + data[y])) {
						found = true;
						// It is removed from the list
						list.remove(list.get(i));
						// Since we removed an element, we have to decrement the index of the list to 
						// next check the next element of the list
						// If we don't do that, we will skip one element of the list
						i--;
					}
				}
				y++;
			}
		}
	}
	
	/**
	 * Removes redundant clauses from a list.  
	 * @param list List with possible redundant clauses. 
	 * @return A list with no redundant clauses. 
	 */
	public static LinkedList<Clause> removeRedundantClauses(LinkedList<Clause> list) {
		// List with no redundant clauses. 
		LinkedList<Clause> res = new LinkedList<Clause>();
		
		// We are going through the list of clauses
		for (int i = 0; i < list.size(); i++) {
			boolean tmp = false;
			// We are going to the last element before the one we are checking
			for (int y = res.size() - 1; y > i; y--) {
				// The clause is redundant, it won't be added to the result list
				if (checkIfAllIsInString(list.get(i).getContained(), list.get(y).getContained())) {
					tmp = true;
				}
			}
			// Non redundant clauses or added to the list of clauses we will return
			if (!tmp)
				res.add(list.get(i));
		}
		return res;
	}

	/**
	 * Method union, that does the union between two list, and will check if 
	 * there is no redundant clauses while combining those 2 lists. 
	 * @param list1 First list to combine. 
	 * @param list2 Second list to combine. 
	 * @return A list with list1 & list2 combine and no redundant clauses. 
	 */
	public static LinkedList<Clause> union(LinkedList<Clause> list1, LinkedList<Clause> list2) {

		LinkedList<Clause> union = new LinkedList<Clause>();
		LinkedList<Clause> res = new LinkedList<Clause>();

		// Adding all of the elements to the union list
		for (int i = 0; i < list1.size(); i++) {
			union.add(list1.get(i));
		}
		for (int i = 0; i < list2.size(); i++) {
			union.add(list2.get(i));
		}

		// Sorting clauses of union by sizes
		Collections.sort(union);

		// Removing redundant clauses
		res = removeRedundantClauses(union);

		return res;
	}

	/**
	 * Checks if the string check is entirely contained in the string base.
	 * 
	 * @param base  Base string
	 * @param check Check string
	 * @return True if the entire string is contained, false otherwise.
	 */
	public static boolean checkIfAllIsInString(String base, String check) {
		String toCheck[] = check.split(" v ");
		String baseTab[] = base.split(" v ");
		int cptF = 0;
		for (int i = 0; i < toCheck.length; i++) {
			for (int y = 0; y < baseTab.length; y++) {
				if (baseTab[y].equals(toCheck[i]))
					cptF++;
			}
		}

		return (cptF == toCheck.length) ? true : false;
	}

	/**
	 * Check if 2 given elements are both present in a base string. 
	 * @param base Base string we will go through. 
	 * @param search1 First element we are looking for. 
	 * @param search2 Second element we are looking for. 
	 * @return True if both elements are found, false otherwise. 
	 */
	public static boolean checkIfIsInTwiceInString(String base, String search1, String search2) {
		String data[] = base.split(" v ");
		boolean res1 = false, res2 = false;

		for (int i = 0; i < data.length; i++) {
			if (data[i].equals(search1))
				res1 = true;
			if (data[i].equals(search2))
				res2 = true;
		}

		return (res1 && res2);
	}

	/**
	 * Remove content present twice in string.
	 * 
	 * @param base    Base string.
	 * @param search1 Content 1 to remove.
	 * @param search2 Content 2 to remove.
	 * @return The new string without search content in parameters.
	 */
	public static String removeIfTwiceInString(String base, String search1, String search2) {
		String res = "";
		if (search1.equals(search2) == true) {
			String data[] = base.split(" v ");
			for (int i = 0; i < data.length - 1; i++) {
				for (int y = i + 1; y < data.length; y++) {
					if (data[i].equals(data[y])) {
						res = removeContent(base, search1, true);
						return res;
					}
				}
			}
		} else if (base.indexOf(search1) != -1 && base.lastIndexOf(search2) != -1) {
			res = removeContent(base, search1, false);
			res = removeContent(res, search2, false);
			return res;
		}
		return base;
	}

	/**
	 * Remove content from a string.
	 * 
	 * @param base     Base string.
	 * @param toRemove Content to remove.
	 * @return The new string without the content in parameters.
	 */
	public static String removeContent(String base, String toRemove, boolean multiple) {
		String res = "";
		String tmp[] = base.split(" v ");
		int cpt = 0;
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].equals(toRemove)) {
				if (multiple) {
					if (cpt == 0) {
						if (res.length() != 0)
							res += " v " + tmp[i];
						else
							res += tmp[i];
						cpt++;
					} else {
						continue;
					}
				} else {
					continue;
				}
			} else if (res.length() != 0) {
				res += " v " + tmp[i];
			} else {
				res += tmp[i];
			}
		}
		return res;
	}

	/**
	 * Check if the list l1 is entirely contained in the list l2. 
	 * @param l1 List of clauses l1 (has to be either equal or smaller list than l2). 
	 * @param l2 List of clauses L2 (has to be either equal or bigger list than l1).
	 * @return True if all elements of l1 are part of l2, false otherwise. 
	 */
	public static boolean checkingIfToAreListEquals(LinkedList<Clause> l1, LinkedList<Clause> l2) {
		int cpt = 0;

		for (int i = 0; i < l1.size(); i++) {
			boolean found = true; 
			int y = 0;
			while (found && y < l2.size()) {
				if (checkIfAllIsInString(l1.get(i).getContained(),l2.get(y).getContained())) {
					cpt++;
					found = false;
				}
				y++;
			}
		}
		
		return (cpt == l1.size()) ? true : false;
	}

	/**
	 * Checks if 2 given clauses can be combined to create a third clause, which will be a combination 
	 * of those 2 clauses. 
	 * @param c1 First clause. 
	 * @param c2 Second clause. 
	 * @return The combination of clauses if they added new information, null otherwise. 
	 */
	public static String isInteresting(Clause c1, Clause c2) {
		String data1[] = c1.getContained().split(" v ");
		String data2[] = c2.getContained().split(" v ");

		for (int i = 0; i < data1.length; i++) {
			for (int y = 0; y < data2.length; y++) {
				if (data1[i].indexOf("~") != -1) {
					String toCompare = data1[i].substring(data1[i].indexOf("~") + 1);
					if (data2[y].equals(toCompare)) {
						String res = removeContent(c1.getContained(), data1[i], false);
						res = removeContent(res, data2[y], false);
						return res;
					}
				} else {
					if (data2[y].equals("~" + data1[i])) {
						String res = removeContent(c1.getContained(), data1[i], false);
						res = removeContent(res, data2[y], false);
						return res;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Resolve 2 clauses if the combination of those 2 is adding new information. 
	 * @param c1 First clause we will try to derive. 
	 * @param c2 Second clause we will try to derive. 
	 * @return A new clause if the combination of the 2 clauses in parameters added information, null otherwise. 
	 */
	public static Clause plResolve(Clause c1, Clause c2) {
		if (isInteresting(c1, c2) != null && isInteresting(c2, c1) != null) {
			String res;
			if (isInteresting(c1, c2).length() == 0 && isInteresting(c2, c1).length() == 0)
				res = "";
			else if (isInteresting(c1, c2).length() == 0)
				res = isInteresting(c2, c1);
			else if (isInteresting(c2, c1).length() == 0)
				res = isInteresting(c1, c2);
			else
				res = isInteresting(c1, c2) + " v " + isInteresting(c2, c1);
			
			Clause resolvent = new Clause(res);
			// Adding the 2 clauses used to derive this new clause
			resolvent.setC1(c1);
			resolvent.setC2(c2);
			// So that we can use this clause to be derived again
			resolvent.setUsedSOS(true);
			// New clause to be added 
			return resolvent;
		}
		return null;
	}
}
