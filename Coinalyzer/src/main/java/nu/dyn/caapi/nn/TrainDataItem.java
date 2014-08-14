package nu.dyn.caapi.nn;

import org.apache.commons.lang3.ArrayUtils;

import eu.verdelhan.ta4j.Tick;

public class TrainDataItem {
	public double i_open;
	public double i_low;
	public double i_high;
	public double i_close;
	public double i_volume;
	
	public double o_open;
	public double o_low;
	public double o_high;
	public double o_close;
	public double o_volume;
	
	public double[] indicators;
	
	public TrainDataItem(Tick input, Tick output) { //, double[] indicators) {
	
		i_open = input.getOpenPrice();
		i_low = input.getMinPrice();
		i_high = input.getMaxPrice();
		i_close = input.getClosePrice();
		i_volume = input.getVolume();
		
		o_open = output.getOpenPrice();
		o_low = output.getMinPrice();
		o_high = output.getMaxPrice();
		o_close = output.getClosePrice();
		o_volume = output.getVolume();
		
		//TODO: incorporate indicators
		//this.indicators = indicators;
		
	}
	
	public double[] getOutputArray() {
		
		return new double[] { o_open, o_low, o_high, o_close, o_volume }; 
		
	}

	public double[] getInputArray() {

		return ArrayUtils.addAll(new double[] { i_open, i_low, i_high, i_close, i_volume }, indicators);
		
	}

}
