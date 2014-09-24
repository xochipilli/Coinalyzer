package nu.dyn.caapi.coinalyzer.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
public class LoggerView implements Serializable {
	
	private static final long serialVersionUID = 1393350331672012086L;
	
	List<String> log = new ArrayList<String>();
	List<String> debugLog = new ArrayList<String>();

	int max_lines = 100;

	private static final Logger logger = LoggerFactory.getLogger(LoggerView.class);
	
	public LoggerView() {
		loadInfo();
		loadDebug();
	}
	
	public void loadInfo() {
		try {
			log = loadFile("coinalyzer-info.log");
		} catch (ParseException e) {
			;
		}
	}
	
	public void loadDebug() {
		try {
			debugLog = loadFile("coinalyzer-debug.log");
		} catch (ParseException e) {
			;
		}
	}
	
	private List<String> loadFile(String path) throws ParseException {

		List<String> list = new ArrayList<String>();
		
		File f = new File(path);
		BufferedReader r = null;
		
		try {
			// count number of lines first
			r = new BufferedReader(new FileReader(f));
			int n=0;
			while (r.readLine() != null)
				n++;	
			r.close();
			
			// start from the top of the file
			for (r = new BufferedReader(new FileReader(f)); n>max_lines; n--)
				r.readLine();
			
			// read only last max_lines lines
			String s = "";
			while ((s = r.readLine()) != null)
				list.add(s);
			
			Collections.reverse(list);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			if (r != null)
				try {
					r.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
		}
		
		return list;
		
	}

	public List<String> getLog() {
		return log;
	}

	public int getMax_lines() {
		return max_lines;
	}
	
	public List<String> getDebugLog() {
		return debugLog;
	}
}
