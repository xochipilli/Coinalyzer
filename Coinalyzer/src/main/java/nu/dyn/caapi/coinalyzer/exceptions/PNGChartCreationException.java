package nu.dyn.caapi.coinalyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Problem writing chart to PNG")
public class PNGChartCreationException extends LoggedException {

	private static final long serialVersionUID = -3332292346834265372L;

	public PNGChartCreationException(String description, Exception e) {

		super("PNGChartCreationException " + description, e);
	
	}
}
