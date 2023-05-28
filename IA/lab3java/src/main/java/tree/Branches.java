package tree;

import java.util.ArrayList;

/**
 * 
 * Class branches, contains all the branches of a tree. 
 * @author chloe
 *
 */
public class Branches {
	
	private ArrayList<String> branches;
	
	/**
	 * Creates and empty list of branches. 
	 */
	public Branches() {
		this.branches = new ArrayList<String>();
	}

	/**
	 * @return the branches
	 */
	public ArrayList<String> getBranches() {
		return branches;
	}

	/**
	 * @param branches the branches to set
	 */
	public void setBranches(ArrayList<String> branches) {
		this.branches = branches;
	}
}
