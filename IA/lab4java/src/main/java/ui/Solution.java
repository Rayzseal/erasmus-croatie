package ui;

import java.io.IOException;

import utils.Parser;

/**
 * 
 * Class solution, basically launch the project.
 * @author chloe
 *
 */
public class Solution {
	
	/**
	 * Method the start the genetic algorithm with all needed hyper parameters. 
	 * @param args hyper parameters
	 * @throws IOException Exception while reading the input files. 
	 */
	public static void startAlgorithm(String[] args) throws IOException {
				
		String trainingFile = null;
		String testingFile = null;
		String structure = null;
		int popsize = 0, elitism = 0, iter = 0;
		double probability = 0, k = 0;
		
		
		// Reading all of the arguments given to the main
		for (int i = 0; i < args.length; i+=2) {
			if (args[i].equals("--train"))
				trainingFile = args[i+1];
			if (args[i].equals("--test"))
				testingFile = args[i+1];
			if (args[i].equals("--nn"))
				structure = args[i+1];
			if (args[i].equals("--popsize"))
				popsize = Integer.parseInt(args[i+1]);
			if (args[i].equals("--elitism"))
				elitism = Integer.parseInt(args[i+1]);
			if (args[i].equals("--p"))
				probability = Double.parseDouble(args[i+1]);
			if (args[i].equals("--K"))
				k = Double.parseDouble(args[i+1]);
			if (args[i].equals("--iter"))
				iter = Integer.parseInt(args[i+1]);
		}
		
		// Adding the output neuron
		structure += "1s";
		
		// Creating corresponding datasets
		Parser p = new Parser();
		p.read(trainingFile, testingFile, structure);
		
		// Executing the genetic algorithm
		GeneticAlgorithm geneticAlg = new GeneticAlgorithm(popsize, elitism, iter,probability, k, p.getDatasetTrain(), p.getDatasetTest(), structure);
		geneticAlg.geneticAlgo();
	}
	
	public static void main(String[] args) throws Exception {
		startAlgorithm(args);
	}

}
