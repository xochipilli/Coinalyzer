package nu.dyn.caapi.coinalyzer.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	
	/** Parameter setter for indicators requiring one parameter only */
	public void setSingleParam(String key, Integer value) {
		
		HashMap<String, Integer> p = new HashMap<String, Integer>();
		p.put(key, value);
		
	}
	
	public Integer getParam(String key) {
		return params.get(key);
	}
	
	public HashMap<String, Integer> getParams() {
	
		return params;
	}

	public List<String> listParams() {
		
		return new ArrayList<String>(params.keySet());
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
