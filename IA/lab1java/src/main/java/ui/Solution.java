package ui;

import java.io.IOException;

import utils.Parser;

public class Solution {

	public static void startAlgorithm(String[] args) throws IOException {

		String nameFile = null;
		String nameHeurFile = null;
		String alg = null;
		boolean checkOpt = false;
		boolean checkCons = false;
		
		String desc = "";

		int i = 0;
		while (i < args.length) {
			// Nodes file
			if (args[i].equalsIgnoreCase("--ss")) {
				nameFile = args[i + 1];
			}
			// Heuristic file
			else if (args[i].equalsIgnoreCase("--h")) {
				nameHeurFile = args[i + 1];
			}
			// Algorithm used
			else if (args[i].equalsIgnoreCase("--alg")) {
				alg = args[i + 1];
			}
			// Checking if heuristic is optimistic
			else if (args[i].equalsIgnoreCase("--check-optimistic")) {
				checkOpt = true;
			}
			// Checking if heuristic is consistent
			else if (args[i].equalsIgnoreCase("--check-consistent")) {
				checkCons = true;
			}
			i++;
		}

		Parser p = new Parser();
		if (!nameFile.equalsIgnoreCase("3x3_puzzle.txt"))
			p.read(nameFile, nameHeurFile);
		else {
			System.out.println("3x3_puzzle is not yet implemented !");
			return;
		}

		// We use an algorithm
		if (alg != null) {
			// BFS
			if (alg.equalsIgnoreCase("bfs")) {
				desc += "# BFS";
				System.out.println(desc);
				BFS.BFS(p.getGraph());
			}
			// UCS
			else if (alg.equalsIgnoreCase("ucs")) {
				desc += "# UCS";
				System.out.println(desc);
				UCS.UCS(p.getGraph());
			}
			// AStar
			else if (alg.equalsIgnoreCase("astar")) {
				desc += "# A-STAR "+nameHeurFile;
				System.out.println(desc);
				AStar.ASTAR(p.getGraph(), true);
			}
		}
		
		// We check if it is consistent or optimistic
		else {
			if (checkOpt) {
				desc += "# HEURISTIC-OPTIMISTIC "+nameHeurFile;
				System.out.println(desc);
				Optimistic.optimistic(p.getGraph());
			}
			else {
				desc += "# HEURISTIC-CONSISTENT "+nameHeurFile;
				System.out.println(desc);
				Consistent.consistent(p.getGraph());
			}
		}

	}

	public static void main(String[] args) throws Exception {

		startAlgorithm(args);
		/**
		 * Parser p = new Parser(); p.read("istra.txt", null);
		 * 
		 * UCS.UCS(p.getGraph());
		 **/

	}

}
