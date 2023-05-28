package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import structure.DFA;
import structure.State;
import structure.Symbol;

public class Parser {

	private DFA dfa;

	public Parser() {
		this.dfa = new DFA();
	}

	/**
	 * @return the dfa
	 */
	public DFA getDfa() {
		return dfa;
	}



	/**
	 * @param dfa the dfa to set
	 */
	public void setDfa(DFA dfa) {
		this.dfa = dfa;
	}



	public void readStates(String line) {
		String parser[] = line.split(": ");
		String states[] = parser[1].split(",");

		for (int i = 0; i < states.length; i++) {
			State s = new State(states[i]);
			this.dfa.addState(s);
		}
	}

	public void readSymbols(String line) {
		String parser[] = line.split(": ");
		String symbols[] = parser[1].split(",");

		for (int i = 0; i < symbols.length; i++) {
			Symbol s = new Symbol(symbols[i]);
			this.dfa.addSymbol(s);
		}
	}

	public void readAcceptingStates(String line) {
		String parser[] = line.split(": ");
		String states[] = parser[1].split(",");

		for (int i = 0; i < states.length; i++)
			if (dfa.getStateByString(states[i]) != null)
				this.dfa.addAcceptingStates(dfa.getStateByString(states[i]));
	}

	public void readInitialState(String line) {
		String parser[] = line.split(": ");
		String states[] = parser[1].split(",");

		for (int i = 0; i < states.length; i++)
			if (dfa.getStateByString(states[i]) != null)
				this.dfa.setInitialState(dfa.getStateByString(states[i]));
	}

	public void readTransition(String line) {
		String leftToRight[] = line.split("->");
		String originAndSymbol[] = leftToRight[0].split(",");
		
		dfa.getSymbolByString(originAndSymbol[1]).add(dfa.getStateByString(originAndSymbol[0]), dfa.getStateByString(leftToRight[1]));
		
		
		/**
		Transitions t = new Transitions(dfa.getSymbolByString(originAndSymbol[1]),
				dfa.getStateByString(leftToRight[1]));
		dfa.getStateByString(originAndSymbol[0]).getTransitions().add(t);
		**/
	}

	public void read(String dfaFile) throws IOException {
		String nameFile = "src/resources/" + dfaFile;

		File fileDir = new File(nameFile);
		
		@SuppressWarnings("resource")
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));

		String line;

		int i = 0;

		// All lines of the file
		while ((line = in.readLine()) != null) {

			String data = line.toString();

			// Line in file is not a comment (starting by "#")
			if (data.charAt(0) != '#') {
				if (i == 0) {
					readStates(line);
				}
				if (i == 1) {
					readSymbols(line);
				}
				if (i == 2) {
					readAcceptingStates(line);
				}
				if (i == 3) {
					readInitialState(line);
				}
				if (i == 4) {
					// Nothing is happening here
				}
				if (i > 4 && !line.equals("----------")) {
					readTransition(line);
				}
				if (i > 4 && line.equals("----------")) {
					break;
				}
				i++;
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		String nameFile = "simple_dfa_1.txt";
		
		Parser p = new Parser();
		p.read(nameFile);
		
		p.dfa.displayDFA();
		/**
		System.out.println("p1 == p3");
		
		int index = p.dfa.getStates().indexOf(p.dfa.getStateByString("p1"));
		p.dfa.getStates().set(index, p.dfa.getStates().get(3));
		for (int i = 0; i < p.dfa.getStates().size(); i++) {
			for (int j = 0; j < p.dfa.getStates().get(i).getTransitions().size(); j++) {
				if ( p.dfa.getStates().get(i).getTransitions().get(j).getReachState().getName().equals("p1")) {
					p.dfa.getStates().get(i).getTransitions().get(j).setReachState(p.dfa.getStates().get(3));
				}
			}
		}
		p.dfa.displayDFA();
		**/
	}

}
