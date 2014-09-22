package nu.dyn.caapi.coinalyzer.market;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import nu.dyn.caapi.coinalyzer.exceptions.HostCouldNotBeResolvedException;
import nu.dyn.caapi.coinalyzer.exceptions.JSONParsingException;
import nu.dyn.caapi.coinalyzer.exceptions.URLOpenConnectionException;
import nu.dyn.caapi.coinalyzer.utils.MyJsonReader;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;

public abstract class Market {

	public ArrayList<CoinTick> series_5m;
	public ArrayList<CoinTick> series_15m;
	public ArrayList<CoinTick> series_30m;
	public ArrayList<CoinTick> series_2h;
	public ArrayList<CoinTick> series_4h;

	// public ArrayList<Series> series_5m_indicators;
	// public ArrayList<Series> series_15m_indicators;
	// public ArrayList<Series> series_30m_indicators;
	// public ArrayList<Series> series_2h_indicators;
	// public ArrayList<Series> series_4h_indicators;
	//
	TimeWindow t;
	MyJsonReader jsonReader;

	protected abstract String constructURL(int windowLength);

	protected abstract String constructFilename(int windowLength);

	public Market(TimeWindow t, MyJsonReader jsonReader) {

		this.t = t;
		this.jsonReader = jsonReader;
		
	}

	/**
	 * Read reference market data series and calculates rest of the series
	 * @param refresh Refresh from internet or use local file?
	 * @throws JSONParsingException
	 * @throws URLOpenConnectionException
	 * @throws HostCouldNotBeResolvedException
	 */
	public void getAllSeries(boolean refresh) throws JSONParsingException, URLOpenConnectionException, HostCouldNotBeResolvedException {

		series_5m = getSeries(Constants.period_5m, refresh);
		
		series_15m = getSerieWithPeriodicity(3, series_5m);
		series_30m = getSerieWithPeriodicity(6, series_5m);
		series_2h = getSerieWithPeriodicity(24, series_5m);
		series_4h = getSerieWithPeriodicity(48, series_5m);

	}

	public ArrayList<CoinTick> getCurrentSeries() {
		
		int p = t.getPeriod();
		
		if (p==Constants.period_5m)
			return  series_5m;
		else if (p==Constants.period_15m)
			return series_15m;
		else if (p==Constants.period_30m)
			return series_30m;
		else if (p==Constants.period_2h)
			return  series_2h;
		else if (p==Constants.period_4h)
			return  series_4h;
		else
			return null;
	}
	
	private  ArrayList<CoinTick> getSerieWithPeriodicity(int periodMultiplicator, ArrayList<CoinTick> base5mSerie) {

		ArrayList<CoinTick> s = new ArrayList<CoinTick>();
		
		int serieSize = base5mSerie.size();
		for (int i = 1; i <= serieSize; i++) {
			if (i % periodMultiplicator == 0) {
				if (serieSize - i - (periodMultiplicator - 1) < 0)
					break;	// the rest of the ticks cannot fill the whole candlestick so we don't add it
				ArrayList<CoinTick> ticks = new ArrayList<CoinTick>();
				for (int j = 0; j < periodMultiplicator; j++) {
					ticks.add(base5mSerie.get( serieSize - i - j));
				}
				s.add(mergeCoinTicks(ticks));
			}
		}
		
		Collections.reverse(s);	// correct order
		
		return s;

	}
	
	// return one tick for all provided ticks
	private CoinTick mergeCoinTicks(ArrayList<CoinTick> ticks) {
		
		double maxPrice = 0D;
		double minPrice = ticks.get(0).getMinPrice();
		double volume = 0D;

		for (int i = 0; i < ticks.size(); i++) {
			if (ticks.get(i).getMaxPrice() > maxPrice)
				maxPrice = ticks.get(i).getMaxPrice();
			if (ticks.get(i).getMinPrice() < minPrice)
				minPrice = ticks.get(i).getMinPrice();
			volume += ticks.get(i).getVolume();
		}
		
		return new CoinTick(ticks.get(0).getBeginTime(), 
							ticks.get(0).getOpenPrice(), 
							maxPrice, minPrice, 
							ticks.get(ticks.size() - 1).getClosePrice(), 
							volume);
	}

	private ArrayList<CoinTick> getSeries(int windowPeriod, boolean refresh)  throws HostCouldNotBeResolvedException, URLOpenConnectionException, JSONParsingException {

		ArrayList<CoinTick> arr = new ArrayList<CoinTick>();

		try {

			String URL = constructURL(windowPeriod);
			String filename = constructFilename(windowPeriod);

			JSONArray json;
			if (refresh)
				json = jsonReader.readJsonFromUrl(URL, filename);
			else
				json = jsonReader.readJson(URL, filename);

			for (int i = 0; i < json.length(); i++) {
				arr.add(new CoinTick(new DateTime((json.getJSONObject(i)
						.getLong("date") * 1000L), DateTimeZone
						.forID("Europe/Paris")), json.getJSONObject(i)
						.getDouble("open"), json.getJSONObject(i).getDouble(
						"high"), json.getJSONObject(i).getDouble("low"), json
						.getJSONObject(i).getDouble("close"), json
						.getJSONObject(i).getDouble("volume")));
			}

		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

		return arr;
	}

}
