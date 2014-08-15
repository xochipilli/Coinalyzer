package nu.dyn.caapi.nn;

import eu.verdelhan.ta4j.Tick;

public class TrainDataItem {
	public double i_open;
	public double i_low;
	public double i_high;
	public double i_close;
	public double i_volume;
	
	public double norm_i_open;
	public double norm_i_low;
	public double norm_i_high;
	public double norm_i_close;
	public double norm_i_volume;
	
	public double o_open;
	public double o_low;
	public double o_high;
	public double o_close;
	public double o_volume;
	
	public double[] indicators;
	
	public TrainDataItem(Tick in, Tick out) { //, double[] indicators) {
		
		i_open = out.getOpenPrice();
		i_low = out.getMinPrice();
		i_high = out.getMaxPrice();
		i_close = out.getClosePrice();
		i_volume = out.getVolume();
		
		o_open = out.getOpenPrice();
		o_low = out.getMinPrice();
		o_high = out.getMaxPrice();
		o_close = out.getClosePrice();
		o_volume = out.getVolume();
		
		//TODO: incorporate indicators
		//this.indicators = indicators;
		
	}
	
	public double[] getOutputArray() {
		
		return new double[] { o_open, o_low, o_high, o_close, o_volume }; 
		
	}

	public double[] getInputArray() {

		//return ArrayUtils.addAll(new double[] { i_open, i_low, i_high, i_close, i_volume }, indicators);
		return new double[] { i_open, i_low, i_high, i_close, i_volume };
		
	}
	
	public double[] getNormalizedInputArray() {

		return new double[] { norm_i_open, norm_i_low, norm_i_high, norm_i_close, norm_i_volume };
		
	}


}
