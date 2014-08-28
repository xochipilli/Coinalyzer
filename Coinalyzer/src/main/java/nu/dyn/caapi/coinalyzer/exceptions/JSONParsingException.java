package nu.dyn.caapi.coinalyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Problem parsing JSON data")
public class JSONParsingException extends LoggedException {

	private static final long serialVersionUID = -3332292346834265372L;

	public JSONParsingException(String description, Exception e) {

		super("JSONParsingException " + description, e);
	
	}
}
