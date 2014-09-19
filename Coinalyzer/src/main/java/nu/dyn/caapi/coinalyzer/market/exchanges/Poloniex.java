package nu.dyn.caapi.coinalyzer.market.exchanges;

import nu.dyn.caapi.coinalyzer.market.CoinPairInfo;
import nu.dyn.caapi.coinalyzer.market.Market;
import nu.dyn.caapi.coinalyzer.market.TimeWindow;
import nu.dyn.caapi.coinalyzer.utils.MyJsonReader;

public class Poloniex extends Market {
	private CoinPairInfo coinPair;
	TimeWindow t;
	
	public Poloniex( TimeWindow t, MyJsonReader jsonReader, CoinPairInfo coinPair) {

		super(t, jsonReader);
		this.t = t;
		this.coinPair = coinPair;
		
	}
		
	protected String constructURL(int windowPeriod) {
		
		return "https://poloniex.com/public?command=returnChartData&currencyPair="+coinPair.getCurrencyPairId()+"&start="+t.getStart().getTime()/1000+"&end="+t.getEnd().getTime()/1000+"&period="+windowPeriod;
	
	}
	
	protected String constructFilename(int windowPeriod) {

		return "poloniex_"+coinPair.getCurrencyPairId()+"_"+windowPeriod+".json";
		
	}
			
}
