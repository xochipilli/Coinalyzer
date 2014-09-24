package nu.dyn.caapi.coinalyzer.model;

import java.util.ArrayList;
import java.util.concurrent.Future;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
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
	@Autowired
	AppConfig appConfig;

	private static final Logger logger = LoggerFactory.getLogger(Analytics.class);

	ArrayList<MyPerceptron> perceptrons;
	
	/** Initialize all perceptrons 
	 */
	public void init() {
		
		logger.info("Initializing perceptrons with "+appConfig.nn);
		
		perceptrons = new ArrayList<MyPerceptron>();
		perceptrons.add(getInitializedPerceptron(client.market.series_5m, "Perceptron 5m"));
		perceptrons.add(getInitializedPerceptron(client.market.series_15m, "Perceptron 15m"));
		perceptrons.add(getInitializedPerceptron(client.market.series_30m, "Perceptron 30m"));
		perceptrons.add(getInitializedPerceptron(client.market.series_2h, "Perceptron 2h"));
		perceptrons.add(getInitializedPerceptron(client.market.series_4h, "Perceptron 4h"));
		
	}
	
	/** Return new named perceptron with train and test datasets for configured dates 
	 * @param series Input cointick serie
	 * @param name
	 */
	private MyPerceptron getInitializedPerceptron(ArrayList<CoinTick> series, String name) {
		
		ArrayList<TrainDataItem> testset  = new ArrayList<TrainDataItem>();
		ArrayList<TrainDataItem> trainset = new ArrayList<TrainDataItem>();
		
		for (int i = 0; i < series.size() - 1; i++) {
			// dataset start
			if (! series.get(i).getBeginTime().isBefore(appConfig.nn.getTraindataStart())) {
				TrainDataItem tdi = new TrainDataItem(series.get(i), series.get(i + 1));
				// test data start
				if (! series.get(i).getBeginTime().isBefore(appConfig.nn.getTestdataStart())) {
					testset.add(tdi);
				// train data set
				} else { 
					trainset.add(tdi);
				}
			}
		}

		return new MyPerceptron(trainset, testset, name, appConfig.nn);
	}

	/** Train all perceptrons
	 * @return
	 */
	@Async
	public Future<String> train() { // throws InterruptedException {

		for (MyPerceptron p: perceptrons)
			p.train();
	
		return new AsyncResult<String>(perceptrons.toString());
	}

	/** Train all perceptrons
	 * @return
	 */
	@Async
	public Future<String> test() { // throws InterruptedException {

		for (MyPerceptron p: perceptrons)
			p.test();
	
		return new AsyncResult<String>(perceptrons.toString());
	}

}
