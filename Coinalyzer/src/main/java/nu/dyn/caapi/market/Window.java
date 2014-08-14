package nu.dyn.caapi.market;

import java.util.Date;


public class Window {
	
	private Date start;
	private Date end;
	private int length;
	
	public Window (Date start, Date end, int period) {
		this.start = start;
		this.end = end;
		this.length = period;
	}
	
	public Window (Date start, Date end) {
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
	public int getLength() {
		return length;
	}
	public void setLength(int period) {
		this.length = period;
	}
	
	@Override
	public String toString() {

		return "["+start.toString()+"-"+end.toString()+" @ "+length+"]";
		
	}

	
}
