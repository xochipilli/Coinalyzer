package nu.dyn.caapi.coinalyzer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Host could not be resolved")
public class HostCouldNotBeResolvedException extends LoggedException {

	private static final long serialVersionUID = -3332292346834265372L;

	public HostCouldNotBeResolvedException(String proxyHost, Exception e) {

		super("HostCouldNotBeResolvedException with host " + proxyHost, e);
		
	}
}
