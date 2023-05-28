package ui;

import java.util.ArrayList;
import java.util.Random;

import struct.Dataset;
import struct.NeuralNetwork;

/**
 * 
 * Class genetic algorithm, execute this algorithm with a list of hyperparameters needed, like elitism, population size,
 * the number of iterations that need to be done... etc.
 * It prints the train error results every 2000 iterations and in the end the test error result. 
 * @author chloe
 *
 */
public class GeneticAlgorithm {
	
	private int popsize, elitism , iter;
	private double probability, k, totalFitness, totalFitnessTmp;
	private ArrayList<NeuralNetwork> population;
	private Dataset datasetTrain, datasetTest;
	private String structure;
	
	/**
	 * Constructor of the genetic algorithm, instantiate an object with all required data. 
	 * @param popsize Population size.
	 * @param elitism Number of best elements to be carried over each iteration. 
	 * @param iter Number of iteration to be done.
	 * @param probability Probability on which each element will mutate. 
	 * @param k standard deviation to be used for mutation.
	 * @param datasetTrain Train dataset.
	 * @param datasetTest Test dataset.
	 * @param structure Structure to be used (e.g. 5s5s for 2 hidden layers with 5 neurons each)
	 */
	public GeneticAlgorithm(int popsize, int elitism, int iter, double probability, double k, Dataset datasetTrain, Dataset datasetTest, String structure) {
		this.population = new ArrayList<NeuralNetwork>();  
		this.popsize = popsize;
		this.datasetTest = datasetTest;
		this.datasetTrain = datasetTrain;
		this.elitism = elitism;
		this.iter = iter;
		this.probability = probability;
		this.k = k;
		this.totalFitness = 0.0;
		this.totalFitnessTmp = 0.0;
		this.structure = structure;
	}
	
	/**
	 * Creates a new neural network with required contents. 
	 * @param datasetTrain The train dataset of this neural network. 
	 * @param datasetTest The test dataset of this neural network. 
	 * @param structure The structure of this neural network (e.g. 5s5s for 2 hidden layers with 5 neurons each)
	 * @return The newly created neural networks with random weights and forward pass completed.
	 */
	public NeuralNetwork createNeuralNetwork(Dataset datasetTrain, Dataset datasetTest, String structure) {
		NeuralNetwork n = new NeuralNetwork(datasetTrain,datasetTest,structure);
		n.initializeWeights();
		n.forwardPass();
		this.totalFitness += n.getFitness(); 
		return n;
	}
	
	/**
	 * Select 2 parents in our population. 
	 * It is based on the wheel of selection process. 
	 * So basically, we will create 2 random numbers, and check if they are in the interval of probability of a certain neural network.
	 * Neural network probabilities are made such that those with higher fitness will have a better chance to appear than lower ones.
	 * So that, in the end, the best one have much more chance to be selected than the others. 
	 * @return The 2 parents randomly selected by the wheel of probability. 
	 */
	public ArrayList<NeuralNetwork> select() {
		Double firstRandom = this.totalFitness * new Random().nextDouble();
		Double secondRandom = this.totalFitness * new Random().nextDouble();

		Double valueTotal = 0.0;
		ArrayList<NeuralNetwork> selected = new ArrayList<NeuralNetwork>();
		int i = 0; 
		while (i < popsize) {
			valueTotal += this.population.get(i).getFitness();
			if (firstRandom < valueTotal) {
				selected.add(this.population.get(i));
				firstRandom = Double.MAX_VALUE;
			}
			if (secondRandom < valueTotal) {
				selected.add(this.population.get(i));
				secondRandom = Double.MAX_VALUE;
			}
			// We selected 2 parents
			if (selected.size() == 2)
				break;
			i++;
		}
		return selected;
	}
	
	/**
	 * Crosser between 2 neural networks. 
	 * Basically, we will compute the arithmetic mean element-wise between all the weight of the 2 given neural networks.
	 * @param n1 First neural network.
	 * @param n2 Second neural network.
	 * @return A newly created neural network which contains the arithmetic mean of the weights of the neural networks n1 & n2. 
	 */
	public NeuralNetwork crossover(NeuralNetwork n1, NeuralNetwork n2) {
		NeuralNetwork n3 = new NeuralNetwork(datasetTrain,datasetTest,n1.getStructure());
		for (int i = 0; i < n1.getHiddenLayers().size(); i++) {
			for (int neuron = 0; neuron < n1.getHiddenLayers().get(i).getNbNeuron(); neuron++) {
				for (int neuronWeight = 0; neuronWeight < n1.getHiddenLayers().get(i).getNbWeightByNeuron(); neuronWeight++) {
					// Arithmetic mean
					Double tmp =  (n1.getHiddenLayers().get(i).getWeights()[neuron][neuronWeight] + n2.getHiddenLayers().get(i).getWeights()[neuron][neuronWeight])/2;
					n3.getHiddenLayers().get(i).getWeights()[neuron][neuronWeight] = tmp;
				}
			}
		}
		return n3;
	}
	
	/**
	 * Mutate all weights of a neural network under a given probability with a given standard deviation.
	 * @param n Neural network which will mutate.
	 * @return The mutated neural network.
	 */
	public NeuralNetwork mutation(NeuralNetwork n) {
		for (int i = 0; i < n.getHiddenLayers().size(); i++) 
			for (int neuron = 0; neuron < n.getHiddenLayers().get(i).getNbNeuron(); neuron++) 
				for (int neuronWeight = 0; neuronWeight < n.getHiddenLayers().get(i).getNbWeightByNeuron(); neuronWeight++) 
					n.addValueToAWeightUnderProba(i, neuron, neuronWeight, k, probability);
		return n;
	}
	
	/**
	 * Computes the elitism, basically finds out the n best neural networks of our population. 
	 * @param elitismVal Number of best neural networks we want to find.
	 * @param add Wether the fitness should be added to the total fitness or not.
	 * @return An list that contains all the best neural networks of our current population.
	 */
	public ArrayList<NeuralNetwork> elitism(int elitismVal, boolean add) {
		ArrayList<NeuralNetwork> pPrime = new ArrayList<NeuralNetwork>();
		ArrayList<NeuralNetwork> popTmp = new ArrayList<NeuralNetwork>();
		
		popTmp.addAll(population);
		while (pPrime.size() < elitismVal) {
			double maxVal = Double.MIN_VALUE;
			int index = 0;
			for (int i = 0; i < popTmp.size(); i++) {
				if (popTmp.get(i).getFitness() > maxVal) {
					maxVal = popTmp.get(i).getFitness();
					index = i;
				}	
			}
			if (add)
				this.totalFitnessTmp += popTmp.get(index).getFitness();
			pPrime.add(popTmp.get(index));
			popTmp.remove(index);
		}
		return pPrime;	
	}
	
	/**
	 * Computes the error on the test dataset using the best model we found.
	 */
	public void computeError() {
		ArrayList<NeuralNetwork> bestNeuralNetwork = elitism(1,false);
		bestNeuralNetwork.get(0).setDatasetTrain(bestNeuralNetwork.get(0).getDatasetTest());
		bestNeuralNetwork.get(0).forwardPass();
		System.out.println("[Test error]: "+bestNeuralNetwork.get(0).getError());
	}
	
	/**
	 * Genetic algorithm.
	 * First we create a new empty population, that will use initial random weights.
	 * Then, for each iteration we create a new empty population P'
	 * we will keep the n best models in p',
	 * we will create 2 children using the wheel of selection in our parent population,
	 * those 2 children will become 1 using the arithmetic mean on all their weights,
	 * the given child will see his weights mutate under a given probability,
	 * we add this new mutated child to our population P' and we repeat this parent - child step until
	 * we reach popsize.
	 * Then the new population P' will replace the old one, and we do all of those steps all over.
	 */
	public void geneticAlgo() {
		// Number of iterations to be done
		int nbIter = 0; 
		int counter = 0;
		// Creates an initial population of size "popsize" with initialized neural networks
		for (int i = 0; i < popsize; i++) 
			population.add(createNeuralNetwork(datasetTrain,datasetTest,structure));
			
		// While we did not reach the stopping criterion
		while(nbIter <= iter) {
			// Next generation
			ArrayList<NeuralNetwork> pPrime = new ArrayList<NeuralNetwork>();
			// Adding the best units for the next generation
			pPrime = elitism(elitism,true);
			
			// Displaying results every 2000 iterations
			if (counter == 2000) {
				ArrayList<NeuralNetwork> test = elitism(1,false);
				display(test.get(0),nbIter);
				counter = 0;
			}	
			
			// Adding elements until we reach popsize
			while (pPrime.size() < this.popsize) {
				ArrayList<NeuralNetwork> parents = select();
				NeuralNetwork child = crossover(parents.get(0),parents.get(1));				
				mutation(child);
				child.forwardPass();
				this.totalFitnessTmp += child.getFitness();
				pPrime.add(child);
			}
			
			// Setting fitnesses for next iteration
			this.population = pPrime;
			this.totalFitness = totalFitnessTmp;
			this.totalFitnessTmp = 0.0;
			
			nbIter++;
			counter++;
		}
		// Computing the error on the test set in the end, using the best model we found.
		computeError();
	}
	
	/**
	 * Display the train error results
	 * @param n On which neural network.
	 * @param index Iteration n°.
	 */
	public void display(NeuralNetwork n, int index) {
		System.out.println("[Train error @" + index + "]: "+n.getError());
	}

}
