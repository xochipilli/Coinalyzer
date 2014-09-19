package nu.dyn.caapi.coinalyzer.market;

import java.util.HashMap;

import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;

public class MyIndicator<N> {
	
	private Indicator<N> i;
	private String name;
	private HashMap<String, Integer> params;

	/**
	 * @param name Description shown on chart
	 */
	public MyIndicator(String name) {
		super();
		this.name = name;
	}

	/** Calculate the indicator for chart with closePrice data
	 * @param closePrice
	 * @return Indicator with unknown name?
	 */
	public boolean init(ClosePriceIndicator closePrice) {
		switch(name) {
			case "EMA":
				i = (Indicator) new EMAIndicator(closePrice,  getParam("value"));
				break;
			case "SMA":
				i = (Indicator) new EMAIndicator(closePrice,  getParam("value"));
				break;
			default:
				return true;
		}
		
		return false;
	}

	public Integer getParam(String key) {
		return params.get(key);
	}
	public HashMap<String, Integer> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Integer> params) {
		this.params = params;
	}
	public Indicator<N> getI() {
		return i;
	}

	public void setI(Indicator<N> i) {
		this.i = i;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
