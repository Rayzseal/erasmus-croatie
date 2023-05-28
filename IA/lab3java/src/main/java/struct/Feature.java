package struct;

import java.util.ArrayList;

/**
 * 
 * Class feature, defined by a name and all of the values using this feature. 
 * @author chloe
 *
 */
public class Feature {
	
	private String name;
	private ArrayList<Value> values;
	
	/**
	 * Constructor of a feature. 
	 * Creates a new feature with a given name and an empty list of values. 
	 * @param name Name of the feature. 
	 */
	public Feature(String name) {
		this.name = name;
		this.values = new ArrayList<Value>();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the values
	 */
	public ArrayList<Value> getValues() {
		return values;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(ArrayList<Value> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Feature [name=" + name +"]";
	}

}
