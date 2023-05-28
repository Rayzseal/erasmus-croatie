package struct;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * Class to create a clause with an id, a content and the clauses used to derive it. 
 * @author chloe
 *
 */
public class Clause implements Comparable<Clause>{
	
	// Used to create an id
	private static final AtomicInteger ID_CLAUSE = new AtomicInteger();
	// ID used
	private final int id;
	// Clause in CNF format
	private String contained;
	
	// If it has been derived, clauses used to create this clause
	private boolean usedSOS;
	private Clause c1;
	private Clause c2;
	
	// Creating a new non derived clause.
	public Clause(String contains) {
		id = ID_CLAUSE.getAndIncrement();
		this.contained = contains;
		usedSOS = false;
		c1 = null;
		c2 = null;
	}
	
	// Creating a new derived clause.
	public Clause(String contains, Clause c1, Clause c2) {
		id = ID_CLAUSE.getAndIncrement();
		this.contained = contains;
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public String toString() {
		if (c1!=null)
			return "Clause [id=" + id + ", contained=" + contained + ", c1=" + c1.id + ", c2=" + c2.id + "]\n";
		else return
				"Clause [id=" + id + ", contained=" + contained + "]\n";
	}

	/**
	 * @return the idClause
	 */
	public static AtomicInteger getIdClause() {
		return ID_CLAUSE;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the contained
	 */
	public String getContained() {
		return contained;
	}

	/**
	 * @return the c1
	 */
	public Clause getC1() {
		return c1;
	}

	/**
	 * @return the c2
	 */
	public Clause getC2() {
		return c2;
	}

	/**
	 * @param contained the contained to set
	 */
	public void setContained(String contained) {
		this.contained = contained;
	}

	/**
	 * @param c1 the c1 to set
	 */
	public void setC1(Clause c1) {
		this.c1 = c1;
	}

	/**
	 * @param c2 the c2 to set
	 */
	public void setC2(Clause c2) {
		this.c2 = c2;
	}
	
    /**
	 * @return the usedSOS
	 */
	public boolean isUsedSOS() {
		return usedSOS;
	}

	/**
	 * @param usedSOS the usedSOS to set
	 */
	public void setUsedSOS(boolean usedSOS) {
		this.usedSOS = usedSOS;
	}

	/**
	 * Method to compare 2 Clauses. 
	 * Used to sort clauses by size of string. 
	 * @param c Clause to compare with. 
	 */
	public int compareTo(Clause c) {
		int size1 = this.contained.split(" v ").length;
		int size2 = c.contained.split(" v ").length;
        return size2 - size1;
    }
}
