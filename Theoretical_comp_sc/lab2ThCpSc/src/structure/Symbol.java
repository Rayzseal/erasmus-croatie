package structure;

/**
 * 
 * Class symbol, a symbol is defined by its name.
 * @author chloe
 *
 */
public class Symbol {
	
	private String name;

	/**
	 * Creates a new symbol with a specific name.
	 * @param name Name of the symbol.
	 */
	public Symbol(String name) {
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
        
		if (!(obj instanceof Symbol)) 
	            return false;
	    
		Symbol s = (Symbol)obj;
		return this.getName().equals(s.getName());
	}

	@Override
	public String toString() {
		return "Symbol [name=" + name + "]";
	}
}
