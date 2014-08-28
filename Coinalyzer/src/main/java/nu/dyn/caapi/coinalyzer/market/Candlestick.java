package nu.dyn.caapi.coinalyzer.market;

import org.json.JSONObject;

public class Candlestick {

	public long date;
	public double high;
	public double low;
	public double open;
	public double close;
	public double volume;
	public double quoteVolume;

	public Candlestick(long date, double high, double low,
			double open, double close, double volume, double quoteVolume) {

		this.date = date;
		this.high = high;
		this.low = low;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.quoteVolume = quoteVolume;
	}
	
	public Candlestick(JSONObject j) {
		
		this.date = j.getLong("date");
		this.high = j.getDouble("high");
		this.low = j.getDouble("low");
		this.open = j.getDouble("open");
		this.close = j.getDouble("close");
		this.volume = j.getDouble("volume");
		this.quoteVolume = j.getDouble("quoteVolume");
	}
	
	@Override
	public String toString() {
		return high+"<"+open+"-"+close+">"+low+" ";
	}
	
}