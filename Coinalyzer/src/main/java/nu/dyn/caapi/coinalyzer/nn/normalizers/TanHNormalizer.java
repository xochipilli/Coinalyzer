package nu.dyn.caapi.coinalyzer.nn.normalizers;

import java.util.ArrayList;
import java.util.Iterator;

import nu.dyn.caapi.coinalyzer.nn.DataNormalizer;
import nu.dyn.caapi.coinalyzer.nn.TrainDataItem;

import org.apache.commons.math3.analysis.function.Atanh;
import org.apache.commons.math3.analysis.function.Tanh;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

// http://www.mirlabs.org/ijcisim/regular_papers_2014/IJCISIM_24.pdf

public class TanHNormalizer extends DataNormalizer {

	public double value_mean;
	public double value_deviation;
		
  /**
	* Calculate Tanh estimator mean and deviation values for dataset values
	* @param dataset List of TrainDataItems 
	*/
	public TanHNormalizer(ArrayList<TrainDataItem> dataset) {
		
		final int num_el_dataitem = 5; //*2;
		double[] arr = new double[dataset.size()*num_el_dataitem];
		
		// put everything in one array so proper parameters can be calculated for the whole set
		Iterator<TrainDataItem> it = dataset.iterator();
		for (int i=0; it.hasNext(); i+=num_el_dataitem) {
			TrainDataItem tdi = it.next();
			arr[i] = tdi.i_open;
			arr[i+1] = tdi.i_low;
			arr[i+2] = tdi.i_high;
			arr[i+3] = tdi.i_close;
			//TODO:
			//			arr[i+4] = tdi.i_volume;
		
//			arr[i+5] = tdi.o_open;
//			arr[i+6] = tdi.o_low;
//			arr[i+7] = tdi.o_high;
//			arr[i+8] = tdi.o_close;
//			arr[i+9] = tdi.o_volume;
			
			// TODO: add indicators
		}

		calculateParameters(arr);
	}

  /**
	* Get normalized data
	* @param dataset Array to be normalized 
	* @return Normalized data
	*/
	public ArrayList<TrainDataItem> normalize(ArrayList<TrainDataItem> dataset) {
		Tanh tanh = new Tanh();
		
		for (int i=0; i<dataset.size(); i++) {
			TrainDataItem tdi = dataset.get(i);
			
			tdi.norm_i_open = 0.5 * ( tanh.value(0.01 * (tdi.i_open - value_mean) /  value_deviation ) + 1 );
			tdi.norm_i_low = 0.5 * ( tanh.value(0.01 * (tdi.i_low - value_mean) /  value_deviation ) + 1 );
			tdi.norm_i_high = 0.5 * ( tanh.value(0.01 * (tdi.i_high - value_mean) /  value_deviation ) + 1 );
			tdi.norm_i_close = 0.5 * ( tanh.value(0.01 * (tdi.i_close - value_mean) /  value_deviation ) + 1 );
			//TODO:
			//	tdi.norm_i_volume = 0.5 * ( tanh.value(0.01 * (tdi.i_volume - value_mean) /  value_deviation ) + 1 );

//			tdi.o_open = 0.5 * ( tanh.value(0.01 * (tdi.o_open - value_mean) /  value_deviation ) + 1 );
//			tdi.o_low = 0.5 * ( tanh.value(0.01 * (tdi.o_low - value_mean) /  value_deviation ) + 1 );
//			tdi.o_high = 0.5 * ( tanh.value(0.01 * (tdi.o_high - value_mean) /  value_deviation ) + 1 );
//			tdi.o_close = 0.5 * ( tanh.value(0.01 * (tdi.o_close - value_mean) /  value_deviation ) + 1 );
//			tdi.o_volume = 0.5 * ( tanh.value(0.01 * (tdi.o_volume - value_mean) /  value_deviation ) + 1 );
			
			//TODO: add indicators
		}
		
		return dataset;
	}
	
  /**
	* Get denormalized data
	* @param dataset DataSet be denormalized 
	* @return Denormalized dataSet
	*/
	// remove?
	public DataSet denormalize(DataSet dataset) {
		
		DataSet denorm_dataset = new DataSet(5, 5);
		
		for (int i=0; i<dataset.size(); i++) {
			
			DataSetRow row = dataset.getRowAt(i);
			denorm_dataset.addRow( new DataSetRow(denormalize(row.getInput()), denormalize(row.getDesiredOutput())) );

		}
		
		return denorm_dataset;
		
	}
	
	public double[] denormalize(double[] data) {
		
		Atanh atanh = new Atanh();
		double[] res = new double[data.length];
				
		for (int i=0; i<data.length; i++) {
			res[i] = atanh.value( data[i] / 0.5  - 1) / 0.01 * value_deviation + value_mean;
		}
		
		return res;
		
	}
	
	private void calculateParameters(double[] input) {
		
		Mean mean = new Mean();
		
		value_mean = mean.evaluate(input);
		value_deviation = new StandardDeviation().evaluate(input);
		
	}
}
