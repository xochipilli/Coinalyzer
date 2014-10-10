package nu.dyn.caapi.coinalyzer.bot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.neuroph.core.learning.stop.MaxErrorStop;
import org.springframework.stereotype.Component;

//http://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.configuration.XMLConfiguration

//TODO len raz za session 

@ManagedBean
@SessionScoped
@Component
public class AppConfig {
		
	public static final String configurationFile = "coinalyzer_config.xml";
	
	public String proxyHost = "sapproxy";
	public Integer proxyPort = 3128;
	public boolean useProxy = true;
	
	//public static final String projectDirectory = "coinalyzer/"; //TODO: ?

	public String coinPrimary = "BTC";
	public String coinCounter = "XMR";
	
	public List<MyIndicator<?>> indicators = new ArrayList<MyIndicator<?>>(); // = new HashMap<String, MyIndicator<?>>();
	
	// Neuronal network
	public NN nn = new NN();
	public class NN {
		public float learningRate = 0.2f;
		public float momentum = 0.7f;
		public float maxError = 0.01f;
		public int maxIterations = 1000;
		public float trainsetRatio = 0.8f;
		DateTime traindataStart = DateTime.parse("8/8/2014", DateTimeFormat.forPattern("dd/MM/yyyy"));
		DateTime testdataStart = DateTime.parse("9/9/2014", DateTimeFormat.forPattern("dd/MM/yyyy"));
	
		public DateTime getTraindataStart() {
			return traindataStart;
		}
		public DateTime getTestdataStart() {
			return testdataStart;
		}
		public float getLearningRate() {
			return learningRate;
		}
		public float getMomentum() {
			return momentum;
		}
		public float getMaxError() {
			return maxError;
		}
		public int getMaxIterations() {
			return maxIterations;
		}
		public float getTrainsetRatio() {
			return trainsetRatio;
		}
		@Override
		public String toString() {
			return "train data start: "+getTraindataStart()+" / test data start: "+getTestdataStart()+" / learning rate: "+learningRate+" / momentum: "+momentum+" / max error: "+maxError+" / max iterations: "+maxIterations+" / trainset ratio: "+trainsetRatio;
		}
	}

	public AppConfig() throws DescribedNumberFormatException, DescribedIOException {
		
		XMLConfiguration config;
		try {
			
			config = new XMLConfiguration(configurationFile);
		
			coinPrimary = config.getString("coinPair.primary", coinPrimary);
			coinCounter = config.getString("coinPair.counter", coinCounter);
			useProxy = "true".equals(config	.getString("proxy.useProxy", useProxy ? "true" : "false")) ? true : false;
			
			proxyHost = config.getString("proxy.host", proxyHost);
		
			// -- INDICATORS ------------
			List<HierarchicalConfiguration> fields = config.configurationsAt("indicators(0).indicator");
			// iterate over all indicators
			for(HierarchicalConfiguration sub : fields) {
				// create new empty indicator with name from tag
				String i_name = sub.getString("[@name]");
				MyIndicator<?> indicator = new MyIndicator(i_name);
				
				// save parameters for indicator to hashmap
				HashMap<String, Integer> params = new HashMap<String, Integer>();
				for (Iterator<String> it = sub.getKeys(); it.hasNext(); ) {
					try {
						String name = it.next();
						Integer value = sub.getInt(name);
						params.put(name, value);
					} catch (ConversionException e) {
						;	// just ignore the key
					}
				}
				
				indicator.setParams(params);
				indicators.add(indicator);
			}
			
			// -- NN ----------------
				nn.learningRate = config.getFloat("nn.learningRate", nn.learningRate);
				nn.momentum = config.getFloat("nn.momentum", nn.momentum);
				nn.maxError = config.getFloat("nn.maxError", nn.maxError);
				nn.maxIterations = config.getInt("nn.maxIterations", nn.maxIterations);
				nn.trainsetRatio = config.getFloat("nn.trainsetRatio", nn.trainsetRatio);
				
				DateTimeFormatter f = DateTimeFormat.forPattern("dd/MM/yyyy");
				try {
					nn.traindataStart= f.parseDateTime(config.getString("nn.traindataStart", nn.traindataStart.toString()));
					nn.testdataStart= f.parseDateTime(config.getString("nn.testdataStart", nn.testdataStart.toString()));
				} catch (IllegalArgumentException e) {
					//TODO: create and throw new exception
				}
				
			// ----------------------
			
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
			
			// TODO:
//			for (MyIndicator<?> i: indicators)
//				config.setaddProperty("indicators.indicator", i.);
//			
			
			config.setProperty("neuronalNetwork.learningRate", nn.learningRate);
			config.setProperty("neuronalNetwork.momentum", nn.momentum);
			config.setProperty("neuronalNetwork.maxError", nn.maxError);
			config.setProperty("neuronalNetwork.maxIterations", nn.maxIterations);
			config.setProperty("neuronalNetwork.trainsetRatio", nn.trainsetRatio);
			config.setProperty("neuronalNetwork.traindataStart", nn.getTraindataStart());
			config.setProperty("neuronalNetwork.testdataStart", nn.getTestdataStart());
		
			config.save();
		} catch (ConfigurationException e) {
			throw new DescribedIOException("Could not write configuration file " + configurationFile, e);
		}
	}
			
	public List<MyIndicator<?>> getIndicators() {
	
		return indicators;
		
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
	
	public NN getNn() {
		return nn;
	}
}
