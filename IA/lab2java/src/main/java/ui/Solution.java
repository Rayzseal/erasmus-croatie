package ui;

import java.io.IOException;

import struct.Clause;
import struct.Element;
import utils.Parser;

/**
 * 
 * Class solution, it is the one used to use the program, it start the algorithm or execute user queries using 
 * files given into parameters. 
 * @author chloe
 *
 */
public class Solution {

	/**
	 * Method to start the appropriate algorithm using the arguments given into parameters of the main. 
	 * @param args Arguments of the main. 
	 * @throws IOException Exception while parsing the files. 
	 */
	public static void startAlgorithm(String[] args) throws IOException {

		String clausesFile = null;
		String cookingFile = null;
		boolean resolve = false;
		boolean cooking = false;

		int i = 0;

		// We will execute the refutation algorithm on a given file
		if (args[i].equalsIgnoreCase("resolution")) {
			clausesFile = args[i + 1];
			resolve = true;
		}

		// We are using a cooking file, which will add / delete and execute the
		// refutation algorithm
		if (args[i].equalsIgnoreCase("cooking")) {
			clausesFile = args[i + 1];
			cookingFile = args[i + 2];
			cooking = true;
		}

		// We will execute the refutation algorithm on a given file
		if (resolve) {
			Parser p = new Parser();
			p.read(clausesFile, null);
			PlResolvent.plResolution(p.getElement(),
					p.getElement().getClauses().get(p.getElement().getClauses().size() - 1).getContained());

		}

		// We are using a cooking file, which will add / delete and execute the
		// refutation algorithm
		if (cooking) {
			Parser p = new Parser();
			p.read(clausesFile, cookingFile);
			cooking(p.getElement());
		}
	}

	/**
	 * Method to execute the different queries of a user. 
	 * It can either be : adding a clause, deleting a clause or executing the 
	 * refutation algorithm given a particular goal state. 
	 * @param element Set of clauses to be used. 
	 */
	public static void cooking(Element element) {
		for (int i = 0; i < element.getWishes().size(); i++) {
			switch (element.getWishes().get(i).getWish()) {
			case ADD:
				element.getClauses().add(new Clause(element.getWishes().get(i).getClause()));
				break;
			case DELETE:
				for (int j = 0; j < element.getClauses().size(); j++) {
					if (element.getClauses().get(j).getContained().equals(element.getWishes().get(i).getClause()))
						element.getClauses().remove(j);
				}
				break;
			case WANT:
				Element tmp = new Element();
				tmp.getClauses().addAll(element.getClauses());
				tmp.getClauses().add(new Clause(element.getWishes().get(i).getClause()));
				PlResolvent.plResolution(tmp, tmp.getClauses().get(tmp.getClauses().size() - 1).getContained());
				break;
			}

		}
	}

	/**
	 * Launch the application. 
	 * @param args Arguments given to the main. 
	 * @throws Exception Exception while parsing the files. 
	 */
	public static void main(String[] args) throws Exception {
		startAlgorithm(args);
	}

}
