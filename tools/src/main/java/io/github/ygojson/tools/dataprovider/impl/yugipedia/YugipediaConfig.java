package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import io.github.ygojson.application.ApplicationInfo;
import io.github.ygojson.application.util.http.ClientConfig;

public class YugipediaConfig implements ClientConfig<YugipediaApi> {

	@Override
	public String name() {
		return "yugipedia";
	}

	@Override
	public Class<YugipediaApi> clientClass() {
		return YugipediaApi.class;
	}

	@Override
	public String baseUrl() {
		return "https://yugipedia.com";
	}

	@Override
	public RateLimit rateLimit() {
		return new RateLimit(
			1,
			Duration
				.ofSeconds(1) // add some milliseconds to be sure that we don't hit it
				.plus(250, ChronoUnit.MILLIS)
		);
	}

	@Override
	public Function<String, String> userAgentMapper(ApplicationInfo info) {
		final String clientNameVersion = getClientNameVersion(info);
		final String contactInfo = getContactInfo(info);
		return original ->
			String.join(
				" ",
				Stream
					.of(clientNameVersion, contactInfo, original)
					.filter(Objects::nonNull)
					.toList()
			);
	}

	private static String getContactInfo(ApplicationInfo info) {
		return info.url() != null ? "(" + info.url() + ")" : null;
	}

	private static String getClientNameVersion(ApplicationInfo info) {
		if (info.title() != null) {
			return (
				info.title() +
				"/" +
				(info.version() != null ? info.version() : "unknown")
			);
		}
		return null;
	}
}
