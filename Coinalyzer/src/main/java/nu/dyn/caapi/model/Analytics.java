package nu.dyn.caapi.model;

import java.util.ArrayList;
import java.util.concurrent.Future;

import nu.dyn.caapi.market.CoinTick;
import nu.dyn.caapi.nn.MyPerceptron;
import nu.dyn.caapi.nn.TrainDataItem;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class Analytics {
	MyPerceptron nn;
	ArrayList<TrainDataItem> dataset = new ArrayList<TrainDataItem>();

	public void init(ArrayList<CoinTick> market_data) {

		for (int i = 0; i < market_data.size() - 1; i++) {
			TrainDataItem tdi = new TrainDataItem(market_data.get(i),
					market_data.get(i + 1));
			dataset.add(tdi);
		}

	}

	@Async
	public Future<String> train() { //throws InterruptedException {
		System.out.println("Training perceptron");
		MyPerceptron p = new MyPerceptron(dataset);

		return new AsyncResult<String>(p.toString());
	}

}
