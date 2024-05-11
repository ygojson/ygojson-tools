package io.github.ygojson.application.util.http;

import java.time.Duration;
import java.util.function.Function;

import io.github.ygojson.application.ApplicationInfo;

/**
 * Configuration of the HTTP-client.
 *
 * @param <T> the configuration for the client.
 */
public interface ClientConfig<T> {
	/**
	 * Rate-limit information.
	 *
	 * @param maxRequest max-request per duration.
	 * @param duration duration between maximum requests.
	 */
	record RateLimit(int maxRequest, Duration duration) {}

	/**
	 * The name of the client.
	 *
	 * @return name of the client.
	 */
	String name();

	/**
	 * The class of the client.
	 *
	 * @return class of the client.
	 */
	Class<T> clientClass();

	/**
	 * Base url for the client.
	 * <br>
	 * This can be used to support test servers.
	 *
	 * @return the base URL for the client.
	 */
	String baseUrl();

	/**
	 * Returns the rate limit to apply to the client.
	 *
	 * @return rate limit for the client.
	 */
	RateLimit rateLimit();

	/**
	 * Gets the supplier to build the user-agent given the current version of the tool.
	 *
	 * @param info information of the application.
	 * @return supplier for the user-agent.
	 */
	Function<String, String> userAgentMapper(final ApplicationInfo info);
}
