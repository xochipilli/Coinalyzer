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
		getChart(true);
	}
	
	protected String constructURL() {
		return "https://poloniex.com/public?command=returnChartData&currencyPair="+coinPair.getCurrencyPairId()+"&start="+t.getStart().getTime()/1000+"&end="+t.getEnd().getTime()/1000+"&period="+t.getPeriod();
	}
	
	public void setPeriod(int period) {
		
		t.setPeriod(period);
		getChart(true);
	}
	
	public Chart getChart(boolean refresh) {
	
		try {
		
			String URL = constructURL();
			String filename = "poloniex_"+coinPair.getCurrencyPairId()+"_"+t.getPeriod()+".json";
			
			JSONArray json;
			
			if (refresh)
				 json = JsonReader.readJsonFromUrl(URL, filename);
			else
				json = JsonReader.readJson(URL, filename);
		    
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
		    
		    chart = new Chart(coinPair.getCurrencyPairId(), arr, t);
		    
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
