package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import structure.DPDA;
import structure.StackSymbols;
import structure.State;
import structure.Symbol;

public class Parser {

	private DPDA dpda;

	public Parser() {
		this.dpda = new DPDA();
		this.dpda.getSymbols().add(new Symbol("€"));
		this.dpda.getStackSymbols().add(new StackSymbols("€"));
	}

	/**
	 * @return the dpda
	 */
	public DPDA getDpda() {
		return dpda;
	}



	/**
	 * @param dpda the dpda to set
	 */
	public void setDpda(DPDA dpda) {
		this.dpda = dpda;
	}



	public void readStates(String line) {
		String parser[] = line.split(": ");
		String states[] = parser[1].split(",");

		for (int i = 0; i < states.length; i++) {
			State s = new State(states[i]);
			this.dpda.addState(s);
		}
	}

	public void readSymbols(String line) {
		String parser[] = line.split(": ");
		String symbols[] = parser[1].split(",");

		for (int i = 0; i < symbols.length; i++) {
			Symbol s = new Symbol(symbols[i]);
			this.dpda.addSymbol(s);
		}
	}
	
	public void readStackSymbols(String line) {
		String parser[] = line.split(": ");
		String stackSymbols[] = parser[1].split(",");
		
		for (int i = 0; i < stackSymbols.length; i++) {
			StackSymbols stack = new StackSymbols(stackSymbols[i]);
			this.dpda.addStack(stack);
		}
			
	}

	public void readAcceptingStates(String line) {

		String parser[] = line.split(": ");
		String states[] = parser[1].split(",");

		if (states[0].equalsIgnoreCase("empty")) {
			this.dpda.setAcceptByEmptyStack(true);
			this.dpda.setAcceptByFinalState(false);
		} else {
			this.dpda.setAcceptByEmptyStack(false);
			this.dpda.setAcceptByFinalState(true);
			for (int i = 0; i < states.length; i++)
				if (dpda.getStateByString(states[i]) != null)
					this.dpda.addAcceptingState(dpda.getStateByString(states[i]));
		}
	}

	public void readInitialState(String line) {
		String parser[] = line.split(": ");
		String states[] = parser[1].split(",");

		for (int i = 0; i < states.length; i++)
			if (dpda.getStateByString(states[i]) != null)
				this.dpda.setInitialState(dpda.getStateByString(states[i]));
	}
	
	public void readInitialStackSymbol(String line) {
		String parser[] = line.split(": ");
		String stack[] = parser[1].split(",");

		for (int i = 0; i < stack.length; i++)
			if (dpda.getStackByString(stack[i]) != null)
				this.dpda.setInitialStack(dpda.getStackByString(stack[i]));
	}
	
	public void readAcceptingString(String line) {
		String parser[] = line.split(": ");
		String parsed = parser[1];
		
		for (int i = 0; i < parsed.length(); i++)
			if (dpda.getSymbolByString(String.valueOf(parsed.charAt(i))) !=null)
				dpda.getAcceptingString().add(dpda.getSymbolByString(String.valueOf(parsed.charAt(i))));
	}

	public void readTransition(String line) {
		String leftToRight[] = line.split("=");
		String originAndSymbol = leftToRight[0];
		String destination = leftToRight[1];
				
		originAndSymbol = originAndSymbol.substring(1, originAndSymbol.length()-2);
		destination = destination.substring(3, destination.length()-2);
		
		String origin[] = originAndSymbol.split(",");
		String dest[] = destination.split(",");
		String stack = dest[1];
		
		State current = dpda.getStateByString(origin[0]);
		Symbol symbolRead = dpda.getSymbolByString(origin[1]);
		StackSymbols stackRead = dpda.getStackByString(origin[2]);
		
		State stateReached = dpda.getStateByString(dest[0]);
		
		ArrayList<StackSymbols> stackReached = new ArrayList<StackSymbols>();
		
		for (int i = 0; i < stack.length(); i++)
			if (dpda.getStackByString(String.valueOf(stack.charAt(i))) !=null)
				stackReached.add(dpda.getStackByString(String.valueOf(stack.charAt(i))));
			
		current.addTransition(symbolRead, stackRead, stateReached, stackReached);
		
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
				if (i == 0) 
					readStates(line);
				else if (i == 1) 
					readSymbols(line);
				else if (i == 2) 
					readStackSymbols(line);
				else if (i == 3) 
					readAcceptingStates(line);
				else if (i == 4) 
					readInitialState(line);
				else if (i == 5) 
					readInitialStackSymbol(line);
				else if (i == 6) 
					readAcceptingString(line);
				else if (i == 7) 
					;
				else if (i >= 8) 
					readTransition(line);
				
				
				
			}
			i++;
		}
	}
}
