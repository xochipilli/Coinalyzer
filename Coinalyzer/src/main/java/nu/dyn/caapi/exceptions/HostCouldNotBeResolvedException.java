package nu.dyn.caapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Host could not be r esolved")
public class HostCouldNotBeResolvedException extends Exception {

	private static final long serialVersionUID = -3332292346834265371L;

	public HostCouldNotBeResolvedException(String proxyHost) {
		super("HostCouldNotBeResolvedException with host " + proxyHost);
	}
}
