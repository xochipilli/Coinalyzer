package nu.dyn.caapi.coinalyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot open connection to host")
public class URLOpenConnectionException extends LoggedException {

	private static final long serialVersionUID = -3332292346834265372L;

	public URLOpenConnectionException(String description, Exception e) {

		super("URLOpenConnectionException " + description, e);
		
	}
}
