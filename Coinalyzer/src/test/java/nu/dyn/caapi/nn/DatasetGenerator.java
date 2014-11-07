package nu.dyn.caapi.nn;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import nu.dyn.caapi.coinalyzer.market.CoinTick;
import nu.dyn.caapi.coinalyzer.nn.TrainDataItem;

import org.joda.time.DateTime;

import eu.verdelhan.ta4j.Tick;

public class DatasetGenerator {
	
	static final int decimal_places_mask = 100000000;
	static final int max_percent_change = 10;
	static final int max_volume = 77777;
	static final int price_max_value = 1;
	static final int number_of_dataset_items = 10;
	
	static Random r = new Random();
	static DateTime dt;
	
	public static ArrayList<TrainDataItem> generate() {
		
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dt = new DateTime(dateFormat.parse("1/1/2013"));
		} catch (ParseException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<TrainDataItem> dataset = new ArrayList<TrainDataItem>();
		
		for (int i=0; i<number_of_dataset_items; i++) {
			
			TrainDataItem tdi = new TrainDataItem(generateTick(), generateTick());
			dataset.add(tdi);
		}
		
		return dataset;
	}
	
	public static Tick generateTick() {
		
		double open = ((double) r.nextInt(price_max_value*decimal_places_mask))/decimal_places_mask;
		double close = open;
		
		double c = open / 100D * ((double) r.nextInt(max_percent_change*decimal_places_mask))/decimal_places_mask;
		if (r.nextBoolean())
			close -= c;
		else
			close += c;
		
		Tick tick = new CoinTick(dt, 
				open,	 
				open+open / 100D *((double) r.nextInt(max_percent_change*decimal_places_mask))/decimal_places_mask,  //high
				close-close / 100D *((double) r.nextInt(max_percent_change*decimal_places_mask))/decimal_places_mask,  //low
				close,  //close
				r.nextInt(max_volume)+r.nextFloat()); //volume
		
		return tick;
	}

}
