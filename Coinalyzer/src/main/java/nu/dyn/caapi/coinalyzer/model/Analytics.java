package nu.dyn.caapi.coinalyzer.model;

import java.util.ArrayList;
import java.util.concurrent.Future;

import nu.dyn.caapi.coinalyzer.controllers.HomeController;
import nu.dyn.caapi.coinalyzer.market.CoinTick;
import nu.dyn.caapi.coinalyzer.market.MarketClient;
import nu.dyn.caapi.coinalyzer.nn.MyPerceptron;
import nu.dyn.caapi.coinalyzer.nn.TrainDataItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class Analytics {
	@Autowired
	private MarketClient client;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	MyPerceptron nn;
	ArrayList<TrainDataItem> dataset_1;
	ArrayList<TrainDataItem> dataset_2;
	ArrayList<TrainDataItem> dataset_3;
	ArrayList<TrainDataItem> dataset_4;
	ArrayList<TrainDataItem> dataset_5;

	MyPerceptron p1;
	MyPerceptron p2;
	MyPerceptron p3;
	MyPerceptron p4;
	MyPerceptron p5;
	
	public void init() {
		dataset_1 = getTrainingData(client.market.series_4h);
		dataset_2 = getTrainingData(client.market.series_2h);
		dataset_3 = getTrainingData(client.market.series_30m);
		dataset_4 = getTrainingData(client.market.series_15m);
		dataset_5 = getTrainingData(client.market.series_5m);
	}

	private ArrayList<TrainDataItem> getTrainingData(ArrayList<CoinTick> series) {

		ArrayList<TrainDataItem> dataset = new ArrayList<TrainDataItem>();

		for (int i = 0; i < series.size() - 1; i++) {
			TrainDataItem tdi = new TrainDataItem(series.get(i),
					series.get(i + 1));
			dataset.add(tdi);
		}

		return dataset;
	}

	@Async
	public Future<String> train() { // throws InterruptedException {
		logger.info("Training perceptron P1");
		p1 = new MyPerceptron(dataset_1);
		p1.save("p1");
		logger.info("Perceptron p1 finished learning");

		logger.info("Training perceptron P2");
		p2 = new MyPerceptron(dataset_2);
		p1.save("p2");
		logger.info("Perceptron p2 finished learning");
		
		logger.info("Training perceptron P3");
		p3 = new MyPerceptron(dataset_3);
		p1.save("p3");
		logger.info("Perceptron p3 finished learning");
		
		logger.info("Training perceptron P4");
		p4 = new MyPerceptron(dataset_4);
		p1.save("p4");
		logger.info("Perceptron p4 finished learning");
		
		logger.info("Training perceptron P5");
		p5 = new MyPerceptron(dataset_5);
		p1.save("p5");
		logger.info("Perceptron p5 finished learning");

		return new AsyncResult<String>(p1.toString());
	}

}
