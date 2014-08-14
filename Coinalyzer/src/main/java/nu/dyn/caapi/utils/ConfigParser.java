package nu.dyn.caapi.utils;

import nu.dyn.caapi.bot.AppConfig;

import org.apache.cassandra.thrift.cassandraConstants;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

//http://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.configuration.XMLConfiguration

public class ConfigParser {

	public static void parse(String filename, AppConfig ac)
			throws ConfigurationException {
		XMLConfiguration config = new XMLConfiguration(filename);

		ac.coinPrimary = getProperty("coinPair.primary", config);
		ac.coinCounter = getProperty("coinPair.counter", config);

	}

	public static void saveProperties(String filename, AppConfig ac)
			throws ConfigurationException {
		XMLConfiguration config = new XMLConfiguration(filename);
		setProperty("coinPair.primary", ac.coinPrimary, config);
		setProperty("coinPair.counter", ac.coinCounter, config);
		config.save();
	}

	static String getProperty(String key, XMLConfiguration c) {

		return c.getString(key);
	}

	static void setProperty(String key, String value, XMLConfiguration c) {
		c.setProperty(key, value);

	}

}
