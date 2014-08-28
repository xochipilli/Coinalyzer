package nu.dyn.caapi.coinalyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "General file IO exception")
public class DescribedIOException extends LoggedException {

	private static final long serialVersionUID = -3332292346834265372L;

	public DescribedIOException(String description, Exception e) {

		super("DescribedIOException " + description, e);
		
	}
}
