package nu.dyn.caapi.model.market;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;


public class MarketClient {
	public Poloniex market;
	
	public MarketClient() {
		CoinPair coinPair = new CoinPair(CoinSymbols.BTC, CoinSymbols.XMR);
		
		
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			long time_start = dateFormat.parse("7/07/2014").getTime()/1000;
			long time_end = dateFormat.parse("23/07/2015").getTime()/1000;
			Timeframe timeframe = new Timeframe(time_start, time_end);
		
			market = new Poloniex(coinPair, timeframe);
//			System.out.println(market);	
			
			ClosePriceIndicator closePrice = new ClosePriceIndicator(market.chart.series);
			
			SMAIndicator i_sma = new SMAIndicator(closePrice, 5750);
			
//			System.out.println("x-ticks-SMA value at the 50th index: " + i_sma.getValue(0));
			
		} catch (ParseException e) {
			System.out.println("Error: "+ e);
		}
	}
	
	
}
