package nu.dyn.caapi.coinalyzer.bot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import nu.dyn.caapi.coinalyzer.exceptions.DescribedIOException;
import nu.dyn.caapi.coinalyzer.exceptions.DescribedNumberFormatException;
import nu.dyn.caapi.coinalyzer.market.MyIndicator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.springframework.stereotype.Component;

import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;

//http://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.configuration.XMLConfiguration

//TODO len raz za session 

@Component
@ManagedBean
@SessionScoped
public class AppConfig {
		
	public static final String configurationFile = "coinalyzer_config.xml";
	
	public String proxyHost = "sapproxy";
	public Integer proxyPort = 3128;
	public boolean useProxy = true;
	
	//public static final String projectDirectory = "coinalyzer/"; //TODO: ?

	public String coinPrimary = "BTC";
	public String coinCounter = "XMR";
	
	// access indicator by name, every indicator has named attributes
	public HashMap<String, MyIndicator<?>> indicators = new HashMap<String, MyIndicator<?>>();
	
	public AppConfig() throws DescribedNumberFormatException, DescribedIOException {
		
		XMLConfiguration config;
		try {
			
			config = new XMLConfiguration(configurationFile);
		
			coinPrimary = config.getString("coinPair.primary", coinPrimary);
			coinCounter = config.getString("coinPair.counter", coinCounter);
			useProxy = "yes".equals(config.getString("proxy.useProxy", useProxy ? "true" : "false")) ? true : false;
			
			proxyHost = config.getString("proxy.host", proxyHost);
		
			// ----------- INDICATORS
			List<HierarchicalConfiguration> fields = config.configurationsAt("indicators(0).indicator");
			// iterate over all indicators
			for(HierarchicalConfiguration sub : fields) {
				MyIndicator<?> indicator = null;
				
				String i_name = sub.getString("[@name]");
				switch (i_name) {
					case "SMA": indicator = new MyIndicator<SMAIndicator>(i_name);
						break;
					case "EMA": indicator = new MyIndicator<EMAIndicator>(i_name);
						break;
					default: continue;	// no recognizable indicator
				}
	
				// save parameters to hashmap	
				HashMap<String, Double> params = new HashMap<String, Double>();
				for (Iterator<String> it = sub.getKeys(); it.hasNext(); ) {
					try {
						String name = it.next();
						Double value = sub.getDouble(name);
						params.put(name, value);
					} catch (ConversionException e) {
						;	// just ignore the key
					}
				}
				
				indicator.setParams(params);
				indicators.put(i_name, indicator);
			}
			//---------------
			
			try {
				String port = config.getString("proxy.port", proxyPort.toString());
				proxyPort = new Integer(port);
			} catch (NumberFormatException e) {
				throw new DescribedNumberFormatException("Unparsable proxy host port number " + proxyPort + " in configuration file", e);
			}
			
		} catch (ConfigurationException e) {
			try {
				PrintWriter writer = new PrintWriter(configurationFile, "UTF-8");
				//TODO: write all defaults to file
				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<config>\n</config>\n");
				writer.close();
				saveProperties();
			} catch (IOException ioe) {
				throw new DescribedIOException("Could not write default configuration file " + configurationFile, ioe);
			}
		}
	}

	public void update(AppConfig c) throws DescribedIOException {
	
		coinPrimary = c.coinPrimary;
		coinCounter = c.coinCounter;
		useProxy = c.useProxy;
		proxyHost = c.proxyHost;
		proxyPort = c.proxyPort;
		
		saveProperties();

	}

	public void saveProperties() throws DescribedIOException {
		try {
		    XMLConfiguration config = new XMLConfiguration(configurationFile);
			config.setProperty("coinPair.primary", coinPrimary);
			config.setProperty("coinPair.counter", coinCounter);
			config.setProperty("proxy.host", proxyHost);
			config.setProperty("proxy.port", proxyPort.toString());
			config.setProperty("proxy.useProxy", useProxy);
			config.save();
		} catch (ConfigurationException e) {
			throw new DescribedIOException("Could not write configuration file " + configurationFile, e);
		}
	}
			
	public String getCoinPrimary() {
		return coinPrimary;
	}

	public void setCoinPrimary(String coinPrimary) {
		this.coinPrimary = coinPrimary;
	}

	public String getCoinCounter() {
		return coinCounter;
	}

	public void setCoinCounter(String coinCounter) {
		this.coinCounter = coinCounter;
	}

	public String getProxyhost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyport() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}
}
