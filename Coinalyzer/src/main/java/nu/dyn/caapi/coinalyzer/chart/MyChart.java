package nu.dyn.caapi.coinalyzer.chart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nu.dyn.caapi.coinalyzer.exceptions.PNGChartCreationException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeriesCollection;

public abstract class MyChart {
	
	JFreeChart chart;
	byte[] PNGChart;

	XYSeriesCollection dataset = new XYSeriesCollection();
	XYItemRenderer renderer;
	private XYPlot mainPlot;
	ValueAxis domainAxis;
	ValueAxis rangeAxis;

	/**
	 * Plot chart with specified date range from - to
	 * 
	 * @param from
	 *            Start date
	 * @param to
	 *            End date
	 * @throws PNGChartCreationException
	 */
	public void plotChart(String name) throws PNGChartCreationException {

//		domainAxis.setRange(window.getStart(), window.getEnd());

		mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

		chart = new JFreeChart(name, null, mainPlot, true);

		writePNG();

	}

	public void writePNG() throws PNGChartCreationException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ChartUtilities.writeChartAsPNG(out, chart, 800, 400);
			PNGChart = out.toByteArray();
		} catch (IOException e) {
			throw new PNGChartCreationException("Error writing chart as PNG", e);
		}
	}
	
	public byte[] getPNGChart() {
		return PNGChart;
	}

}
