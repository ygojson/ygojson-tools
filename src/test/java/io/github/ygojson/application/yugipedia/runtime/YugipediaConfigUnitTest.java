package io.github.ygojson.application.yugipedia.runtime;

import java.time.Duration;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.util.http.AbstractRateLimiterRequestFilter.RateLimitConfig;

@QuarkusTest
class YugipediaConfigUnitTest {

	@Inject
	RateLimitConfig rateLimitConfig;

	@Test
	void given_injectedRateLimitConfig_then_isLargerThan1PerSecond() {
		// given - rateLimitConfig
		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(rateLimitConfig.maxRequest()).isEqualTo(1);
			softly
				.assertThat(rateLimitConfig.duration())
				.isGreaterThan(Duration.ofSeconds(1));
		});
	}
}
