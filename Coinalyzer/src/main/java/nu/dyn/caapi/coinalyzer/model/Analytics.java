package nu.dyn.caapi.coinalyzer.model;

import java.util.ArrayList;
import java.util.concurrent.Future;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.chart.NNErrorChart;
import nu.dyn.caapi.coinalyzer.exceptions.PNGChartCreationException;
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

	public NNErrorChart errorChart;
	public NNErrorChart errorChart_testset;
	
	private static final Logger logger = LoggerFactory.getLogger(Analytics.class);

	public ArrayList<MyPerceptron> perceptrons;
	
	/** Initialize all perceptrons 
	 */
	public void init() {
		
		logger.info("Initializing perceptrons with "+appConfig.nn);
		
		perceptrons = new ArrayList<MyPerceptron>();
		perceptrons.add(getInitializedPerceptron(client.market.series_5m, "Perceptron 5m"));
//		perceptrons.add(getInitializedPerceptron(client.market.series_15m, "Perceptron 15m"));
//		perceptrons.add(getInitializedPerceptron(client.market.series_30m, "Perceptron 30m"));
//		perceptrons.add(getInitializedPerceptron(client.market.series_2h, "Perceptron 2h"));
//		perceptrons.add(getInitializedPerceptron(client.market.series_4h, "Perceptron 4h"));
		
		 errorChart = new NNErrorChart();
		 errorChart_testset = new NNErrorChart();
		 

	}
	
	/** Return new named perceptron with train and test datasets for configured dates 
	 * @param series Input cointick serie
	 * @param name
	 */
	private MyPerceptron getInitializedPerceptron(ArrayList<CoinTick> series, String name) {
		
		ArrayList<TrainDataItem> testset  = new ArrayList<TrainDataItem>();
		ArrayList<TrainDataItem> trainset = new ArrayList<TrainDataItem>();
		
		int size = series.size();
		int start = size - 1 - appConfig.nn.getMaxDatasetSize();
		if (start < 0)	
			start = 0;
		int start_test = (int) (appConfig.nn.getTrainsetRatio() * (size - start) + start);
		
		for (int i = start; i < size - 1; i++) {
			// dataset start
			//if (! series.get(i).getBeginTime().isBefore(appConfig.nn.getTraindataStart())) {
				TrainDataItem tdi = new TrainDataItem(series.get(i), series.get(i + 1));
				// test data start
				//if (! series.get(i).getBeginTime().isBefore(appConfig.nn.getTestdataStart())) {
				if (i >= start_test) {
					testset.add(tdi);
				// train data set
				} else { 
					trainset.add(tdi);
				}
			//}
		}

		return new MyPerceptron(trainset, testset, name, appConfig.nn);
	}

//	public ArrayList<String> getPerceptronNames() {
//		
//		ArrayList<String> names = new ArrayList<>();
//		
//		for (MyPerceptron p: perceptrons)
//			names.add(p.name);
//		
//		return names;
//	}
	
	public byte[] getErrorChart() {
		
	    try {
			errorChart.plotChart("Training errors");
		} catch (PNGChartCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return errorChart.getPNGChart();
		
	}
	
	public byte[] getErrorChart_testset() {
		
	    try {
			errorChart_testset.plotChart("Test dataset errors");
		} catch (PNGChartCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return errorChart_testset.getPNGChart();
		
	}
	
	/** Train all perceptrons
	 * @return
	 */
	@Async
	public Future<String> train() { // throws InterruptedException {

		for (MyPerceptron p: perceptrons) {
			p.train();
			errorChart.addNNErrorChart(p.name, p.error.getErrors());
		}
	
		return new AsyncResult<String>(perceptrons.toString());
	}

	/** Train all perceptrons
	 * @return
	 */
	@Async
	public Future<String> test() { // throws InterruptedException {

		for (MyPerceptron p: perceptrons) {
			p.test();
			errorChart_testset.addNNErrorChart(p.name, p.error.getErrors_testSet());
		}
		
		return new AsyncResult<String>(perceptrons.toString());
	}

}
