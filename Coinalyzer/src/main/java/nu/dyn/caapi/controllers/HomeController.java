package nu.dyn.caapi.controllers;

import java.io.OutputStream;
import java.util.Locale;

import javax.annotation.PostConstruct;

import nu.dyn.caapi.bot.AppConfig;
import nu.dyn.caapi.model.market.Chart;
import nu.dyn.caapi.model.market.MarketClient;
import nu.dyn.caapi.utils.ConfigParser;

import org.apache.commons.configuration.ConfigurationException;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
	private AppConfig appConfig;

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	// @ModelAttribute("AppConfig")
	// public AppConfig getAppConfig() {
	// return appConfig;
	// }

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		return "home";

	}

	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public String config(Locale locale, Model model) {
		if (appConfig == null)
			return "errors/noconfigfile";
		else {

			model.addAttribute("AppConfig", appConfig);
			return "configuration";
		}

	}

	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public String config(@ModelAttribute AppConfig c, Model model) {
		// ModelAndView view = new ModelAndView();

		try {
			appConfig.update(c);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("AppConfig", appConfig);
		model.addAttribute("message", "New Configuration saved.");
		
		return "configuration";
	}

	@PostConstruct
	public void init() {
		/*
		 * try { ConfigParser.parse(AppConfig.configurationFile, appConfig);
		 * 
		 * } catch (ConfigurationException e) {
		 * System.out.println("Could not load configuration from " +
		 * AppConfig.configurationFile); }
		 */}

	@RequestMapping("/chart.png")
	public void renderChart(OutputStream stream) throws Exception {

		MarketClient client = new MarketClient(appConfig);
		client.market.chart.prepareChart();

		ClosePriceIndicator closePrice = new ClosePriceIndicator(
				client.market.chart.series);
		SMAIndicator i_sma50 = new SMAIndicator(closePrice, 50);
		SMAIndicator i_sma20 = new SMAIndicator(closePrice, 20);
		EMAIndicator i_ema20 = new EMAIndicator(closePrice, 20);

		client.market.chart.addIndicator(Chart.buildChartTimeSeries(
				client.market.chart.series, i_sma50, "50 SMA"));
		client.market.chart.addIndicator(Chart.buildChartTimeSeries(
				client.market.chart.series, i_sma20, "20 SMA"));
		client.market.chart.addIndicator(Chart.buildChartTimeSeries(
				client.market.chart.series, i_ema20, "20 EMA"));

		JFreeChart chart = client.market.chart.getChart();

		ChartUtilities.writeChartAsPNG(stream, chart, 800, 400);

	}
}
