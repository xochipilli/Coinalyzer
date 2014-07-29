package nu.dyn.caapi.model.market;


public class Timeframe {
	
	private long start;
	private long end;
//	private int period;
	
	public Timeframe (long start, long end) {
		this.start = start;
		this.end = end;
		//this.period = period;
	}
	
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
//	public int getPeriod() {
//		return period;
//	}
//	public void setPeriod(int period) {
//		this.period = period;
//	}
	
	
}
