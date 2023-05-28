package struct;

import java.util.ArrayList;

/**
 * 
 * Class dataset, used to store all the data. 
 * @author chloe
 *
 */
public class Dataset {
	
	private ArrayList<Feature> features;
	private ArrayList<Value> values;
	private ArrayList<Label> labels;
	
	/**
	 * Constructor of a dataset, created empty list of features, values and labels.
	 */
	public Dataset() {
		this.features = new ArrayList<Feature>();
		this.values = new ArrayList<Value>();
		this.labels = new ArrayList<Label>();
	}
	
	/**
	 * Creates a copy of a dataset. 
	 * @param d Dataset to be copied.
	 */
	public Dataset(Dataset d) {
		this.features = new ArrayList<Feature>();
		this.values = new ArrayList<Value>();
		this.labels = new ArrayList<Label>();
		this.features.addAll(d.getFeatures());
		this.values.addAll(d.getValues());
		this.labels.addAll(d.getLabels());
	}
	
	/**
	 * Creates a new dataset that do not contain the feature given into parameter.
	 * @param d Original dataset. 
	 * @param f Feature we do not want to have in our newly created dataset.
	 */
	public Dataset(Dataset d, Feature f) {
		this.features = new ArrayList<Feature>();
		this.values = new ArrayList<Value>();
		this.labels = new ArrayList<Label>();
		
		for (int i = 0; i < d.getFeatures().size(); i++) 
			if (d.getFeatures().get(i).getName() != f.getName())
				this.features.add(d.getFeatures().get(i));
		
		for (int i = 0; i < d.getValues().size(); i++) 
			if (d.getValues().get(i).getFeature().getName() != f.getName())
				this.values.add(d.getValues().get(i));
		
		this.labels.addAll(d.getLabels());
	}
	
	/**
	 * Creates a dataset that contains all the values and features equals to the ones given into parameter. 
	 * @param d Original dataset. 
	 * @param f Feature we want. 
	 * @param v Value we want. 
	 */
	public Dataset(Dataset d, Feature f, Value v) {
		this.features = new ArrayList<Feature>();
		this.values = new ArrayList<Value>();
		this.labels = new ArrayList<Label>();
		for (int i = 0; i < d.getValues().size(); i++) {
			if (v.getName() == d.getValues().get(i).getName() && 
					d.getValues().get(i).getFeature().getName() == f.getName()) {
				this.values.add(d.getValues().get(i));
				this.features.add(d.getValues().get(i).getFeature());
			}
		}
		
		for (int i = 0; i < d.getLabels().size(); i++) {
			for (int y = 0; y < d.getLabels().get(i).getValues().size(); y++) {
				if (d.getLabels().get(i).getValues().get(y).getName().equals(v.getName()) && 
						d.getLabels().get(i).getValues().get(y).getFeature().getName().equals(v.getFeature().getName())) {
					this.labels.add(d.getLabels().get(i));
				}
			}
			
		}
	}
	
	/**
	 * Created a dataset that contains only the values contained in parameter. 
	 * @param d Original dataset. 
	 * @param values Values used to create our dataset. 
	 */
	public Dataset(Dataset d, ArrayList<Value> values) {
		for (int i = 0; i < values.size(); i++)
			d = new Dataset(d, values.get(i));
	}
	
	/**
	 * Using a given datatset, creates a new dataset which contains only the features/values & labels corresponding to the value
	 * given into parameter. 
	 * @param d Dataset used to create a new dataset. 
	 * @param v Value we want to use to recreate our dataset with. 
	 */
	public Dataset(Dataset d, Value v) {
		this.features = new ArrayList<Feature>();
		this.values = new ArrayList<Value>();
		this.labels = new ArrayList<Label>();
		
		for (int i = 0; i < d.getLabels().size(); i++) {
			for (int y = 0; y < d.getLabels().get(i).getValues().size(); y++)
				// TODO change condition
				if (d.getLabels().get(i).getValues().get(y).getName().equals(v.getName()) && d.getLabels().get(i).getValues().get(y).getFeature().getName().equals(v.getFeature().getName())) {
					this.labels.add(d.getLabels().get(i));
				}
		}
		
		for (int i = 0; i < this.getLabels().size(); i++) {
			this.values.addAll(this.getLabels().get(i).getValues());	
		}
		
		for (int i = 0; i < d.getFeatures().size(); i++) {
			if (!v.getFeature().getName().equalsIgnoreCase(d.getFeatures().get(i).getName())) {
				this.features.add(d.getFeatures().get(i));
			}
		}
	}

	/**
	 * @return the features
	 */
	public ArrayList<Feature> getFeatures() {
		return features;
	}

	/**
	 * @return the values
	 */
	public ArrayList<Value> getValues() {
		return values;
	}

	/**
	 * @return the labels
	 */
	public ArrayList<Label> getLabels() {
		return labels;
	}

	/**
	 * @param features the features to set
	 */
	public void setFeatures(ArrayList<Feature> features) {
		this.features = features;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(ArrayList<Value> values) {
		this.values = values;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(ArrayList<Label> labels) {
		this.labels = labels;
	}
	
	public ArrayList<Value> getValuesForAFeature(Feature feature) {
		ArrayList<Value> tmp = new  ArrayList<Value>();
		for (int i = 0; i < this.getValues().size(); i++) {
			if (this.getValues().get(i).getFeature().getName() == feature.getName()) {
				tmp.add(this.getValues().get(i));
			}
		}
		return tmp;
	}
	
	public ArrayList<Value> getPossibleValuesForAFeature(Feature feature) {
		ArrayList<Value> tmp = new  ArrayList<Value>();
		for (int i = 0; i < this.getValues().size(); i++) {
			if (this.getValues().get(i).getFeature().getName() == feature.getName()) {
				if (!tmp.contains(this.getValues().get(i)))
					tmp.add(this.getValues().get(i));
			}
		}
		return tmp;
	}
	
	public ArrayList<Label> getPossibleLabels() {
		ArrayList<Label> labels = new ArrayList<Label>();
		for (int i = 0; i < this.getLabels().size(); i++) {
			if (!labels.contains(this.getLabels().get(i)))
				labels.add(this.getLabels().get(i));
		}
		return labels;
	}

	@Override
	public String toString() {
		return "Dataset [\nFeatures=" + features + "\nValues=" + values + "\nLabels=" + labels + "\n]";
	}
}
