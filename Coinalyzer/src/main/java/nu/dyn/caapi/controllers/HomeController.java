package nu.dyn.caapi.controllers;

import java.io.OutputStream;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import nu.dyn.caapi.bot.AppConfig;
import nu.dyn.caapi.model.market.Constants;
import nu.dyn.caapi.model.market.MarketClient;
import nu.dyn.caapi.utils.TimeframeUtils;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
	private MarketClient client;

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	// @ModelAttribute("AppConfig")
	// public AppConfig getAppConfig() {
	// return appConfig;
	// }

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/",  method = RequestMethod.GET )
	public String home(Locale locale, Model model, HttpServletRequest request) {
		
		logger.info("Welcome home! The client locale is {}.", locale);
		
		if (request.getParameter("timeframe")!=null) {
			if (request.getParameter("timeframe").equals("1m")) {
				client.setChartRange(TimeframeUtils.getLastNWeeksTimeframe(4));
			} else if (request.getParameter("timeframe").equals("2w")) {
				client.setChartRange(TimeframeUtils.getLastNWeeksTimeframe(2));
			} else if (request.getParameter("timeframe").equals("1w")) {
				client.setChartRange(TimeframeUtils.getLastNWeeksTimeframe(1));
			} else if (request.getParameter("timeframe").equals("4d")) {
				client.setChartRange(TimeframeUtils.getLastNDaysTimeframe(4));
			} else if (request.getParameter("timeframe").equals("2d")) {
				client.setChartRange(TimeframeUtils.getLastNDaysTimeframe(2));
			} else if (request.getParameter("timeframe").equals("24h")) {
				client.setChartRange(TimeframeUtils.getLastNDaysTimeframe(1));
			} else if (request.getParameter("timeframe").equals("6h")) {
				client.setChartRange(TimeframeUtils.getLastNHoursTimeframe(6));
			}
		}
		
		if (request.getParameter("period")!=null) {
			if (request.getParameter("period").equals("5m")) {
				client.setChartPeriod(Constants.period_5m);
			} else if (request.getParameter("period").equals("15m")) {
				client.setChartPeriod(Constants.period_15m);
			}  else if (request.getParameter("period").equals("30m")) {
				client.setChartPeriod(Constants.period_30m);
			}  else if (request.getParameter("period").equals("2h")) {
				client.setChartPeriod(Constants.period_2h);
			}  else if (request.getParameter("period").equals("4h")) {
				client.setChartPeriod(Constants.period_4h);
			}
			
			
		}
		
		if (request.getParameter("refresh")!=null)
			client.market.getChart(true);

		return "home";

	}
	
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public String config(Locale locale, Model model) {
		if (client.appConfig == null)
			return "errors/noconfigfile";
		else {

			model.addAttribute("AppConfig", client.appConfig);
			return "configuration";
		}

	}

	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public String config(@ModelAttribute AppConfig c, Model model) {
		// ModelAndView view = new ModelAndView();

		try {
			client.appConfig.update(c);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("AppConfig", client.appConfig);
		model.addAttribute("message", "New Configuration saved.");
		
		return "configuration";
	}

	@RequestMapping(value = "/refresh_chart", method = RequestMethod.POST)
	public String refresh_chart() {
		
		client.getChart();
		
		return "home";
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

		stream.write(client.getChart());

	}
}
