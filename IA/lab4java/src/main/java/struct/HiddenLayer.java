package struct;

import java.util.ArrayList;

/**
 * 
 * Class hidden layer, represents the hidden layer of a neural network. 
 * It basically contains the weights for a given number of neurons, and the weights generated during the forward pass. 
 * @author chloe
 *
 */
public class HiddenLayer {
	
	private int nbNeuron;
	private int nbWeightByNeuron;
	
	private Double weights[][];
	private ArrayList<Double> weightsForwardPass;
	
	/**
	 * Constructor of a hidden layer, it has a certain number of neurons.
	 * @param nbNeuron Number of neurons.
	 */
	public HiddenLayer(int nbNeuron) {
		this.nbNeuron = nbNeuron;
		this.weightsForwardPass = new ArrayList<Double>();
	}
	/**
	 * @return the nbNeuron
	 */
	public int getNbNeuron() {
		return nbNeuron;
	}
	/**
	 * @return the nbWeightByNeuron
	 */
	public int getNbWeightByNeuron() {
		return nbWeightByNeuron;
	}
	/**
	 * @return the weights
	 */
	public Double[][] getWeights() {
		return weights;
	}
	/**
	 * @return the weightsForwardPass
	 */
	public ArrayList<Double> getWeightsForwardPass() {
		return weightsForwardPass;
	}
	/**
	 * @param nbNeuron the nbNeuron to set
	 */
	public void setNbNeuron(int nbNeuron) {
		this.nbNeuron = nbNeuron;
	}
	/**
	 * @param nbWeightByNeuron the nbWeightByNeuron to set
	 */
	public void setNbWeightByNeuron(int nbWeightByNeuron) {
		this.nbWeightByNeuron = nbWeightByNeuron;
	}
	/**
	 * @param weights the weights to set
	 */
	public void setWeights(Double[][] weights) {
		this.weights = weights;
	}
	
	public void setAWeight(int x, int y, double weight) {
		this.weights[x][y] = weight;
	}
	/**
	 * @param weightsForwardPass the weightsForwardPass to set
	 */
	public void setWeightsForwardPass(ArrayList<Double> weightsForwardPass) {
		this.weightsForwardPass = weightsForwardPass;
	}
	
	/**
	 * Shows all the weights
	 * @return String with all weights. 
	 */
	public String displayWeights() {
		String res = "";
		for (int i = 0; i < this.weights.length; i++) {
			for (int y = 0; y < this.weights[i].length; y++)
				res += this.weights[i][y]+" ";
			res+= "\n";
		}
		return res;
	}
	
	@Override
	public String toString() {
		return "HiddenLayer [nbNeuron=" + nbNeuron + ", nbWeightByNeuron=" + nbWeightByNeuron + ", weights="
				+ displayWeights() + ", weightsForwardPass=" + weightsForwardPass + "]";
	}
}
