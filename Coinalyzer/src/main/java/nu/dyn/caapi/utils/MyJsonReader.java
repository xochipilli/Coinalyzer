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

import nu.dyn.caapi.bot.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;


public class MyJsonReader {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public static JSONArray readJson(String url, String filename) throws IOException {
		
		try { 
			String s = readFile(filename);
			JSONArray json = new JSONArray(s);
			return json;
		} catch (FileNotFoundException e) {
			return readJsonFromUrl(url, filename);
		}
	}
	
	public static JSONArray readJsonFromUrl(String url, String fileName) throws IOException,	JSONException {
		System.out.println("Reading from URL: "+url);
		
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				AppConfig.proxyHost, AppConfig.proxyPort));
		
		URL server = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) server.openConnection(proxy);
		InputStream is = conn.getInputStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			
			PrintWriter p = new PrintWriter(fileName);
			p.write(json.toString());
			p.close();
			
			return json;
		} finally {
			is.close();
		}
		
	}
	
	public static String readFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		 
		StringBuffer sb = new StringBuffer();
		String line = null;
		 
		while((line = br.readLine())!=null)
			sb.append(line).append("\n");
		 
		br.close();
		
		return new String(sb.toString());
		
		
	}
}
