package nu.dyn.caapi.coinalyzer.market;

import java.util.HashMap;
import java.util.Set;

public class Constants {

	public static int period_5m = 300;
	public static int period_15m = 900;
	public static int period_30m = 1800;
	public static int period_2h = 14400;
	public static int period_4h = 86400;
	
	public static final HashMap<String, MyIndicator<?>> allIndicators = new HashMap<String, MyIndicator<?>>();
	static {
		MyIndicator<Double> i;
		
		i = new MyIndicator<Double>("SMA");
		i.setSingleParam("value", 20);
		allIndicators.put(i.getName(), i);

		i = new MyIndicator<Double>("EMA");
		i.setSingleParam("value", 20);
		allIndicators.put(i.getName(), i);
		
		i = new MyIndicator<Double>("RSI");
		allIndicators.put(i.getName(), i);

		i = new MyIndicator<Double>("MACD");
		allIndicators.put(i.getName(), i);	
	}

	public static Set<String> getAllIndicators() {
		return allIndicators.keySet();
	}

	public static int getPeriod_5m() {
		return period_5m;
	}
	public static int getPeriod_15m() {
		return period_15m;
	}
	public static int getPeriod_30m() {
		return period_30m;
	}
	public static int getPeriod_2h() {
		return period_2h;
	}
	public static int getPeriod_4h() {
		return period_4h;
	}
}
