package nu.dyn.caapi.model;

import java.util.ArrayList;

import nu.dyn.caapi.market.CoinTick;
import nu.dyn.caapi.nn.MyPerceptron;
import nu.dyn.caapi.nn.TrainDataItem;

public class Analytics {
	MyPerceptron nn;
	
	public Analytics( ArrayList<CoinTick> market_data) {
		
		ArrayList<TrainDataItem> arr_tdi = new ArrayList<TrainDataItem>();
		for (int i=0; i<market_data.size()-1; i++ ) {
			TrainDataItem tdi = new TrainDataItem(market_data.get(i), market_data.get(i+1));
			arr_tdi.add(tdi);
		}

		MyPerceptron p = new MyPerceptron(arr_tdi);

		
	
	}
	
	
}
