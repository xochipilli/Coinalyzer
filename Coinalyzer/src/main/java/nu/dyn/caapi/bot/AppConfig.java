package nu.dyn.caapi.bot;

import nu.dyn.caapi.utils.ConfigParser;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.stereotype.Service;

@Service
public class AppConfig {
	public static final String proxyHost = "sapproxy";
	public static final int proxyPort = 3128;
	public static final String configurationFile = "coinalyzer_config.xml";

	public String coinPrimary;
	public String coinCounter;

	AppConfig() throws ConfigurationException {
		ConfigParser.parse(configurationFile, this);
	}

	public void update(AppConfig c) throws ConfigurationException {
		coinPrimary = c.coinPrimary;
		coinCounter = c.coinCounter;

		ConfigParser.saveProperties(configurationFile, this);

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

	public static String getProxyhost() {
		return proxyHost;
	}

	public static int getProxyport() {
		return proxyPort;
	}
}
