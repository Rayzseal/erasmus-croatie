package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import struct.Clause;
import struct.Element;
import struct.UserWish;
import struct.UserWishes;

/**
 * 
 * Class parser, used to parse files of clauses & cooking files containing user queries. 
 * @author chloe
 *
 */
public class Parser {
	
	private Element element;
	
	/**
	 * Creates an empty element. 
	 */
	public Parser() {
		this.element = new Element();
	}
	
	/**
	 * Read a clause file given into parameters. 
	 * @param elementFile Clause file to be read. 
	 * @throws IOException Excpetion while opening the file.
	 */
	public void readAllElem(String elementFile) throws IOException {
		
		String nameFile = "src/main/resources/"+elementFile;
		
		// Open file
		File fileDir = new File(nameFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
			
		String line;
		
		// All lines
		while((line = in.readLine()) != null) {
						
			String data = line.toString();
						
			// Line in file is not a comment (starting by "#")
			if (data.charAt(0) != '#') {
				// Adding a clause to an element 
				element.getClauses().add(new Clause(data.toLowerCase()));
			}
		}
		
		
	}
	
	/**
	 * Reading a cooking file given into parameters. 
	 * @param cookingFile Cooking File to read. 
	 * @throws IOException Excpetion while opening the file. 
	 */
	public void readAllWishes(String cookingFile) throws IOException {
		String nameFile = "src/main/resources/"+cookingFile;
		
		// Open file
		File fileDir = new File(nameFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
			
		String line;
		
		// All lines
		while((line = in.readLine()) != null) {
						
			String data = line.toString().toLowerCase();
						
			// Line in file is not a comment (starting by "#")
			if (data.charAt(0) != '#') {
				
				// Reading the entire content of a line
				String c ="";
				String val = "";
				boolean end = false;
				int i = 0;
				// Separating the content of a clause from the characters +,- or &
				while (i < data.length() && !end) {
					if (data.charAt(i) == ' ' && (data.charAt(i+1) == '-' || data.charAt(i+1) == '+' || data.charAt(i+1) == '?' )) {
						val += data.charAt(i+1);
						end = true;
					} else {
						c += data.charAt(i);
					}
					i++;
				}
				
				data.toLowerCase();
				
				// Adding a user wish
				if (val.equalsIgnoreCase("?")) {
					UserWishes us= new UserWishes(c,UserWish.WANT);
					element.getWishes().add(us);
				} else if (val.equalsIgnoreCase("-")) {
					UserWishes us= new UserWishes(c,UserWish.DELETE);
					element.getWishes().add(us);
				} else if (val.equalsIgnoreCase("+")) {
					UserWishes us= new UserWishes(c,UserWish.ADD);
					element.getWishes().add(us);
				}
			}
		}
	}
	
	/**
	 * Method to read 2 files given in parameters. 
	 * @param elementFile File of clauses. 
	 * @param cookingFile File of cooking clauses. 
	 * @throws IOException Exception occuring while opening the files. 
	 */
	public void read(String elementFile, String cookingFile) throws IOException {
		// It is possible that there's no cookingFile file given
		if (cookingFile != null ) {	
			readAllElem(elementFile);
			readAllWishes(cookingFile);
		} else {
			readAllElem(elementFile);
		}
	}

	/**
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(Element element) {
		this.element = element;
	}
}
