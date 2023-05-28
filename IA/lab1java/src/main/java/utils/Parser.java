package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import graph.Graph;

/**
 * 
 * Class parser, used to read all kind of files corresponding to a graph. 
 * 
 * @author chloe
 *
 */
public class Parser {
	
	private Graph graph;

	/**
	 * Creates a graph. 
	 */
	public Parser() {
		this.graph = new Graph();
	}
	
	/**
	 * Read one heuristic and add it to the graph. 
	 * @param line Line of the heuristic. 
	 */
	public void readOneHeuristic(String line) {
		
		// In cityAndHeur we have a node and its heuristic value that we'll add to the graph
		String cityAndHeur[] = line.split(":");
					
		String value = cityAndHeur[1];
		
		// Delete's first space
		value = value.substring(1);
		
		graph.addNewNodeHeur(cityAndHeur[0], Float.parseFloat(value));	
		
	}
	
	@SuppressWarnings("resource")
	/**
	 * Read all the heuristics.
	 * @param file Line of the heuristic.
	 * @throws IOException Exception if there is an error. 
	 */
	public void readAllHeuristic(String file) throws IOException {
		
		String nameFile = "src/main/resources/" + file;
		
		File fileDir = new File(nameFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
			
		String line;
		
		// All lines of the file
		while((line = in.readLine()) != null) {
				
			String data = line.toString();
				
			// Line in file is not a comment (starting by "#")
			if (data.charAt(0) != '#') {
				// Adding all nodes and heuristics values to graph
				//System.out.println("Data "+data);
				readOneHeuristic(data);
			}
		}
	}
	
	/**
	 * Read the initial state. 
	 * @param line Line of the initial state. 
	 */
	public void readInitialState(String line) {
		graph.Node n = new graph.Node(line);
		graph.setInitialState(n);
	}
	
	/**
	 * Read all of the goal states. 
	 * @param line Line of the goal states. 
	 */
	public void readGoalStates(String line) {
				
		// We can have multiple goal states separated by a single " "
		String cities[] = line.split(" ");
		
		// For all of those goal states
		for (int i = 0; i < cities.length; i++) {

			int y = 0; 
			boolean found = false;
			while (!found && y < graph.getStates().size()) {
				if (graph.getStates().get(y).getName().equals(cities[i])) {
					found = true;
				} else {
					y++;
				}
			}
			if (found) {
				graph.getGoalStates().add(graph.getStates().get(y));
			} else {
				graph.Node n = new graph.Node(cities[i]);
				graph.addNewNode(cities[i]);
				graph.getGoalStates().add(n);
			}
		}
	}
	
	/**
	 * Read one transition. 
	 * @param line Line of the transition. 
	 */
	public void readTransition(String line) {
				
		// In cityAndReference[0] we have the node we will add transitions to,
		// And in cityAndReference[1] all the transitions
		String cityAndReference[] = line.split(":");
			
		//It can happen that there's no destination for a node
		if (cityAndReference.length > 1) {
			String transitions = cityAndReference[1];
			
			// Delete's first space
			transitions = transitions.substring(1);
			
			// We now have all the transitions to add
			// Each of them are separated by a " ". 
			String reference[] = transitions.split(" ");
			
			if (graph.checkIfExistByName(cityAndReference[0]) == null) {
				graph.addNewNode(cityAndReference[0]);
			}
			
			if (graph.checkIfExistByName(cityAndReference[0]) != null) {
				
				for (int i = 0; i < reference.length; i++) {
					
					String nameDest;
					float weightDest;
					
					// For each transition we have the destination node and the weight separated 
					// by a single ",".
					String oneTransition[] = reference[i].split(",");
				
					// the destination node
					nameDest = oneTransition[0];
					
					// the weight
					weightDest = Float.parseFloat(oneTransition[1]);
					
					if (graph.checkIfExistByName(nameDest) == null) {
						graph.addNewNode(nameDest);
					}
					
					// If a node does not already exist, this method "cityExists" will create it 
					// or return it if this node exist in the graph. 
					graph.Node dest = graph.checkIfExistByName(nameDest);
					
					if (dest != null ) {
						//We add the transition to the graph
						graph.checkIfExistByName(cityAndReference[0]).addTransition(dest, weightDest);
					}
				}
			}
		}
		
	}
	
	@SuppressWarnings("resource")
	/**
	 * Read all the states contained in a file. 
	 * @param fileName Name of file. 
	 * @throws IOException Excpetion if there is an error. 
	 */
	public void readAllStates(String fileName) throws IOException {
		
		String nameFile = "src/main/resources/"+fileName;
		
		// Open file
		File fileDir = new File(nameFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
			
		String line;
		int i = 0;
		
		
		// All lines
		while((line = in.readLine()) != null) {
				
			String data = line.toString();
				
			// Line in file is not a comment (starting by "#")
			if (data.charAt(0) != '#') {
				if (i == 0) {
					readInitialState(data);
				} else if (i == 1) {
					readGoalStates(data);					
				} else {
					readTransition(data);
				}
				i++;
			}
		}
	}
	
	/**
	 * Read all the files given. 
	 * @param nodeFile File of nodes. 
	 * @param heuristicFile Heuristic file. 
	 * @throws IOException Exception if something happened while opening the files. 
	 */
	public void read(String nodeFile, String heuristicFile) throws IOException {
		// It is possible that there's no heuristic file given
		if (heuristicFile != null ) {	
			readAllHeuristic(heuristicFile);
			readAllStates(nodeFile);
		} else {
			readAllStates(nodeFile);
		}
	}

	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @param graph the graph to set
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	
}
