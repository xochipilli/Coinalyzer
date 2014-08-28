package nu.dyn.caapi.coinalyzer.nn;

import java.util.ArrayList;

import org.neuroph.core.data.DataSet;

public abstract class DataNormalizer {

	abstract public ArrayList<TrainDataItem> normalize(ArrayList<TrainDataItem> dataset);

	abstract public DataSet denormalize(DataSet dataset);
	
	abstract public double[] denormalize(double[] data);
		
}

