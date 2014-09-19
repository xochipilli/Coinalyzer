package nu.dyn.caapi.coinalyzer.utils;

import java.util.Calendar;
import java.util.Date;

import nu.dyn.caapi.coinalyzer.market.TimeWindow;

public class TimeframeUtils {
	public static TimeWindow getLastNWeeksTimeframe(int weeks) {
		
		Date date = new Date();
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
	    c.add(Calendar.DATE, -i - (7 * weeks));
	    Date start = c.getTime();
	    c.add(Calendar.DATE, 6);
	    Date end = c.getTime();
	    
	    return new TimeWindow(start, end);
	    
	}
	
public static TimeWindow getLastNDaysTimeframe(int days) {
		
		Date date = new Date();
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DAY_OF_MONTH, days*-1);
	    
	    return new TimeWindow(c.getTime(), date);
	    
	}

public static TimeWindow getLastNHoursTimeframe(int hours) {
	
	Date date = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.HOUR_OF_DAY, hours*-1);
    
    return new TimeWindow(c.getTime(), date);
    
}

}
