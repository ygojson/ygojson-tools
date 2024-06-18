package io.github.ygojson.application.yugipedia.testutil;

import static io.github.ygojson.application.util.http.AbstractRateLimiterRequestFilter.RateLimitConfig;

import java.time.Duration;
import java.util.List;

import io.quarkus.test.junit.QuarkusTestProfile;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.yugipedia.client.YugipediaRateLimitRequestFilter;

/**
 * Profile to be used on tests interacting with the yugipedia API,
 * to prevent that the real API is hit.
 */
public class YugipediaMockProfile implements QuarkusTestProfile {

	private static final Logger LOG = LoggerFactory.getLogger(
		YugipediaMockProfile.class
	);

	@Override
	public List<TestResourceEntry> testResources() {
		LOG.debug("Providing test-resources for yugipeda-mock profile");
		return List.of(new TestResourceEntry(YugipediaWiremockResource.class));
	}

	/**
	 * Alternative rate-limit configuration for mock-tests.
	 *
	 * @return loose rate limit configuration.
	 */
	@Produces
	@Singleton
	@Alternative
	@Priority(1)
	@Named(YugipediaRateLimitRequestFilter.RATE_LIMIT_CONFIG_INJECT_NAME)
	public RateLimitConfig testRateLimitConfig() {
		return new RateLimitConfig(100, Duration.ofSeconds(1));
	}
}
