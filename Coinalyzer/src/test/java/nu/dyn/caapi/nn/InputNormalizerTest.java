package nu.dyn.caapi.nn;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import nu.dyn.caapi.market.CoinTick;
import nu.dyn.caapi.nn.normalizers.TanHNormalizer;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import eu.verdelhan.ta4j.Tick;

public class InputNormalizerTest {

	@Test
	public final void testTanhEstimator() {
		
//		final int decimal_places_mask = 100000000;
//		final int max_percent_change = 10;
//		final int max_volume = 77777;
//		final int number_of_dataset_items = 10;
//		
//		ArrayList<TrainDataItem> dataset = new ArrayList<TrainDataItem>();
//		
//		Random r = new Random();
//		double open;
//		double close;
//		
//		DateTime dt=null;
//		try {
//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//			dt = new DateTime(dateFormat.parse("1/1/2013"));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for (int i=0; i<number_of_dataset_items; i++) {
//			
//			open = r.nextInt(100*decimal_places_mask)/decimal_places_mask;
//			close = open - r.nextInt(100*decimal_places_mask)/decimal_places_mask;
//		
//			Tick tick_i = new CoinTick(dt, 
//					open,	 
//					open+r.nextInt(max_percent_change*decimal_places_mask)/decimal_places_mask, // high
//					close-r.nextInt(max_percent_change*decimal_places_mask)/decimal_places_mask, // low
//					close, // close
//					(double) r.nextInt(max_volume*decimal_places_mask)/decimal_places_mask ); //volume
//			
//			open = r.nextInt(100*decimal_places_mask)/decimal_places_mask;
//			close = open - r.nextInt(100*decimal_places_mask)/decimal_places_mask;
//					
//			Tick tick_o = new CoinTick(dt, 
//					open,	 
//					open+r.nextInt(max_percent_change*decimal_places_mask)/decimal_places_mask, // high
//					close-r.nextInt(max_percent_change*decimal_places_mask)/decimal_places_mask, // low
//					close, // close
//					(double) r.nextInt(max_volume*decimal_places_mask)/decimal_places_mask ); //volume
//			
//			TrainDataItem tdi = new TrainDataItem(tick_i, tick_o);
//			
//			dataset.add(tdi);
//		}
//
//		// test output to be in range <-1,1>
//		DataNormalizer normalizer = new TanHNormalizer(dataset);
//		ArrayList<TrainDataItem> arr_norm = normalizer.normalize(dataset);
//		
//		for (int i=0; i<arr_norm.size(); i++) {
//			System.out.println("Generated input " + i);
//			System.out.println("open:"+dataset.get(i).i_open +" normalized:"+arr_norm.get(i).i_open);
//			System.out.println("high:"+dataset.get(i).i_high +" normalized:"+arr_norm.get(i).i_high);
//			System.out.println("low:"+dataset.get(i).i_low +" normalized:"+arr_norm.get(i).i_low);
//			System.out.println("close:"+dataset.get(i).i_close +" normalized:"+arr_norm.get(i).i_close);
//			System.out.println("volume:"+dataset.get(i).i_volume +" normalized:"+arr_norm.get(i).i_volume);
//			System.out.println("");
//			
//			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).i_open < 1 && arr_norm.get(i).i_open > -1);
//			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).i_low < 1 && arr_norm.get(i).i_low > -1);
//			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).i_close < 1 && arr_norm.get(i).i_close > -1);
//			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).i_high < 1 && arr_norm.get(i).i_high > -1);
//			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).i_volume < 1 && arr_norm.get(i).i_volume > -1);
//			
////			System.out.println("Generated output " + i);
////			System.out.println("open:"+dataset.get(i).o_open +" normalized:"+arr_norm.get(i).o_open);
////			System.out.println("high:"+dataset.get(i).o_high +" normalized:"+arr_norm.get(i).o_high);
////			System.out.println("low:"+dataset.get(i).o_low +" normalized:"+arr_norm.get(i).o_low);
////			System.out.println("close:"+dataset.get(i).o_close +" normalized:"+arr_norm.get(i).o_close);
////			System.out.println("volume:"+dataset.get(i).o_volume +" normalized:"+arr_norm.get(i).o_volume);
////			System.out.println("");
////			
////			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).o_open < 1 && arr_norm.get(i).o_open > -1);
////			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).o_low < 1 && arr_norm.get(i).o_low > -1);
////			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).o_close < 1 && arr_norm.get(i).o_close > -1);
////			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).o_high < 1 && arr_norm.get(i).o_high > -1);
////			Assert.assertTrue("Normalizing function returned result out of <-1,1> bound", arr_norm.get(i).o_volume < 1 && arr_norm.get(i).o_volume > -1);
//		}
//		
	}

}
