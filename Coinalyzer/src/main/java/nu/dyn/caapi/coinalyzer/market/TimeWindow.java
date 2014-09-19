package nu.dyn.caapi.coinalyzer.market;

import java.util.Date;

public class TimeWindow {
	
	private Date start;
	private Date end;
	private int period;
	
	public TimeWindow (Date start, Date end, int period) {
		this.start = start;
		this.end = end;
		this.period = period;
	}
	
	public TimeWindow (Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	
	@Override
	public String toString() {

		return "["+getStart()+"-"+getEnd()+" @ "+getPeriod()+"]";
		
	}

	
}
