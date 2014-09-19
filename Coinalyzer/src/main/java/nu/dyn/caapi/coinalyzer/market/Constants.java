package nu.dyn.caapi.coinalyzer.market;

import java.util.HashMap;

public class Constants {
	public static int period_5m = 300;
	public static int period_15m = 900;
	public static int period_30m = 1800;
	//public static int period_1h = 7200;
	public static int period_2h = 14400;
	public static int period_4h = 86400;
	
	public static final HashMap<String, String> indicators;
	static {
		indicators = new HashMap<String, String>();
		indicators.put("ema", "EMA");
		indicators.put("sma", "SMA");
		indicators.put("rsi", "RSI");
		indicators.put("macd", "MACD");
	}
}
