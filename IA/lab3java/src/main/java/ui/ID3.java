package ui;

import java.util.ArrayList;
import java.util.Collections;

import struct.Dataset;
import struct.Feature;
import struct.Label;
import struct.Value;
import tree.Node;
import tree.Tree;

public class ID3 {
	
	private Tree tree;
	public int maxLevelValue;

	/**
	 * @param currentDataset
	 * @param tree
	 */
	public ID3(int maxLevelValue) {
		this.tree = new Tree();
		this.maxLevelValue = maxLevelValue;
	}

	/**
	 * Compute the entropy for a given dataset 
	 * @param dataset Dataset on which we are computing the entropy. 
	 * @return The value of the entropy. 
	 */
	public double entropy(Dataset dataset) {
		double frequencyLabel[] = new double[dataset.getLabels().size()];
		double nbFreq = 0;
		double entropy = 0;
		
		ArrayList<Label> labels = dataset.getPossibleLabels();
		
		// Getting the number of elements for each label
		for (int i = 0; i < labels.size(); i++) {
			frequencyLabel[i] = Collections.frequency(dataset.getLabels(), new Label(labels.get(i).getName()));
			nbFreq += frequencyLabel[i];
		}
		
		// Computing the entropy
		for (int i = 0; i < frequencyLabel.length; i++) 
			if (frequencyLabel[i] != 0) 
				entropy -= (frequencyLabel[i]/nbFreq) * (Math.log(frequencyLabel[i]/nbFreq) / Math.log(2));
			
		return entropy;
	}
	
	/**
	 * Computed the information gain for a given feature. 
	 * @param dataset Dataset on which we will compute the information gain. 
	 * @param feature Feature to be tested. 
	 * @return The computed information gain. 
	 */
	public double IG(Dataset dataset, Feature feature) {
		double frequencyValues[] = new double[dataset.getLabels().size()];
		double nbFreq = 0, baseEntropy = 0, igTmp = 0, ig = 0;
		
		baseEntropy = entropy(dataset);
		
		ArrayList<Value> valuesPossible = dataset.getPossibleValuesForAFeature(feature);
		
		for (int i = 0; i < valuesPossible.size(); i++) {
			frequencyValues[i] = Collections.frequency(dataset.getValuesForAFeature(feature),new Value(valuesPossible.get(i).getName()));
			nbFreq += frequencyValues[i];
		}
		
		for (int i = 0; i < valuesPossible.size(); i++) {
			Dataset d = new Dataset(dataset, feature, valuesPossible.get(i));
			igTmp -= (frequencyValues[i]/nbFreq)*entropy(d);
		}
		
		ig = baseEntropy + igTmp;
		
		return ig;
	}
	
	/**
	 * Return the most frequent label of the dataset. 
	 * @param dataset Dataset on which we would like to get the most frequent label. 
	 * NB: in 2 labels are present the same number of times, it will return the most frequent label in alphabetical order.
	 * @return Return the most frequent label of the dataset.
	 */
	public static Label argmaxLabel(Dataset dataset) {
		
		Label label = null;
		double frequencyLabel[] = new double[dataset.getLabels().size()];
		
		ArrayList<Label> labels = dataset.getPossibleLabels();
		for (int i = 0; i < labels.size(); i++) 
			frequencyLabel[i] = Collections.frequency(dataset.getLabels(), new Label(labels.get(i).getName()));
		
		
		// Get the max index
		double maxVal = Double.MIN_VALUE;
		for (int i = 0; i < frequencyLabel.length; i++) {
			if (frequencyLabel[i] > maxVal) {
				maxVal = frequencyLabel[i];
				label = labels.get(i);
				// Alphabetical order
			} else if (frequencyLabel[i] == maxVal && labels.get(i).getName().compareTo(label.getName()) < 0) 
				label = labels.get(i);
		}
		
		// Return the most current label
		return label;
	}
	
	/**
	 * Return the feature that has the higher information gain. 
	 * If 2 features have the same information gain, it will return the first feature in alphabetical order. 
	 * @param dataset Dataset on which we are looking for the feature with the highest information gain. 
	 * @return Return the feature that has the higher information gain.
	 */
	public Feature argmax(Dataset dataset) {
		double maxIGValue = Double.MIN_VALUE;
		Feature f = null;
		
		// Get the most discriminative feature
		for (int i = 0; i < dataset.getFeatures().size(); i++) {
			double tmpIG = IG(dataset,dataset.getFeatures().get(i));
			if (tmpIG > maxIGValue) {
				maxIGValue = tmpIG;
				f = dataset.getFeatures().get(i);
				// Alphabetical order
			} else if (tmpIG == maxIGValue && dataset.getFeatures().get(i).getName().compareTo(f.getName()) < 0) {
				f = dataset.getFeatures().get(i);
			}
		}
		return f;
	}
	
	/**
	 * ID3 algorithm.
	 * @param dataset Dataset on which we will execute the ID3 algorithm
	 * @param parentDataset "precedent" dataset.
	 * @param node Actual node in the tree.
	 * @param level Actual level of the tree.
	 */
	public void id3(Dataset dataset, Dataset parentDataset, Node node, int level) {

		// Empty datatset
		if (dataset.getLabels().isEmpty()) {
			// Adding most current value of label of the parent dataset
			Label lab = argmaxLabel(parentDataset);
			// Adding a leaf to the tree
			this.tree.addLeaf(lab, node);
			return;
		}
		
		// Get the most current label of the dataset
		Label label = argmaxLabel(dataset);
		
		// If there are no more feature, if the entropy is equal to 0 or if we reached the maximum level
		if (dataset.getFeatures().isEmpty() || entropy(dataset) == 0 || level >= maxLevelValue) {
			// We need to test the case of level = 0, so that if we want to have only a root node, we can
			if (level != 0) {
				// Adding a leaf to the tree
				this.tree.addLeaf(label, node);
				return;
			}
		}
		
		// Getting the feature with the highest information gain
		Feature feature = argmax(dataset);

		// Getting all of the values possible for the feature having the highest information gain
		ArrayList<Value> tmpValuesForFeature = dataset.getPossibleValuesForAFeature(feature);
		for (Value v : tmpValuesForFeature) {
			Dataset newDataset = new Dataset(dataset,v);
			Node newnode = null;
			// Adding either a root node or a node (basically a feature) to the tree
			// We can have multiple root nodes (basically, a node is defined by its value and not its feature)
			// But all the root nodes will have the same feature
			if (node == null) 
				newnode = this.tree.addRootNode(v);
			else 
				newnode = this.tree.addNode(node, v);
			
			// Restarting the algorithm using the newly generated dataset 
			id3(newDataset,dataset,newnode,newnode.getSize());
		}
		return;
	}

	/**
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}
}
