package nu.dyn.caapi.coinalyzer.market;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.exceptions.PNGChartCreationException;
import nu.dyn.caapi.coinalyzer.market.exchanges.Poloniex;
import nu.dyn.caapi.coinalyzer.utils.MyJsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service	
public class MarketClient {
	@Autowired 
	private AppConfig appConfig;
	
	public Market market;
	public boolean initialized = false;

	public void init() throws Exception {

		CoinPairInfo coinPair = new CoinPairInfo(appConfig.coinPrimary, appConfig.coinCounter);

		try {
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Window timeframe = new Window(dateFormat.parse("1/1/2013"),
					dateFormat.parse("1/1/2015"), Constants.period_15m);

			market = new Poloniex(coinPair, timeframe, new MyJsonReader(appConfig));
			market.getAllSeries(false);
			
			initialized = true;
			
		} catch (ParseException e) {
			// TODO:
			System.out.println("Error: " + e);
		}
	}
	
	public byte[] getChart() throws PNGChartCreationException {

		return market.chart.getChart(false);

	}

	public byte[] getRefreshedChart() throws Exception {
		
		market.getAllSeries(true);
		
		return market.chart.getChart(true);

	}

	public void setChartRangeAll() throws PNGChartCreationException {
		market.chart.getChart(
			market.chart.series.getTick(0).getBeginTime().toDate(),
			market.chart.series.getTick(market.chart.series.getSize()-1).getBeginTime().toDate()
		);
	}

	public void setChartRange(Window t) throws PNGChartCreationException {

		market.chart.getChart(t.getStart(), t.getEnd());

	}

	public void setChartPeriod(int period) throws Exception {

		market.setPeriod(period);

	}

}
