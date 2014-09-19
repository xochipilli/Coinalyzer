package nu.dyn.caapi.coinalyzer.market;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.exceptions.PNGChartCreationException;
import nu.dyn.caapi.coinalyzer.market.exchanges.Poloniex;
import nu.dyn.caapi.coinalyzer.utils.MyJsonReader;
import nu.dyn.caapi.coinalyzer.utils.TimeframeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ManagedBean
@SessionScoped
@Component
public class MarketClient {
	@Autowired 
	private AppConfig appConfig;
	@Autowired 
	private Chart chart;
	
	CoinPairInfo coinPair;

	public Market market;
	public boolean initialized = false;

	public String timeframe_str = "all";
	
	public void init() throws Exception {

		try {
			
			coinPair = new CoinPairInfo(appConfig.coinPrimary, appConfig.coinCounter);
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			TimeWindow timeframe = new TimeWindow(dateFormat.parse("1/1/2013"),
					dateFormat.parse("1/1/2015"), Constants.period_5m);

			market = new Poloniex(timeframe, new MyJsonReader(appConfig), coinPair);
			
			market.getAllSeries(false);
			
			plotChartForPeriod(timeframe.getPeriod());
			
			initialized = true;
			
		} catch (ParseException e) {
			// TODO:
			System.out.println("Error: " + e);
		}
	}
	
	public byte[] getChart() throws PNGChartCreationException {

		return chart.getChart(false);

	}

	public byte[] getRefreshedChart() throws Exception {
		
		market.getAllSeries(true);
		
		return chart.getChart(true);

	}
 
	public void setTimeframeAll() throws PNGChartCreationException {
		chart.getChart(
			chart.series.getTick(0).getBeginTime().toDate(),
			chart.series.getTick(chart.series.getSize()-1).getBeginTime().toDate()
		);
	}

	public void setChartRange(String t) throws PNGChartCreationException {

		if (t==null)
			return;
		
		if (t.equals("all")) {
			setTimeframeAll();
		} else if (t.equals("1m")) {
			setTimeframe(TimeframeUtils.getLastNDaysTimeframe(30), t);
		} else if (t.equals("2w")) {
			setTimeframe(TimeframeUtils.getLastNDaysTimeframe(14), t);
		} else if (t.equals("1w")) {
			setTimeframe(TimeframeUtils.getLastNDaysTimeframe(7), t);
		} else if (t.equals("4d")) {
			setTimeframe(TimeframeUtils.getLastNDaysTimeframe(4), t);
		} else if (t.equals("2d")) {
			setTimeframe(TimeframeUtils.getLastNDaysTimeframe(2), t);
		} else if (t.equals("24h")) {
			setTimeframe(TimeframeUtils.getLastNDaysTimeframe(1), t);
		} else if (t.equals("6h")) {
			setTimeframe(TimeframeUtils.getLastNHoursTimeframe(6), t);
		}

	}

	private void setTimeframe(TimeWindow t, String str) throws PNGChartCreationException {
	
		timeframe_str = str;
		chart.getChart(t.getStart(), t.getEnd());
	
	}

	public String getTimeframe() {
		
		return timeframe_str;
	}
	
	public void plotChartForPeriod(int period) throws Exception {

		market.t.setPeriod(period);
		chart.init(coinPair.getCurrencyPairId(), market.getCurrentSeries(),  market.t);
		chart.getChart(true);
	}

	public int getCurrentPeriod() {
		return market.t.getPeriod();
	}
}
