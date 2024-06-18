package io.github.ygojson.application.yugipedia.client;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import io.github.ygojson.application.util.http.AbstractRateLimiterRequestFilter;

/**
 * Provides the yugipedia rate-limit defaults.
 */
public class YugipediaRateLimitRequestFilter
	extends AbstractRateLimiterRequestFilter {

	public static final String RATE_LIMIT_CONFIG_INJECT_NAME =
		"yugipedia.rateLimitConfig";

	private final RateLimitConfig rateLimit;

	/**
	 * Default constructor.
	 * <br>
	 * Supports injection (named with {@link #RATE_LIMIT_CONFIG_INJECT_NAME}).
	 *
	 * @param rateLimitConfig configuration.
	 */
	@Inject
	public YugipediaRateLimitRequestFilter(
		@Named(RATE_LIMIT_CONFIG_INJECT_NAME) final RateLimitConfig rateLimitConfig
	) {
		this.rateLimit = rateLimitConfig;
	}

	@Override
	protected RateLimitConfig getConfig() {
		return rateLimit;
	}
}
