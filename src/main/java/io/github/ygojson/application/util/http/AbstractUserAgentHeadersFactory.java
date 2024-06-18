package io.github.ygojson.application.util.http;

import java.util.List;

import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

/**
 * Abstract implementation to use a custom User-Agent header.
 */
public abstract class AbstractUserAgentHeadersFactory
	implements ClientHeadersFactory {

	private static final String USER_AGENT_HEADER_NAME = "User-Agent";

	@Override
	public final MultivaluedMap<String, String> update(
		MultivaluedMap<String, String> incomingHeaders,
		MultivaluedMap<String, String> clientOutgoingHeaders
	) {
		final List<String> userAgents = clientOutgoingHeaders.get(
			USER_AGENT_HEADER_NAME
		);
		final String original = userAgents.isEmpty()
			? null
			: String.join(" ", userAgents);
		final String newHeader = getHeaderString(original);
		clientOutgoingHeaders.putSingle(USER_AGENT_HEADER_NAME, newHeader);
		return clientOutgoingHeaders;
	}

	/**
	 * Gets the header String combining the original header.
	 *
	 * @param originalHeader the original header of the current request.
	 * @return the new header String for the client.
	 */
	protected abstract String getHeaderString(final String originalHeader);
}
