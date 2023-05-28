package tree;

import java.util.ArrayList;

import struct.Label;
import struct.Value;

/**
 * 
 * Class node, used to create node that contains a value, a label (only if it is a leaf), and a parent node 
 * (only if it is not a root node), and the list of children node associated. 
 * @author chloe
 *
 */
public class Node {
	
	private Value value;
	private Label label;
	private Node parent;
	private ArrayList<Node> children;
	
	/**
	 * @param value Value of the node. 
	 * @param parent Node parent. 
	 */
	public Node(Value value, Node parent) {
		this.value = value;
		this.parent = parent;
		this.label = null;
		this.children = new ArrayList<Node>();
	}
	
	/**
	 * Constructor of a node (to create a root node). 
	 * @param value Value of the node. 
	 */
	public Node(Value value) {
		this.value = value;
		this.parent = null;
		this.children = new ArrayList<Node>();
	}

	/**
	 * @return the value
	 */
	public Value getValue() {
		return value;
	}

	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @return the children
	 */
	public ArrayList<Node> getChildren() {
		return children;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Value value) {
		this.value = value;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}
	
	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(Label label) {
		this.label = label;
	}
	
	/**
	 * For a given node, computes the size of a tree. 
	 * @return The depth of the tree. 
	 */
	public int getSize() {
		int res = 1;
		Node n = this;
		Node n2 = this;
		while (!n.getChildren().isEmpty()) {
			n = n.getChildren().get(0);
			res++;
		}
		while(n2.getParent() != null) {
			n2 = n2.getParent();
			res++;
		}
		return res;
	}

	@Override
	public String toString() {
		if (parent!=null)
			return "Node [value=" + value + ", feature="+ value.getFeature().getName() +", parent=" + parent.getValue().getFeature().getName() +"]";
		else
			return "Node [value=" + value + ", feature="+ value.getFeature().getName() +"]";
	}
}
