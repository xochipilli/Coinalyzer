package nu.dyn.caapi.coinalyzer.controllers;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.exceptions.DescribedIOException;
import nu.dyn.caapi.coinalyzer.exceptions.LoggedException;
import nu.dyn.caapi.coinalyzer.exceptions.PNGChartCreationException;
import nu.dyn.caapi.coinalyzer.market.Constants;
import nu.dyn.caapi.coinalyzer.market.MarketClient;
import nu.dyn.caapi.coinalyzer.model.Analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
	private MarketClient client;
	@Autowired
	private Analytics analytics;
	@Autowired
	private AppConfig appConfig;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/")
	public String home(Locale locale, Model model, HttpServletRequest request) throws Exception {

		logger.info("Welcome home! The client locale is {}.", locale);

		// TODO: move to init()?
		if (! client.initialized) {
			try {
				client.init();
			} catch (Exception e) {
				// TODO:
				System.out.println(e);
				System.out.println(e.getStackTrace());
				return "error";
			}
		}
		
		client.setChartRange(request.getParameter("timeframe"));
		
		String p_period = request.getParameter("period");
		if (p_period != null) {
			if (p_period.equals("5m")) {
				client.plotChartForPeriod(Constants.period_5m, false);
			} else if (p_period.equals("15m")) {
				client.plotChartForPeriod(Constants.period_15m, false);
			} else if (p_period.equals("30m")) {
				client.plotChartForPeriod(Constants.period_30m, false);
			} else if (p_period.equals("2h")) {
				client.plotChartForPeriod(Constants.period_2h, false);
			} else if (p_period.equals("4h")) {
				client.plotChartForPeriod(Constants.period_4h, false);
			}
		}

		if (request.getParameter("refresh") != null)
			client.getRefreshedChart();

		if (request.getParameter("train") != null) {
			analytics.init();
			analytics.train();
		}
		
		model.addAttribute("period", client.getCurrentPeriod());		
		model.addAttribute("timeframe", client.getTimeframe());	
		model.addAttribute("Constants", new Constants());	
		model.addAttribute("appConfig", appConfig);
	
		return "home";
		
	}

	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public String config(Locale locale, Model model) {

		model.addAttribute("AppConfig", appConfig);
		return "configuration";

	}

	@RequestMapping(value = "/config", method = RequestMethod.POST)
	public String config(@ModelAttribute AppConfig c, Model model) throws DescribedIOException {
		
		appConfig.update(c);
		
		model.addAttribute("AppConfig", appConfig);
		model.addAttribute("message", "New Configuration saved.");

		return "configuration";
	}

	@RequestMapping(value = "/refresh_chart", method = RequestMethod.POST)
	public String refresh_chart() throws PNGChartCreationException {

		client.getChart();

		return "home";
	}

	@RequestMapping("/chart.png")
	public void renderChart(OutputStream stream) throws Exception {

		stream.write(client.getChart());

	}
	
	 
	@ExceptionHandler(LoggedException.class)
	public ModelAndView handleException(HttpServletRequest request, Exception ex){
	  
		//TODO: make proper 1-liner
		logger.error("Requested URL="+request.getRequestURL());
	    logger.error("Exception Raised="+ex);
	         
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
		Calendar cal = Calendar.getInstance();
		
	    ModelAndView modelAndView = new ModelAndView("error");
	    modelAndView.addObject("timestamp", dateFormat.format(cal.getTime()));
	    modelAndView.addObject("exception", ex);
	    modelAndView.addObject("url", request.getRequestURL());
	         
	    return modelAndView;
	    
	 } 
	 
}
