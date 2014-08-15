package nu.dyn.caapi.market;

import java.io.IOException;
import java.util.ArrayList;

import nu.dyn.caapi.utils.MyJsonReader;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;

public abstract class Market {
	
	private CoinPairInfo coinPair;
	public Chart chart;
	public ArrayList<CoinTick> series_5m;
	public ArrayList<CoinTick> series_15m;
	public ArrayList<CoinTick> series_30m;
	public ArrayList<CoinTick> series_2h;
	public ArrayList<CoinTick> series_4h;
	
//	public ArrayList<Series> series_5m_indicators;
//	public ArrayList<Series> series_15m_indicators;
//	public ArrayList<Series> series_30m_indicators;
//	public ArrayList<Series> series_2h_indicators;
//	public ArrayList<Series> series_4h_indicators;
//	
	Window t;
	
	protected abstract String constructURL(int windowLength);
	protected abstract String constructFilename(int windowLength);
	
	public Market(CoinPairInfo coinPair, Window t) {

		this.coinPair = coinPair;
		this.t = t;
		
	}
		
	public void setPeriod(int period) {
		
		t.setLength(period);
		chart = new Chart(coinPair.getCurrencyPairId(), getSeries(period, true), t);
		
	}
	
	public void getAllSeries(boolean refresh) {
		
		series_5m = getSeries(Constants.period_5m, refresh);
		series_15m = getSeries(Constants.period_15m, refresh);
		series_30m = getSeries(Constants.period_30m, refresh);
		series_2h = getSeries(Constants.period_2h, refresh);
		series_4h = getSeries(Constants.period_4h, refresh);
		
		chart = new Chart(coinPair.getCurrencyPairId(), series_2h, t);	// TODO: choose t.length series
		
	}
	
	public ArrayList<CoinTick> getSeries(int windowLength, boolean refresh) {
		
		ArrayList<CoinTick> arr = new ArrayList<CoinTick>() ;
		
		try {
			
			String URL = constructURL(windowLength);
			String filename = constructFilename(windowLength);
			
			JSONArray json;
			
			if (refresh)
				 json = MyJsonReader.readJsonFromUrl(URL, filename);
			else
				json = MyJsonReader.readJson(URL, filename);
		    
		    for (int i=0; i<json.length(); i++) {
		    	arr.add(new CoinTick(
			    			new DateTime(( json.getJSONObject(i).getLong("date") * 1000L ), DateTimeZone.forID( "Europe/Paris" )),
			    			json.getJSONObject(i).getDouble("open"),
			    			json.getJSONObject(i).getDouble("high"),
			    			json.getJSONObject(i).getDouble("low"),
			    			json.getJSONObject(i).getDouble("close"),
			    			json.getJSONObject(i).getDouble("volume")	));
		    }
		
		} catch (IOException e) {
			System.out.println("Error: "+ e);
		}
		
	    return arr;
	}
	
	@Override
	public String toString() {
		return chart.toString(); //TODO: _5m+chart_15m+chart_30m+chart_1h+chart_2h+chart_4h;
	}	
		
}
