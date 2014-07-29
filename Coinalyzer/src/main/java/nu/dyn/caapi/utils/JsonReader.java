package nu.dyn.caapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import nu.dyn.caapi.bot.Config;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonReader {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Config.proxyHost, Config.proxyPort));
		//InputStream 
		
		URL server = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) server.openConnection(proxy);
		InputStream is = conn.getInputStream();
		try {
		  BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONArray json = new JSONArray(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	}
}
