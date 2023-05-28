package structure;

/**
 * 
 * Class Stack, creates new stack symbols defined by their names.
 * @author chloe
 *
 */
public class StackSymbols {
	
	private String name;

	/**
	 * Creates a new stack symbol with a specific name.
	 * @param name Name of the stack symbol.
	 */
	public StackSymbols(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) 
            return true;
        
		if (!(obj instanceof StackSymbols)) 
	            return false;
	    
		StackSymbols s = (StackSymbols)obj;
		return this.getName().equals(s.getName());
	}

	@Override
	public String toString() {
		return "Stack [name=" + name + "]";
	}
}
