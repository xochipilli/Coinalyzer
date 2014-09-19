package nu.dyn.caapi.coinalyzer.market;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.exceptions.PNGChartCreationException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.series.DefaultTimeSeries;

@Service
public class Chart {
	public TimeSeries series;

	//private HashMap<String, MyIndicator<?>> indicators; 

	public DefaultOHLCDataset ohlcDataSet;

	String coinPairName;
	
	@Autowired
	AppConfig appConfig;
	
	JFreeChart chart;
	public byte[] PNGChart;
	private XYPlot mainPlot;
    private DateAxis domainAxis = new DateAxis("Date");
    private NumberAxis rangeAxis = new NumberAxis("Price");
    private CandlestickRenderer renderer;
	public TimeWindow window;
	
	
	/** Prepare the chart with input ticks on a coin for a time window
	 * @param coinPairName
	 * @param ticks
	 * @param t
	 */
	public void init(String coinPairName, ArrayList<CoinTick> ticks, TimeWindow t) {
		
		this.coinPairName = coinPairName;
		this.window = t;
		this.series = new DefaultTimeSeries(ticks);
		
		// convert ticks to ohlcd dats set
		OHLCDataItem[] data = new OHLCDataItem[ticks.size()];
		for (int i=0; i<ticks.size(); i++) {
			 data[i] = new OHLCDataItem(
					ticks.get(i).getBeginTime().toDate(), 
					ticks.get(i).getOpenPrice(), 
					ticks.get(i).getMaxPrice(), 
					ticks.get(i).getMinPrice(), 
					ticks.get(i).getClosePrice(), 
					ticks.get(i).getVolume());
			 
		}
		ohlcDataSet = new DefaultOHLCDataset(coinPairName, data);
	
		prepareChart();
	}

	/**
	* Builds a JFreeChart time series from a Ta4j time series and an indicator.
	* @param tickSeries the ta4j time series
	* @param indicator the indicator
	* @param name the name of the chart time series
	* @return the JFreeChart time series collection
	*/
    public static org.jfree.data.time.TimeSeriesCollection buildChartTimeSeries(TimeSeries tickSeries, Indicator<Double> indicator, String name) {
    	
    	org.jfree.data.time.TimeSeries series = new org.jfree.data.time.TimeSeries(name);
    	for (int i = 0; i < tickSeries.getSize(); i++) {
            Tick tick = tickSeries.getTick(i);
            
            series.add(new Minute(tick.getBeginTime().toDate()), indicator.getValue(i));
        }
        return new TimeSeriesCollection(series);
        
    }
    
	/** Calculate and plot indicators for current chart
	 * 
	 */
	private void initIndicators() {
		
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		
		for (int k=0; k < appConfig.indicators.size(); k++) {
		
			appConfig.indicators.get(k).init(closePrice);
			
			plotIndicator(appConfig.indicators.get(k));
			
		}
	}
	
	/**
	 * Add indicator with name to current chart 
	 * @param ind TA4j indicator 
	 * @param name Description
	 */
	private void plotIndicator(MyIndicator indicator) {
		
		int n = mainPlot.getDatasetCount();
		mainPlot.setDataset(n, Chart.buildChartTimeSeries(series, indicator.getI(), indicator.getName()) );
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesStroke(n, new BasicStroke(1.0f));
		renderer.setDrawOutlines(false);
		mainPlot.setRenderer(n, renderer);

	}
	
	
	/**
	* Generate chart 
	* @param Plot new chart if true
	* @return byte[] PNG image of chart
	 * @throws PNGChartCreationException 
	*/
	public byte[] getChart(boolean rePlot) throws PNGChartCreationException {

		if (rePlot || chart==null)
			plotChart();
		    
		return PNGChart;
	}

	/**
	* Generate and return chart with specified date range from - to 
	* @param from Start date 
	* @param to End date
	* @param Indicators
	* @return byte[] PNG image of chart
	 * @throws PNGChartCreationException 
	*/
	public byte[] getChart(Date from, Date to) throws PNGChartCreationException {
		
		window.setStart(from);
		window.setEnd(to);
		
		return getChart(true);
	}
	
	public void prepareChart() {
        
        renderer = new CandlestickRenderer() {
        	private static final long serialVersionUID = 1L;

			// set the border color to be the same as the body
        	@Override
        	public Paint getItemPaint(int row, int column) {
        		//determine up or down candle 
        	    XYDataset dataset = getPlot().getDataset();
        	    OHLCDataset highLowData = (OHLCDataset) dataset;
        	    int series = row, item = column;
        	    Number yOpen = highLowData.getOpen(series, item);
        	    Number yClose = highLowData.getClose(series, item);
        	    boolean isUpCandle = yClose.doubleValue() > yOpen.doubleValue();

        	    //return the same color as that used to fill the candle
        	    if (isUpCandle)
        	        return getUpPaint();
        	    else
        	        return getDownPaint();
        	}
        };
        renderer.setUseOutlinePaint(false);
        renderer.setDrawVolume(true);
        renderer.setDownPaint(Color.RED);
        renderer.setUpPaint(Color.GREEN);
        
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setAutoRange(true);
        
    	//plotChart();
		
	}
	
	/**
	* Plot chart with specified date range from - to 
	* @param from Start date 
	* @param to End date
	 * @throws PNGChartCreationException 
	*/
	void plotChart() throws PNGChartCreationException {
		System.out.println("reploting");
		
		domainAxis.setRange(window.getStart(), window.getEnd());
		
		mainPlot = new XYPlot(ohlcDataSet, domainAxis, rangeAxis, renderer);
		
		chart = new JFreeChart(coinPairName, null, mainPlot, true);
		
		initIndicators();
		
		writePNG();
		
	}
	
	public void writePNG() throws PNGChartCreationException {
	    try {
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			ChartUtilities.writeChartAsPNG(out, chart, 800, 400);
			PNGChart = out.toByteArray();
		} catch (IOException e) {
			throw new PNGChartCreationException("Error writing chart as PNG", e);
		}
	}
	    
	@Override
	public String toString() {

		String s = "["+series.getBegin()+"-"+series.getEnd()+":";
		
		for (int i=0; i<series.getSize(); i++)
			s += " "+series.getTick(i);
		
		return s+"]";
	}

}
