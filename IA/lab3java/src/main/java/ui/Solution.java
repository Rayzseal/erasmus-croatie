package ui;

import java.io.IOException;

import struct.Dataset;
import utils.Parser;

public class Solution {
	
	public static void startAlgorithm(String[] args) throws IOException {
		
		String trainingFile = null;
		String testingFile = null;
		int limitedDepth = Integer.MAX_VALUE;
		
		// Reading all of the arguments given to the main
		for (int i = 0; i < args.length; i++) {
			if (i == 0)
				trainingFile = args[i];
			if (i == 1)
				testingFile = args[i];
			if (i == 2)
				limitedDepth = Integer.parseInt(args[i]);
		}
		
		// Creating corresponding datasets
		Parser p = new Parser();
		p.read(trainingFile, testingFile);
		
		// Starting predict method (which will use id3 and display the results)
		new Predict(p.getDatasetTrain(),p.getDatasetTest(),limitedDepth);
		
	}
	
	public static void main(String[] args) throws Exception {
		startAlgorithm(args);
	}

}
