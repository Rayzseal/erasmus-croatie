package struct;

/**
 * 
 * Class value, is defined by a name and the feature needed to obtain this value. 
 * @author chloe
 *
 */
public class Value {
	
	private String name;
	private Feature feature;
	
	/**
	 * Constructor of a value. 
	 * Created a value with a given name. 
	 * @param name Name of the value. 
	 */
	public Value(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the feature
	 */
	public Feature getFeature() {
		return feature;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param feature the feature to set
	 */
	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	@Override
	public String toString() {
		return "Value [name=" + name + ", feature=" + feature.getName() + "]";
	}
	
	@Override
    public boolean equals(Object o) {
        Value v;
        if(!(o instanceof Value)) {
            return false;
        } else {
            v = (Value) o;
            if(this.name.equals(v.getName())) {
                return true;
            }
        }
        return false;
    }
}
