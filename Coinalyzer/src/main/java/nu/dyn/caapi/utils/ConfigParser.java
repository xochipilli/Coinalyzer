package nu.dyn.caapi.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import nu.dyn.caapi.bot.AppConfig;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;


//http://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.configuration.XMLConfiguration

public class ConfigParser {

	public static void parse(String filename, AppConfig ac) {
		
		XMLConfiguration config;
		try {
			
			config = new XMLConfiguration(filename);
			
			ac.coinPrimary = getProperty("coinPair.primary", config);
			ac.coinCounter = getProperty("coinPair.counter", config);
			
		} catch (ConfigurationException e) {
			try {
				PrintWriter writer = new PrintWriter(filename, "UTF-8");
				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<config>\n</config>\n");
				writer.close();
				saveProperties(filename, ac);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public static void saveProperties(String filename, AppConfig ac) {
		try {
		     XMLConfiguration config = new XMLConfiguration(filename);
			setProperty("coinPair.primary", ac.coinPrimary, config);
			setProperty("coinPair.counter", ac.coinCounter, config);
			config.save();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String getProperty(String key, XMLConfiguration c) {

		return c.getString(key);
	}

	static void setProperty(String key, String value, XMLConfiguration c) {
		c.setProperty(key, value);

	}

}
