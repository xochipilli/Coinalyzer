package nu.dyn.caapi.utils;

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

import nu.dyn.caapi.exceptions.HostCouldNotBeResolvedException;

import org.json.JSONArray;
import org.json.JSONException;

public class MyJsonReader {
	private String proxyHost;
	private int proxyPort;
	
	public MyJsonReader(String proxyHost, int proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}
	
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public JSONArray readJson(String url, String filename) throws Exception {
		
		try { 
			String s = readFile(filename);
			JSONArray json = new JSONArray(s);
			return json;
		} catch (FileNotFoundException e) {
			return readJsonFromUrl(url, filename);
		}
	}
	
	public JSONArray readJsonFromUrl(String url, String fileName) throws HostCouldNotBeResolvedException  {
		System.out.println("Reading from URL: "+url);
		
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		JSONArray json=null;
		
		try {
			URL server = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) server.openConnection(proxy);
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
			System.out.println("Error: "+e);
			throw new HostCouldNotBeResolvedException(proxyHost);
		} catch (JSONException e){
			System.out.println("Error: "+e);
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
