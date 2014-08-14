package nu.dyn.caapi.nn;

import java.util.ArrayList;

import org.neuroph.core.learning.DataSet;

public abstract class DataNormalizer {

	abstract public ArrayList<TrainDataItem> normalize(ArrayList<TrainDataItem> dataset);

	abstract public DataSet denormalize(DataSet dataset);
	
	abstract public double[] denormalize(double[] data);
		
}

