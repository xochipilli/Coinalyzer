package nu.dyn.caapi.coinalyzer.market;

import java.util.HashMap;

import eu.verdelhan.ta4j.Indicator;

public class MyIndicator<N> {
	
	private Indicator<N> i;
	private String name;
	private HashMap<String, ?> params;

	/**
	 * @param name Description shown on chart
	 */
	public MyIndicator(String name) {
		super();
		this.name = name;
	}

	public HashMap<String, ?> getParams() {
		return params;
	}

	public void setParams(HashMap<String, ?> params) {
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
		name = name;
	}
	
}
