package tree;

import java.util.ArrayList;

import struct.Label;
import struct.Value;

/**
 * 
 * Class tree, used to store all of our root node (and their children) 
 * and the branches of this tree (used to display the tree).
 * @author chloe
 *
 */
public class Tree {
	
	private ArrayList<Node> root;
	private Branches branches;
	
	/**
	 * Constructor of a tree, basically creates empty list. 
	 */
	public Tree() {
		this.root = new ArrayList<Node>();
		this.branches = new Branches();
	}

	/**
	 * @return the root
	 */
	public ArrayList<Node> getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(ArrayList<Node> root) {
		this.root = root;
	}
		
	/**
	 * Method to display the tree in correct format. 
	 * @param node Node to be displayed. 
	 * @param cpt Counter (= depth of the tree for a given node)
	 * @param disp String to be displayed
	 * @param end If it is the last node, will also add the label value for this node. 
	 * @return A string to be displayed. 
	 */
	public String dispAdd(Node node, int cpt, String disp, boolean end) {
		if (disp == "")
			disp = cpt+":"+node.getValue().getFeature().getName()+"="+node.getValue().getName();
		else 
			disp +=" "+cpt+":"+node.getValue().getFeature().getName()+"="+node.getValue().getName();
		if (end)
			disp+=" "+node.getLabel().getName();
		return disp;
	}
	
	/**
	 * Recursive function to display all of the branches of our tree. 
	 * @param node Node to be displayed. 
	 * @param disp String containing what should be displayed. 
	 * @param cpt Counter (= depth of the tree for a given node)
	 */
	public void displayRecursive(Node node, String disp, int cpt) {
		if (node.getLabel()==null) {
			disp = dispAdd(node,cpt,disp,false);
			cpt++;
			for (int i = 0; i < node.getChildren().size(); i++)
				displayRecursive(node.getChildren().get(i),disp,cpt);
		} else {
			// Add disp label
			disp = dispAdd(node,cpt,disp,true);
			cpt++;
			//System.out.println(disp);
			this.branches.getBranches().add(disp);
		}
	}
	
	/**
	 * Display a tree. 
	 */
	public void display() {
		System.out.println("[BRANCHES]:");
		for (int i = 0; i < this.getRoot().size(); i++)
			displayRecursive(this.getRoot().get(i), "", 1);
		result();
	}
	
	/**
	 * Display all of the branches of a tree. 
	 */
	public void result() {
		for (int i = 0; i < this.branches.getBranches().size(); i++)
			System.out.println(this.branches.getBranches().get(i));
	}
	
	/**
	 * Adding a leaf to the tree.
	 * @param l Label to be added to a given node. 
	 * @param n Node which will become a leaf node of the tree. 
	 */
	public void addLeaf(Label l, Node n) {
		n.setLabel(l);
	}
	
	/**
	 * Adding a root node to the tree. 
	 * @param value Value of the root node to add. 
	 * @return The newly added root node. 
	 */
	public Node addRootNode(Value value) {
		Node n = new Node(value);
		this.root.add(n);
		return n;
	}
	
	/**
	 * Adding a node to the tree. 
	 * @param parent Parent node of the node which will be added. 
	 * @param value Value of the node which will be added. 
	 * @return The newly added node. 
	 */
	public Node addNode(Node parent, Value value) {
		Node n = new Node(value,parent);
		parent.getChildren().add(n);
		return n;
	}
	
	/**
	 * @return the branches
	 */
	public Branches getBranches() {
		return branches;
	}

	/**
	 * @param branches the branches to set
	 */
	public void setBranches(Branches branches) {
		this.branches = branches;
	}

	@Override
	public String toString() {
		return "Tree [root=" + root + "]";
	}
}
