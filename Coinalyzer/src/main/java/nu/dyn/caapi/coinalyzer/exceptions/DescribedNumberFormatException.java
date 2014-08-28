package nu.dyn.caapi.coinalyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Incorrect number")
public class DescribedNumberFormatException extends LoggedException {

	private static final long serialVersionUID = -3332292346834265372L;

	public DescribedNumberFormatException(String description, Exception e) {
		
		super("DescribedNumberFormatException " + description, e);

	}
}
