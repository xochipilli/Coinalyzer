package nu.dyn.caapi.nn;

import java.util.ArrayList;

import nu.dyn.caapi.coinalyzer.nn.DataNormalizer;
import nu.dyn.caapi.coinalyzer.nn.TrainDataItem;
import nu.dyn.caapi.coinalyzer.nn.normalizers.TanHNormalizer;

import org.junit.Assert;
import org.junit.Test;



public class InputNormalizerTest {

	@Test
	public final void testTanhEstimator() {
		
		ArrayList<TrainDataItem> dataset = DatasetGenerator.generate();
		
		 //test output to be in range <-1,1>
		DataNormalizer normalizer = new TanHNormalizer(dataset);
		ArrayList<TrainDataItem> arr_norm = normalizer.normalize(dataset);
		
		for (int i=0; i<arr_norm.size(); i++) {
			System.out.println("Generated input " + i);
			System.out.println("open:"+dataset.get(i).i_open +" normalized:"+arr_norm.get(i).i_open);
			System.out.println("high:"+dataset.get(i).i_high +" normalized:"+arr_norm.get(i).i_high);
			System.out.println("low:"+dataset.get(i).i_low +" normalized:"+arr_norm.get(i).i_low);
			System.out.println("close:"+dataset.get(i).i_close +" normalized:"+arr_norm.get(i).i_close);
			System.out.println("volume:"+dataset.get(i).i_volume +" normalized:"+arr_norm.get(i).i_volume);
			
			Assert.assertTrue("Normalizing open out of <-1,1> bound", arr_norm.get(i).i_open < 1 && arr_norm.get(i).i_open > -1);
			Assert.assertTrue("Normalizing low out of <-1,1> bound", arr_norm.get(i).i_low < 1 && arr_norm.get(i).i_low > -1);
			Assert.assertTrue("Normalizing close out of <-1,1> bound", arr_norm.get(i).i_close < 1 && arr_norm.get(i).i_close > -1);
			Assert.assertTrue("Normalizing high out of <-1,1> bound", arr_norm.get(i).i_high < 1 && arr_norm.get(i).i_high > -1);
			// TODO:
			//	Assert.assertTrue("Normalizing volume out of <-1,1> bound", arr_norm.get(i).i_volume < 1 && arr_norm.get(i).i_volume > -1);
			
		}
		
	}


	
}
