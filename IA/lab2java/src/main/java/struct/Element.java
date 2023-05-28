package struct;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Class to create an element, that contains a list of clauses, cooking wished & goals states. 
 * @author chloe
 *
 */
public class Element {
	
	public List<Clause> clauses;
	public List<UserWishes> wishes;
	public List<Clause> goals;
	public Clause original;

	/**
	 * Create an element with a list of clauses & user wishes. 
	 */
	public Element() {
		this.clauses = new LinkedList<Clause>();
		this.wishes = new LinkedList<UserWishes>();
		this.goals = new LinkedList<Clause>();

	}

	/**
	 * @return the clauses
	 */
	public List<Clause> getClauses() {
		return clauses;
	}

	/**
	 * @param clauses the clauses to set
	 */
	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
	}

	/**
	 * @return the wishes
	 */
	public List<UserWishes> getWishes() {
		return wishes;
	}

	/**
	 * @param wishes the wishes to set
	 */
	public void setWishes(List<UserWishes> wishes) {
		this.wishes = wishes;
	}
	
	/**
	 * @return the goals
	 */
	public List<Clause> getGoals() {
		return goals;
	}

	/**
	 * @param goals the goals to set
	 */
	public void setGoals(List<Clause> goals) {
		this.goals = goals;
	}
	
	@Override
	public String toString() {
		return "Element [clauses=" + clauses + ", wishes=" + wishes + ", goal=" + goals + "]";
	}

	/**
	 * Returns the clause if the content of the clause is equal to the parameter. 
	 * @param contains Content of clause we are looking for. 
	 * @return The clause if found, null otherwise. 
	 */
	public Clause getClause(String contains) {
		for (Clause c : this.getClauses()) {
			if (c.getContained().equalsIgnoreCase(contains)) {
				return c;
			}
		}
		return null;
	}
}
