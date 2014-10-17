package nu.dyn.caapi.coinalyzer.chart;

import java.util.List;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;

public class NNErrorChart extends MyChart {

	
	
	/** Prepare the chart with input ticks on a coin for a time window
	 * @param coinPairName
	 * @param ticks
	 * @param t
	 */
	public NNErrorChart() {
		
		renderer = new XYSplineRenderer();
		domainAxis = new NumberAxis("Iterations");
		rangeAxis = new NumberAxis("Error");
		
		
	}

	public void addNNErrorChart(String name, List<Double> data) {
		
		XYSeries series = new XYSeries(name);
		for (int i=0; i<data.size(); i++)
			 series.add(i, data.get(i));
		
		dataset.addSeries(series);
		
	}
	
}
