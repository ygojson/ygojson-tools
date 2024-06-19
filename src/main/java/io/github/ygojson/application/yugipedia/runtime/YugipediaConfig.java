package io.github.ygojson.application.yugipedia.runtime;

import static io.github.ygojson.application.util.http.AbstractRateLimiterRequestFilter.RateLimitConfig;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import io.github.ygojson.application.yugipedia.client.YugipediaRateLimitRequestFilter;

/**
 * Configuration for the shared beans of the yugipedia component.
 */
@ApplicationScoped
public class YugipediaConfig {

	/**
	 * Configured rate-limit for the Yugipedia API.
	 *
	 * @return the rate-limit config object.
	 */
	@Produces
	@Singleton
	@Named(YugipediaRateLimitRequestFilter.RATE_LIMIT_CONFIG_INJECT_NAME)
	public RateLimitConfig rateLimitConfig() {
		return new RateLimitConfig(
			1,
			Duration
				.ofSeconds(1) // add some milliseconds to be sure that we don't hit it
				.plus(250, ChronoUnit.MILLIS)
		);
	}
}
