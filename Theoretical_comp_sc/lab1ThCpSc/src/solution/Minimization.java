package solution;

import java.io.IOException;

import utils.Parser;

public class Minimization {
	
	/**
	 * Run the minimization algorithm on all the arguments given to the main.
	 * @param args Files to be read.
	 * @throws IOException Exception while reading the file.
	 */
	public static void runMinimization(String[] args) throws IOException {
		for (int i = 0; i < args.length; i++) {
			Parser p = new Parser();
			p.read(args[i]);

			ListState mini = new ListState();

			mini.displayOnlyMinimized(p.getDfa());
		}
		
	}
	
	/**
	 * Run the project.
	 * @param args Files to be read.
	 * @throws Exception Exception while reading the file.
	 */
	public static void main(String[] args) throws Exception {
		
		runMinimization(args);

	}

}
