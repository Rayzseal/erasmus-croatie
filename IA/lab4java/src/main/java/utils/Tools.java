package utils;

import java.util.Random;

/**
 * 
 * Tools class, contains generic version of easy to use functions. 
 * @author chloe
 *
 */
public class Tools {
	
	/**
	 * Return a random number using the normal law with a given standard deviation. 
	 * @param standardDeviation Standard deviation to be used. 
	 * @return A random number. 
	 */
	static public Double gaussian(Double standardDeviation) {
		return new Random().nextGaussian()*standardDeviation;
	}
	
	/**
	 * Computes the sigmoid. 
	 * @param x value of exponential
	 * @return The sigmoid value
	 */
	static public Double sigmoid(Double x) {
		return 1/(1+Math.exp(-x));
	}

}
