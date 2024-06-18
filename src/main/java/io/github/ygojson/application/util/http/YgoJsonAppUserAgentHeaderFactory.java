package io.github.ygojson.application.util.http;

import java.util.Objects;
import java.util.stream.Stream;

import jakarta.inject.Inject;

import io.github.ygojson.application.ApplicationInfo;

/**
 * Default implementation to be used in clients.
 * <br>
 * This might not be the default for some clients, as they might have specific requirements.
 * For example, some service might require rolling User-Agents.
 */
public class YgoJsonAppUserAgentHeaderFactory
	extends AbstractUserAgentHeadersFactory {

	@Inject
	ApplicationInfo info;

	@Override
	protected String getHeaderString(String originalHeader) {
		return String.join(
			" ",
			Stream
				.of(getClientNameVersion(), getContactInfo(), originalHeader)
				.filter(Objects::nonNull)
				.toList()
		);
	}

	/**
	 * Gets the contact-info part.
	 *
	 * @return contact info part.
	 */
	private String getContactInfo() {
		return info.url() != null ? "(" + info.url() + ")" : null;
	}

	/**
	 * Gets the client-name version part.
	 *
	 * @return the client-name version part.
	 */
	private String getClientNameVersion() {
		if (info.name() != null) {
			return (
				info.name() +
				"/" +
				(info.version() != null ? info.version() : "unknown")
			);
		}
		return null;
	}
}
