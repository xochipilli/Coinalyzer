package nu.dyn.caapi.coinalyzer.bot;

import java.io.IOException;
import java.io.PrintWriter;

import nu.dyn.caapi.coinalyzer.exceptions.DescribedIOException;
import nu.dyn.caapi.coinalyzer.exceptions.DescribedNumberFormatException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.springframework.stereotype.Service;

//http://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.configuration.XMLConfiguration


@Service
public class AppConfig {
		
	public static final String configurationFile = "coinalyzer_config.xml";
	
	public String proxyHost = "sapproxy2";
	public Integer proxyPort = 3128;
	//public static final String projectDirectory = "coinalyzer/"; //TODO: ?

	public String coinPrimary = "BTC";
	public String coinCounter = "XMR";

	public AppConfig() throws DescribedNumberFormatException, DescribedIOException {
		
		XMLConfiguration config;
		try {
			
			config = new XMLConfiguration(configurationFile);
		
			coinPrimary = config.getString("coinPair.primary", coinPrimary);
			coinCounter = config.getString("coinPair.counter", coinCounter);
			proxyHost = config.getString("proxy.host", proxyHost);
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
			} catch (IOException e1) {
				throw new DescribedIOException("Could not write default configuration file " + configurationFile, e);
			}
		}
	}

	public void update(AppConfig c) throws DescribedIOException {
	
		coinPrimary = c.coinPrimary;
		coinCounter = c.coinCounter;

		saveProperties();

	}

	public void saveProperties() throws DescribedIOException {
		try {
		    XMLConfiguration config = new XMLConfiguration(configurationFile);
			config.setProperty("coinPair.primary", coinPrimary);
			config.setProperty("coinPair.counter", coinCounter);
			config.setProperty("proxy.host", proxyHost);
			config.setProperty("proxy.port", proxyPort.toString());
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

	public int getProxyport() {
		return proxyPort;
	}
}
