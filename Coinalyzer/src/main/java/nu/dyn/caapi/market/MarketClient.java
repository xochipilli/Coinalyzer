package nu.dyn.caapi.market;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import nu.dyn.caapi.bot.AppConfig;
import nu.dyn.caapi.market.exchanges.Poloniex;
import nu.dyn.caapi.model.Analytics;
import nu.dyn.caapi.nn.MyPerceptron;
import nu.dyn.caapi.nn.TrainDataItem;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.stereotype.Service;

@Service
public class MarketClient {
	public Market market;
	public AppConfig appConfig;
	
	public MarketClient() throws ConfigurationException {
		
		appConfig = new AppConfig();
		
		CoinPairInfo coinPair = new CoinPairInfo(appConfig.coinPrimary, appConfig.coinCounter);
				
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Window timeframe = new Window(dateFormat.parse("1/1/2013"), dateFormat.parse("1/1/2015"), Constants.period_15m);
	
			market = new Poloniex(coinPair, timeframe);
			market.getAllSeries(false);

		Analytics analytics = new Analytics(market.series_2h);
		
			
			
		} catch (ParseException e) {
			//TODO:
			System.out.println("Error: "+ e);
		}
	}
	
	public byte[] getChart() {
		
		return market.chart.getChart(false);
		
	}
	
	public byte[] getRefreshedChart() {
		
		return market.chart.getChart(true);
		
	}

	public void setChartRange(Window t) {
		
		market.chart.getChart(t.getStart(), t.getEnd());
		
	}
	
	public void setChartPeriod(int period) {
		
		market.setPeriod(period);
		
	}
	
	
}
