package io.github.ygojson.application.util.http;

import java.io.IOException;
import java.time.Duration;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.Response;
import org.slf4j.LoggerFactory;

/**
 * Abstract {@link ClientRequestFilter} providing functionality to throttle the request
 * based on a rate-limit.
 */
public abstract class AbstractRateLimiterRequestFilter
	implements ClientRequestFilter {

	/**
	 * Rate-limit configuration..
	 *
	 * @param maxRequest max-request per duration.
	 * @param duration   duration between maximum requests.
	 */
	public record RateLimitConfig(int maxRequest, Duration duration) {}

	private RateLimiter rateLimiter;

	/**
	 * Provides the rate-limit configuration.
	 * <br>
	 * This method is only called once to cache the rate-limiter and share accross calls.
	 *
	 * @return rate-limit configuration.
	 */
	protected abstract RateLimitConfig getConfig();

	private RateLimiter getRateLimiter() {
		if (rateLimiter == null) {
			final RateLimitConfig rateLimit = getConfig();
			final RateLimiterConfig rateLimitConfig = RateLimiterConfig
				.custom()
				.limitForPeriod(rateLimit.maxRequest())
				.limitRefreshPeriod(rateLimit.duration())
				.timeoutDuration(rateLimit.duration())
				.build();
			rateLimiter = RateLimiter.of(this.getClass().getName(), rateLimitConfig);
			LoggerFactory
				.getLogger(this.getClass())
				.debug("Using rate-limiter: {}", rateLimit);
		}
		return rateLimiter;
	}

	@Override
	public void filter(final ClientRequestContext requestContext)
		throws IOException {
		try {
			RateLimiter.waitForPermission(getRateLimiter());
		} catch (final Exception e) {
			requestContext.abortWith(
				Response.status(Response.Status.TOO_MANY_REQUESTS).build()
			);
		}
	}
}
