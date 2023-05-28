package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import struct.Dataset;
import struct.HiddenLayer;
import struct.NeuralNetwork;

/**
 * 
 * The class parser is used to parse given either training or testing files and
 * creates datasets corresponding to the files that have been read.
 * 
 * @author chloe
 *
 */
public class Parser {

	private Dataset datasetTrain;
	private Dataset datasetTest;

	/**
	 * Creates 2 empty datasets, one for training, one for testing.
	 */
	public Parser() {
		this.datasetTrain = new Dataset();
		this.datasetTest = new Dataset();
	}

	/**
	 * Read a dataset file given into parameters.
	 * 
	 * @param dataSetFile datatset file to be read.
	 * @param d           Dataset on which we would like to add the data that have
	 *                    been read.
	 * @throws IOException Excpetion while opening the file.
	 */
	public void readAllData(String dataSetFile, Dataset d) throws IOException {

		String nameFile = "src/main/resources/" + dataSetFile;

		// Open file
		File fileDir = new File(nameFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));

		String line;
		int nbLine = 0;

		// All lines
		while ((line = in.readLine()) != null) {

			nbLine++;

			String data = line.toString();

			// Line in file is not a comment (starting by "#")
			if (data.charAt(0) != '#')
				// Adding values & labels to the dataset
				if (nbLine != 1) {
					readAllValues(line, d);
				}

		}

	// Closing the file
	in.close();

	}

	/**
	 * Method to read values and labels.
	 * 
	 * @param line  Line to be read.
	 * @param d     Dataset on which we would like to add the values and the labels
	 *              that have been read.
	 * @param index index on which the value should be added
	 */
	public void readAllValues(String line, Dataset d ) {
		String data[] = line.split(",");
		ArrayList<Double> tmp= new ArrayList<Double>();
		// We are going through the line
		for (int i = 0; i < data.length; i++) {
			// We are reading a label
			if (i == data.length - 1) 
				// Adding the label to the list
				d.getErrorWanted().add(Double.parseDouble(data[i]));
			// We are reading a value
			else 
				tmp.add(Double.parseDouble(data[i]));
		}
		// Adding a new value to the dataset
		d.getInputValues().add(tmp);
	}

	/**
	 * @return the datasetTrain
	 */
	public Dataset getDatasetTrain() {
		return datasetTrain;
	}

	/**
	 * @return the datasetTest
	 */
	public Dataset getDatasetTest() {
		return datasetTest;
	}

	/**
	 * @param datasetTrain the datasetTrain to set
	 */
	public void setDatasetTrain(Dataset datasetTrain) {
		this.datasetTrain = datasetTrain;
	}

	/**
	 * @param datasetTest the datasetTest to set
	 */
	public void setDatasetTest(Dataset datasetTest) {
		this.datasetTest = datasetTest;
	}

	/**
	 * Method to read 2 files given in parameters.
	 * 
	 * @param trainingFile File of training.
	 * @param testingFile  File of testing.
	 * @throws IOException Exception occuring while opening the files.
	 */
	public void read(String trainingFile, String testingFile, String structure) throws IOException {
		readAllData(trainingFile, datasetTrain);
		readAllData(testingFile, datasetTest);
	}
}
