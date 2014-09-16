package nu.dyn.caapi.coinalyzer.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import nu.dyn.caapi.coinalyzer.bot.AppConfig;
import nu.dyn.caapi.coinalyzer.exceptions.HostCouldNotBeResolvedException;
import nu.dyn.caapi.coinalyzer.exceptions.JSONParsingException;
import nu.dyn.caapi.coinalyzer.exceptions.URLOpenConnectionException;

import org.json.JSONArray;
import org.json.JSONException;

public class MyJsonReader {

	AppConfig appConfig;
	
	public MyJsonReader(AppConfig appConfig) {
			this.appConfig = appConfig;
	}
	
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public JSONArray readJson(String url, String filename) throws JSONParsingException, HostCouldNotBeResolvedException, URLOpenConnectionException, IOException {
		
		try { 
			String s = readFile(filename);
			JSONArray json = new JSONArray(s);
			return json;
		} catch (FileNotFoundException e) {
			return readJsonFromUrl(url, filename);
		}
	}
	
	public JSONArray readJsonFromUrl(String url, String fileName) throws HostCouldNotBeResolvedException, URLOpenConnectionException, JSONParsingException  {
		System.out.println("Reading from URL: "+url);
		
		JSONArray json=null;
		
		try {
			URL server = new URL(url);
		
			HttpsURLConnection conn;
			if (appConfig.useProxy) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(appConfig.proxyHost, appConfig.proxyPort));
				conn = (HttpsURLConnection) server.openConnection(proxy);
			} else {
				conn = (HttpsURLConnection) server.openConnection();
			}
			
			InputStream is = conn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			json = new JSONArray(jsonText);
			
			PrintWriter p = new PrintWriter(fileName);
			p.write(json.toString());
			p.close();
			is.close();
			
		} catch (IOException e) {
			throw new URLOpenConnectionException("Cannot open connection to " + url, e);
		} catch (JSONException e){
			throw new JSONParsingException("Problem parsing JSON market data from "+ url, e);
		}
		return json;
				
	}
	
	public String readFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		 
		StringBuffer sb = new StringBuffer();
		String line = null;
		 
		while((line = br.readLine())!=null)
			sb.append(line).append("\n");
		 
		br.close();
		
		return new String(sb.toString());		
		
	}
}
