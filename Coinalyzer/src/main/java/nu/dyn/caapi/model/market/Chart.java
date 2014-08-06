package nu.dyn.caapi.model.market;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.activemq.util.ByteArrayOutputStream;
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

import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.series.DefaultTimeSeries;

public class Chart {
	public TimeSeries series;
	public DefaultOHLCDataset ohlcDataSet;
	String coinPairName;
	JFreeChart chart;
	public byte[] PNGChart;
	private XYPlot mainPlot;
    private DateAxis domainAxis = new DateAxis("Date");
    private NumberAxis rangeAxis = new NumberAxis("Price");
    private CandlestickRenderer renderer;
	private Timeframe timeframe;
	
    /**
	* Builds a JFreeChart time series from a Ta4j time series and an indicator.
	* @param tickSeries the ta4j time series
	* @param indicator the indicator
	* @param name the name of the chart time series
	* @return the JFreeChart time series collection
	*/
    public static  org.jfree.data.time.TimeSeriesCollection buildChartTimeSeries(TimeSeries tickSeries, Indicator<Double> indicator, String name) {
    	
    	org.jfree.data.time.TimeSeries series = new org.jfree.data.time.TimeSeries(name);
    	for (int i = 0; i < tickSeries.getSize(); i++) {
            Tick tick = tickSeries.getTick(i);
            
            series.add(new Minute(tick.getBeginTime().toDate()), indicator.getValue(i));
        }
        return new TimeSeriesCollection(series);
        
    }
    
	public Chart(String coinPairName, ArrayList<CoinTick> ticks, Timeframe t) {
		
		this.coinPairName = coinPairName;
		this.timeframe = t;
		this.series = new DefaultTimeSeries(ticks);
		
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

	void addIndicators() {
		
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		SMAIndicator i_sma50 = new SMAIndicator(closePrice, 50);
		SMAIndicator i_sma20 = new SMAIndicator(closePrice, 20);
		EMAIndicator i_ema20 = new EMAIndicator(closePrice, 20);

		addIndicator(Chart.buildChartTimeSeries(series, i_sma50, "50 SMA"));
		addIndicator(Chart.buildChartTimeSeries(series, i_sma20, "20 SMA"));
		addIndicator(Chart.buildChartTimeSeries(series, i_ema20, "20 EMA"));
		
	}

	void addIndicator(XYDataset set) {
		
		int n = mainPlot.getDatasetCount();
		
		mainPlot.setDataset(n, set);
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesStroke(n, new BasicStroke(1.0f));
		renderer.setDrawOutlines(false);
		mainPlot.setRenderer(n, renderer);

	}
	
	/**
	* Get previously generated chart 
	* @return byte[] PNG image of chart
	*/
	public byte[] getChart(boolean rePlot) {

		if (rePlot || chart==null)
			plotChart();
		    
		return PNGChart;
	}

	/**
	* Generate and return chart with specified date range from - to 
	* @param from Start date 
	* @param to End date
	* @return byte[] PNG image of chart
	*/
	public byte[] getChart(Date from, Date to) {
		
		timeframe.setStart(from);
		timeframe.setEnd(to);
		
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
	*/
	void plotChart() {
		System.out.println("reploting");
		
		domainAxis.setRange(timeframe.getStart(), timeframe.getEnd());
		
		mainPlot = new XYPlot(ohlcDataSet, domainAxis, rangeAxis, renderer);
		
		chart = new JFreeChart(coinPairName, null, mainPlot, true);
		
		addIndicators();
		
		writePNG();
		
	}
	
	public void writePNG() {
	    try {
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			ChartUtilities.writeChartAsPNG(out, chart, 800, 400);
			PNGChart = out.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
