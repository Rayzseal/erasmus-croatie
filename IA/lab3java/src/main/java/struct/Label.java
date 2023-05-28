package struct;

import java.util.ArrayList;

/**
 * 
 * Class label, contains the name of the label, and a list of values needed to obtain this label. 
 * @author chloe
 *
 */
public class Label {
	
	private String name;
	private ArrayList<Value> values;
	
	/**
	 * Constructor of a label, created a label with a given name.
	 * @param name Name of the label to be created. 
	 */
	public Label(String name) {
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
	
	/**
	 * Return the value at which a feature has been found (if it exist)
	 * @param feature Feature we are looking for. 
	 * @return The value where the feature has been found. 
	 */
	public Value getLabelWithFeature(Feature feature) {
		for (int i = 0; i < this.values.size(); i++) {
			if (this.values.get(i).getFeature().getName() == feature.getName()) {
				return this.values.get(i);
			}
		}
		return null;		
	}

	@Override
	public String toString() {
		return "Label [name=" + name + ", values=" + values + "]";
	}
	
	@Override
    public boolean equals(Object o) {
        Label l;
        if(!(o instanceof Label)) {
            return false;
        } else {
            l = (Label) o;
            if(this.name.equals(l.getName())) {
                return true;
            }
        }
        return false;
    }
}
