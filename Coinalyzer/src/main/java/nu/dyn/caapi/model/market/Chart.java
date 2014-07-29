package nu.dyn.caapi.model.market;

import java.util.ArrayList;

import org.jfree.data.xy.DefaultOHLCDataset;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.series.DefaultTimeSeries;

public class Chart {
	TimeSeries series;
	DefaultOHLCDataset candlestickChart;
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
	public Chart(ArrayList<CoinTick> ticks) {
		this.series = new DefaultTimeSeries(ticks);
	}
	
	@Override
	public String toString() {

		String s = "["+series.getBegin()+"-"+series.getEnd()+":";
		
		for (int i=0; i<series.getSize(); i++)
			s += " "+series.getTick(i);
		
		return s+"]";
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
