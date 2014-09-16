package nu.dyn.caapi.coinalyzer.market;

import java.io.IOException;
import java.util.ArrayList;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.exceptions.HostCouldNotBeResolvedException;
import nu.dyn.caapi.coinalyzer.exceptions.JSONParsingException;
import nu.dyn.caapi.coinalyzer.exceptions.URLOpenConnectionException;
import nu.dyn.caapi.coinalyzer.utils.MyJsonReader;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Market {

	private CoinPairInfo coinPair;
	public Chart chart;
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
	Window t;
	MyJsonReader jsonReader;

	protected abstract String constructURL(int windowLength);

	protected abstract String constructFilename(int windowLength);

	public Market(CoinPairInfo coinPair, Window t, MyJsonReader jsonReader) {

		this.coinPair = coinPair;
		this.t = t;
		this.jsonReader = jsonReader;
		
	}
	
	public void setPeriod(int period) throws JSONParsingException, URLOpenConnectionException, HostCouldNotBeResolvedException{

		t.setLength(period);
		chart = new Chart(coinPair.getCurrencyPairId(),
				getSeries(period, false), t);

	}

	public void getAllSeries(boolean refresh) throws JSONParsingException, URLOpenConnectionException, HostCouldNotBeResolvedException {

		series_5m = getSeries(Constants.period_5m, refresh);
		
		series_15m = getSerieWithPeriodicity(3, series_5m);
		series_30m = getSerieWithPeriodicity(6, series_5m);
		series_2h = getSerieWithPeriodicity(24, series_5m);
		series_4h = getSerieWithPeriodicity(48, series_5m);

		chart = new Chart(coinPair.getCurrencyPairId(), series_5m, t); 

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

	public ArrayList<CoinTick> getSeries(int windowLength, boolean refresh)  throws HostCouldNotBeResolvedException, URLOpenConnectionException, JSONParsingException {

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
