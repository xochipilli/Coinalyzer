package nu.dyn.caapi.coinalyzer.exceptions;

import org.slf4j.LoggerFactory;

public class LoggedException extends Exception {

	private static final long serialVersionUID = -3332292346834265372L;

	public LoggedException(String description, Exception e) {

		super(description);
		
		//TODO:
		LoggerFactory.getLogger("nu.dyn.caapi.coinalyzer").debug(e.getMessage());
		LoggerFactory.getLogger("nu.dyn.caapi.coinalyzer").error(description);
		
	}
}
