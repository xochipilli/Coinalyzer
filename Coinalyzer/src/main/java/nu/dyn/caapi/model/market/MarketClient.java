package nu.dyn.caapi.model.market;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import nu.dyn.caapi.bot.AppConfig;
import nu.dyn.caapi.utils.TimeframeUtils;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.stereotype.Service;

@Service
public class MarketClient {
	public Poloniex market;
	public AppConfig appConfig;
	
	public MarketClient() throws ConfigurationException {
		 appConfig = new AppConfig();
		
		CoinPairInfo coinPair = new CoinPairInfo(appConfig.coinPrimary, appConfig.coinCounter);
				
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Timeframe timeframe = new Timeframe(dateFormat.parse("1/1/2013"), dateFormat.parse("1/1/2015"), Constants.period_15m);
	
			market = new Poloniex(coinPair, timeframe);
			
//			System.out.println(market);	
			
			//ClosePriceIndicator closePrice = new ClosePriceIndicator(market.chart.series);
			
			//SMAIndicator i_sma = new SMAIndicator(closePrice, 5750);
			
//			System.out.println("x-ticks-SMA value at the 50th index: " + i_sma.getValue(0));
			
		} catch (ParseException e) {
			//TODO:
			System.out.println("Error: "+ e);
		}
	}
	
	public byte[] getChart() {
		
		return market.chart.getChart(false);
		
	}

	public void setChartRange(Timeframe t) {
		
		market.chart.getChart(t.getStart(), t.getEnd());
		
	}
	
	public void setChartPeriod(int period) {
		
		market.setPeriod(period);
		
	}
	
}
