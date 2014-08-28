package nu.dyn.caapi.coinalyzer.market;

import java.io.IOException;
import java.util.ArrayList;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.utils.MyJsonReader;

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

	MyJsonReader jsonReader;
	
	// public ArrayList<Series> series_5m_indicators;
	// public ArrayList<Series> series_15m_indicators;
	// public ArrayList<Series> series_30m_indicators;
	// public ArrayList<Series> series_2h_indicators;
	// public ArrayList<Series> series_4h_indicators;
	//
	Window t;

	protected abstract String constructURL(int windowLength);

	protected abstract String constructFilename(int windowLength);

	public Market(CoinPairInfo coinPair, Window t, AppConfig appConfig) {

		this.coinPair = coinPair;
		this.t = t;
		
		initJsonReader(appConfig.proxyHost, appConfig.proxyPort);
		
	}

	private void initJsonReader(String proxyHost, int proxyPort) {
		
		jsonReader = new MyJsonReader(proxyHost, proxyPort);

	}
	
	public void setPeriod(int period) throws Exception {

		t.setLength(period);
		chart = new Chart(coinPair.getCurrencyPairId(),
				getSeries(period, false), t);

	}

	public void getAllSeries(boolean refresh) throws Exception {

		series_5m = getSeries(Constants.period_5m, refresh);
		
		series_15m = getSerieWithPeriodicity(3, series_5m);
		series_30m = getSerieWithPeriodicity(6, series_5m);
		series_2h = getSerieWithPeriodicity(24, series_5m);
		series_4h = getSerieWithPeriodicity(48, series_5m);

		chart = new Chart(coinPair.getCurrencyPairId(), series_5m, t); 

	}

	private  ArrayList<CoinTick> getSerieWithPeriodicity(int periodMultiplicator, ArrayList<CoinTick> base5mSerie) {

		ArrayList<CoinTick> s = new ArrayList<CoinTick>();
		
		for (int i = 1; i <= base5mSerie.size(); i++) {
			if (i % periodMultiplicator == 0) {
				ArrayList<CoinTick> ticks = new ArrayList<CoinTick>();
				for (int j = 0; j < periodMultiplicator; j++) {
					ticks.add(base5mSerie.get( base5mSerie.size() - i - j));
				}
				s.add(mergeCoinTicks(ticks));
			}
		}
		
		return s;

	}
	
	// return one tick for from all provided ticks
	private CoinTick mergeCoinTicks(ArrayList<CoinTick> ticks) {
		
		double maxPrice = 0D;
		double minPrice = 0D;
		double volume = 0D;

		for (int i = 0; i < ticks.size(); i++) {
			if (ticks.get(i).getMaxPrice() > maxPrice)
				maxPrice = ticks.get(i).getMaxPrice();
			if (ticks.get(i).getMinPrice() > minPrice)
				minPrice = ticks.get(i).getMinPrice();
			volume += ticks.get(i).getVolume();
		}
		
		return new CoinTick(ticks.get(0).getBeginTime(), 
							ticks.get(0).getOpenPrice(), 
							maxPrice, minPrice, 
							ticks.get(ticks.size() - 1).getClosePrice(), 
							volume);
	}

	public ArrayList<CoinTick> getSeries(int windowLength, boolean refresh) throws Exception {

		ArrayList<CoinTick> arr = new ArrayList<CoinTick>();

		try {

			String URL = constructURL(windowLength);
			String filename = constructFilename(windowLength);

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

	@Override
	public String toString() {
		return chart.toString(); // TODO:
									// _5m+chart_15m+chart_30m+chart_1h+chart_2h+chart_4h;
	}

}
