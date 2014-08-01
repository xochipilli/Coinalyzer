package nu.dyn.caapi.model.market;

import java.io.IOException;
import java.util.ArrayList;

import nu.dyn.caapi.utils.JsonReader;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;

public class Poloniex {
	
	private CoinPairInfo coinPair;
	public Chart chart;
//	public Chart chart_5m;
//	public Chart chart_15m;
//	public Chart chart_30m;
//	public Chart chart_1h;
//	public Chart chart_2h;
//	public Chart chart_4h;
	Timeframe t;
	
	public Poloniex(CoinPairInfo coinPair, Timeframe t) {

		this.coinPair = coinPair;
		this.t = t;
		getCharts();
	}
	
	protected String constructURL(int period) {
System.out.println("https://poloniex.com/public?command=returnChartData&currencyPair="+coinPair.getCurrencyPairId()+"&start="+t.getStart()+"&end="+t.getEnd()+"&period="+period);
		return "https://poloniex.com/public?command=returnChartData&currencyPair="+coinPair.getCurrencyPairId()+"&start="+t.getStart()+"&end="+t.getEnd()+"&period="+period;
	}
	
	private void getCharts() {
		
		chart = getChart(t, Constants.period_15m);
//		chart_5m = getChart(t, Constants.period_5m);
//		chart_15m = getChart(t, Constants.period_15m);
//		chart_30m = getChart(t, Constants.period_30m);
//		chart_1h = getChart(t, Constants.period_1h);
//		chart_2h = getChart(t, Constants.period_2h);
//		chart_4h = getChart(t, Constants.period_4h);
	}
		
	private Chart getChart(Timeframe timeframe, int period) {
		try {
		
			String URL = constructURL(period);
		    JSONArray json = JsonReader.readJsonFromUrl(URL);
		    
//		    ArrayList<Candlestick> arr = new ArrayList<Candlestick>() ;
		    ArrayList<CoinTick> arr = new ArrayList<CoinTick>() ;
		    
		    for (int i=0; i<json.length(); i++) {
//		    	arr.add(new Candlestick(json.getJSONObject(i)));
		    	arr.add(new CoinTick(
			    			new DateTime(( json.getJSONObject(i).getLong("date") * 1000L ), DateTimeZone.forID( "Europe/Paris" )),
			    			json.getJSONObject(i).getDouble("open"),
			    			json.getJSONObject(i).getDouble("high"),
			    			json.getJSONObject(i).getDouble("low"),
			    			json.getJSONObject(i).getDouble("close"),
			    			json.getJSONObject(i).getDouble("volume")	));
		    }
		    
		    return new Chart(coinPair.getCurrencyPairId(), arr);
		    
		} catch (IOException e) {
			System.out.println("Error: "+ e);
		}
		
		return null;
	}

	@Override
	public String toString() {
		return chart.toString(); //_5m+chart_15m+chart_30m+chart_1h+chart_2h+chart_4h;
	}	
		
}
