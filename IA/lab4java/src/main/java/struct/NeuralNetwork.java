package struct;

import java.util.ArrayList;

import utils.Tools;

/**
 * 
 * Class neural network, represents a neural network with its hidden layers of neurons which all contains randomly generated weights. 
 * This neural network can be instantiated using the train & test datasets, the structure of the neural network 
 * (e.g. 5s5s for 2 hidden layers with 5 neurons each).
 * It also provides a method to compute the forward pass on the network. 
 * @author chloe
 *
 */
public class NeuralNetwork {

	private ArrayList<HiddenLayer> hiddenLayers;
	private Double error;
	private Dataset datasetTrain;
	private Dataset datasetTest;
	private ArrayList<Double> y;
	private Double fitness;
	private String structure;

	/**
	 * Constructor of a neural network, created an empty list of hidden layers.
	 */
	public NeuralNetwork(Dataset datasetTrain, Dataset datasetTest, String structure) {
		this.hiddenLayers = new ArrayList<HiddenLayer>();
		this.y = new ArrayList<Double>();
		this.error = 0.0;
		this.fitness = 0.0;
		this.datasetTrain = datasetTrain;
		this.datasetTest = datasetTest;
		this.structure = structure;
		createTables(structure);
	}

	/**
	 * Method used to instantiate randomly generated weight for our neural network using the normal law. 
	 * @param myStructure Structure of the network (e.g. 5s5s for 2 hidden layers with 5 neurons each).
	 */
	public void createTables(String myStructure) {

		String nbNeurons[] = myStructure.split("s");

		int nbWeight = this.datasetTest.getInputValues().get(0).size();

		for (int i = 0; i < nbNeurons.length; i++) {
			int nbneur = Integer.parseInt(nbNeurons[i]);

			HiddenLayer h = new HiddenLayer(nbneur);

			if (i == 0) {
				h.setWeights(new Double[nbneur][nbWeight + 1]);
				h.setNbWeightByNeuron(nbWeight + 1);
			} else {
				// The number of weights for a neuron of a hidden layer is equal to
				int nbWeightsPreLayer = this.getHiddenLayers().get(this.getHiddenLayers().size() - 1).getNbNeuron();
				h.setWeights(new Double[nbneur][nbWeightsPreLayer + 1]);
				h.setNbWeightByNeuron(nbWeightsPreLayer + 1);
			}
			this.getHiddenLayers().add(h);
		}
	}

	/**
	 * Method to initialize the weights of a neural network.
	 */
	public void initializeWeights() {
		for (int i = 0; i < this.hiddenLayers.size(); i++)
			for (int y = 0; y < this.hiddenLayers.get(i).getWeights().length; y++)
				for (int z = 0; z < this.hiddenLayers.get(i).getWeights()[y].length; z++)
					this.hiddenLayers.get(i).setAWeight(y, z, Tools.gaussian(0.01));
	}

	/**
	 * Add a given value to all the weight of a network under a given probability.
	 * 
	 * @param hiddenLayer       Index of the hidden layer
	 * @param x                 N° of neuron of the hidden layer
	 * @param y                 W° of a neuron of a hidden layer
	 * @param value             Value to add
	 * @param standardDeviation Standard deviation of the gaussian law.
	 * @param probability       Probability under the addition will be made.
	 */
	public void addValueToAWeightUnderProba(int hiddenLayer, int x, int y, Double standardDeviation,Double probability) {
		// Gausian noise
		double res = Tools.gaussian(standardDeviation);

		// Random
		double prob = Math.random();
		if (prob < probability)
			this.hiddenLayers.get(hiddenLayer).setAWeight(x, y,this.getHiddenLayers().get(hiddenLayer).getWeights()[x][y] + res);
	}

	/**
	 * Execute the forward pass on the current neural network.
	 * @param indexInDataset Index of the input values in the train / test dataset. 
	 */
	public void forwardPassCompute(int indexInDataset) {
		for (int i = 0; i < this.getHiddenLayers().size(); i++) {
			// It's the first hidden layer, we will use the weight of the dataset
			if (i == 0) {
				// For every neuron
				for (int neuron = 0; neuron < this.getHiddenLayers().get(i).getNbNeuron(); neuron++) {
					Double tmpVal = 0.0;
					for (int neuronValue = 0; neuronValue < this.getDatasetTrain().getInputValues().get(indexInDataset)
							.size(); neuronValue++) {
						Double valDataset = this.getDatasetTrain().getInputValues().get(indexInDataset)
								.get(neuronValue);
						tmpVal += valDataset * this.getHiddenLayers().get(i).getWeights()[neuron][neuronValue + 1];
					}
					// Adding bias
					tmpVal += this.getHiddenLayers().get(i).getWeights()[neuron][0];
					// Computing the transfer function
					tmpVal = Tools.sigmoid(tmpVal);
					// Adding the value to the weight used for the forward pass
					this.getHiddenLayers().get(i).getWeightsForwardPass().add(tmpVal);
				}
			} else {
				// For every neuron
				for (int neuron = 0; neuron < this.getHiddenLayers().get(i).getNbNeuron(); neuron++) {
					Double tmpVal = 0.0;
					// For every preceding hidden layer values
					for (int hiddenLayerValues = 0; hiddenLayerValues < this.getHiddenLayers().get(i - 1)
							.getWeightsForwardPass().size(); hiddenLayerValues++) {
						for (int weightOfNeuron = 0; weightOfNeuron < this.getHiddenLayers().get(i)
								.getNbWeightByNeuron() - 1; weightOfNeuron++) {
							Double test = this.getHiddenLayers().get(i).getWeights()[neuron][weightOfNeuron + 1];
							tmpVal += this.getHiddenLayers().get(i - 1).getWeightsForwardPass().get(hiddenLayerValues)
									* test;
						}
					}
					// Adding bias
					tmpVal += this.getHiddenLayers().get(i).getWeights()[neuron][0];
					// It is the last hidden layer, we do not compute any sigmoid
					// We add the value to the y values
					if (i == this.getHiddenLayers().size() - 1) {
						this.y.add(tmpVal);
					} else {
						// Computing the transfer function
						tmpVal = Tools.sigmoid(tmpVal);
						// Adding the value to the weight used for the forward pass
						this.getHiddenLayers().get(i).getWeightsForwardPass().add(tmpVal);
					}
				}
			}
		}
	}

	/**
	 * After each iteration of the forward pass, we clear the weights that have been stored. 
	 */
	public void clearWeightForwardPass() {
		for (int i = 0; i < this.getHiddenLayers().size(); i++)
			this.getHiddenLayers().get(i).getWeightsForwardPass().clear();
	}

	/**
	 * Computes the forward pass for each input of the train dataset.
	 */
	public void forwardPass() {
		for (int i = 0; i < this.getDatasetTrain().getInputValues().size(); i++) {
			forwardPassCompute(i);
			clearWeightForwardPass();
		}
		error();
		this.y.clear();
	}

	/**
	 * Computes the error for all input values. 
	 */
	public void error() {
		Double error = 0.0;
		for (int i = 0; i < this.y.size(); i++) {
			error += Math.pow((this.getDatasetTrain().getErrorWanted().get(i) - this.y.get(i)), 2);
		}
		error /= this.y.size();
		this.setError(error);
		this.setFitness(1 / error);
	}

	/**
	 * @return the hiddenLayers
	 */
	public ArrayList<HiddenLayer> getHiddenLayers() {
		return hiddenLayers;
	}

	/**
	 * @return the error
	 */
	public Double getError() {
		return error;
	}

	/**
	 * @param hiddenLayers the hiddenLayers to set
	 */
	public void setHiddenLayers(ArrayList<HiddenLayer> hiddenLayers) {
		this.hiddenLayers = hiddenLayers;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(Double error) {
		this.error = error;
	}

	/**
	 * @return the datasetTrain
	 */
	public Dataset getDatasetTrain() {
		return datasetTrain;
	}

	/**
	 * @return the datasetTest
	 */
	public Dataset getDatasetTest() {
		return datasetTest;
	}

	/**
	 * @param datasetTrain the datasetTrain to set
	 */
	public void setDatasetTrain(Dataset datasetTrain) {
		this.datasetTrain = datasetTrain;
	}

	/**
	 * @param datasetTest the datasetTest to set
	 */
	public void setDatasetTest(Dataset datasetTest) {
		this.datasetTest = datasetTest;
	}

	/**
	 * @return the y
	 */
	public ArrayList<Double> getY() {
		return y;
	}

	/**
	 * @return the fitness
	 */
	public Double getFitness() {
		return fitness;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(ArrayList<Double> y) {
		this.y = y;
	}

	/**
	 * @param fitness the fitness to set
	 */
	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

	/**
	 * @return the structure
	 */
	public String getStructure() {
		return structure;
	}

	/**
	 * @param structure the structure to set
	 */
	public void setStructure(String structure) {
		this.structure = structure;
	}

	@Override
	public String toString() {
		return "NeuralNetwork =[ Error=" + error + " ,Fitness=" + fitness + " ]";
	}

}
