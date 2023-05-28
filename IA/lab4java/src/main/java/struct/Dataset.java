package struct;

import java.util.ArrayList;

/**
 * 
 * Class dataset, used to store all the data. 
 * @author chloe
 *
 */
public class Dataset {
	
	private ArrayList<ArrayList<Double>> inputValues;
	private ArrayList<Double> errorWanted; // or label
	
	/**
	 * Constructor of a dataset, created empty list of inputValues, errorWanted.
	 */
	public Dataset() {
		this.inputValues = new ArrayList<ArrayList<Double>>();
		this.errorWanted = new ArrayList<Double>();
	}

	/**
	 * @return the inputValues
	 */
	public ArrayList<ArrayList<Double>> getInputValues() {
		return inputValues;
	}

	/**
	 * @return the errorWanted
	 */
	public ArrayList<Double> getErrorWanted() {
		return errorWanted;
	}

	/**
	 * @param inputValues the inputValues to set
	 */
	public void setInputValues(ArrayList<ArrayList<Double>> inputValues) {
		this.inputValues = inputValues;
	}

	/**
	 * @param errorWanted the errorWanted to set
	 */
	public void setErrorWanted(ArrayList<Double> errorWanted) {
		this.errorWanted = errorWanted;
	}

	@Override
	public String toString() {
		return "Dataset [inputValues=" + inputValues + ", errorWanted=" + errorWanted + "]";
	}
	
	
}
