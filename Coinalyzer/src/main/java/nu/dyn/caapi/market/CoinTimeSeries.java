package nu.dyn.caapi.market;

import java.util.ArrayList;

import org.jfree.data.time.TimeSeries;

public class CoinTimeSeries extends TimeSeries {
    private static final long serialVersionUID = 23184956238947L;

//	TimeSeries series;
	
	public CoinTimeSeries(String name, ArrayList<CoinTick> ticks) {
		super(name);
		data = ticks;
//		this.series = new DefaultTimeSeries(ticks);
		
		
	}
	
	@Override
	public String toString() {

		String s = "["+data.get(0)+"-"+data.get(data.size()-1)+":";
		
		for (int i=0; i<data.size(); i++)
			s += " "+data.get(i);
		
		return s+"]";
	}

}
