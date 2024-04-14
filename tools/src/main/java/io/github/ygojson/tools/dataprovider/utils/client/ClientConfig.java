package io.github.ygojson.tools.dataprovider.utils.client;

import java.time.Duration;
import java.util.function.Function;

import io.github.ygojson.application.ApplicationInfo;

public interface ClientConfig<T> {
	record RateLimit(int maxRequest, Duration duration) {}

	String name();

	Class<T> apiClass();

	String baseUrl();

	RateLimit rateLimit();

	/**
	 * Gets the supplier to build the user-agent given the current version of the tool.
	 *
	 * @param info information of the application.
	 * @return supplier for the user-agent.
	 */
	Function<String, String> userAgentMapper(final ApplicationInfo info);
}
