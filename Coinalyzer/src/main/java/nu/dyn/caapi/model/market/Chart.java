package nu.dyn.caapi.model.market;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import eu.verdelhan.ta4j.series.DefaultTimeSeries;

public class Chart {
	public TimeSeries series;
	public DefaultOHLCDataset ohlcDataSet;
	String coinPairName;
	JFreeChart chart;
	XYPlot mainPlot;
    private DateAxis domainAxis = new DateAxis("Date");
    private NumberAxis rangeAxis = new NumberAxis("Price");
	
//	public ArrayList<Candlestick> candlesticks;
//	public long start;
//	public long end;
//	public int period;
//	
//	public Chart(ArrayList<Candlestick> c, int period) {
//		this.candlesticks = c;
//		this.period = period;
//		this.start = c.get(0).date;
//		this.end = c.get(c.size()-1).date;
//		
//	}
//	
	

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
    
    
	public Chart(String coinPairName, ArrayList<CoinTick> ticks) {
		this.coinPairName = coinPairName;
		
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
	}
	
	@Override
	public String toString() {

		String s = "["+series.getBegin()+"-"+series.getEnd()+":";
		
		for (int i=0; i<series.getSize(); i++)
			s += " "+series.getTick(i);
		
		return s+"]";
	}

	public void addIndicator(XYDataset set) {
		
		int n = mainPlot.getDatasetCount();
		
		mainPlot.setDataset(n, set);
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesStroke(n, new BasicStroke(1.0f));
		renderer.setDrawOutlines(false);
		mainPlot.setRenderer(n, renderer);

		
	}
	
	public JFreeChart getChart() {
		
		chart = new JFreeChart(coinPairName, null, mainPlot, true);
		return chart;
	}
	
	public void prepareChart() {
        
        CandlestickRenderer renderer = new CandlestickRenderer() {
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
        
        renderer.setDownPaint(Color.RED);
        renderer.setUpPaint(Color.GREEN);
        
        renderer.setDrawVolume(true);
       
        try {
        	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            domainAxis.setRange(new Date(dateFormat.parse("25/07/2014").getTime()), new Date(dateFormat.parse("28/07/2014").getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setAutoRange(true);
        mainPlot = new XYPlot(ohlcDataSet, domainAxis, rangeAxis, renderer);
        
        
        ///mainPlot.getDataRange(rangeAxis);
	}
	
//	@Override
//	public String toString() {
//
//		String s = "["+period+"s] ";
//		
//		for (Candlestick c: candlesticks) {
//			s += c;
//		}
//		return s+"\n";
//	}
}
