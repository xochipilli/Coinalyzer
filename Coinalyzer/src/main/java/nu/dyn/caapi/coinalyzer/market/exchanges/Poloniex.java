package nu.dyn.caapi.coinalyzer.market.exchanges;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.market.CoinPairInfo;
import nu.dyn.caapi.coinalyzer.market.Market;
import nu.dyn.caapi.coinalyzer.market.Window;

public class Poloniex extends Market {
	private CoinPairInfo coinPair;
	Window t;
	
	public Poloniex(CoinPairInfo coinPair, Window t, AppConfig appConfig) {

		super(coinPair, t, appConfig);
		
		this.t = t;
		this.coinPair = coinPair;

	}
		
	protected String constructURL(int windowLength) {
		
		return "https://poloniex.com/public?command=returnChartData&currencyPair="+coinPair.getCurrencyPairId()+"&start="+t.getStart().getTime()/1000+"&end="+t.getEnd().getTime()/1000+"&period="+windowLength;
	
	}
	
	protected String constructFilename(int windowLength) {

		return "poloniex_"+coinPair.getCurrencyPairId()+"_"+windowLength+".json";
		
	}
			
}
