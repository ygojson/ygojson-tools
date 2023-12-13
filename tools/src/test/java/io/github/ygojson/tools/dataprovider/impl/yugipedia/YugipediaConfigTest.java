package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.tools.common.ApplicationInfo;
import io.github.ygojson.tools.dataprovider.utils.client.ClientConfig;

class YugipediaConfigTest {

	@Test
	void given_configuration_when_rateLimit_then_isLargerThan1PerSecond() {
		// given
		final YugipediaConfig config = new YugipediaConfig();
		// when
		final ClientConfig.RateLimit limit = config.rateLimit();
		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(limit.maxRequest()).isEqualTo(1);
			softly.assertThat(limit.duration()).isGreaterThan(Duration.ofSeconds(1));
		});
	}

	public static Stream<Arguments> userAgentArguments() {
		return Stream.of(
			Arguments.of(
				new ApplicationInfo("ygojson", "v0.0.0", "https://ygojson.github.io"),
				"okhttp/${version}",
				"ygojson/v0.0.0 (https://ygojson.github.io) okhttp/${version}"
			),
			Arguments.of(
				new ApplicationInfo("ygojson", "v0.0.0", "https://ygojson.github.io"),
				null,
				"ygojson/v0.0.0 (https://ygojson.github.io)"
			),
			Arguments.of(new ApplicationInfo(null, null, null), null, ""),
			Arguments.of(
				new ApplicationInfo(null, "v0.0.0", "https://ygojson.github.io"),
				"okhttp/${version}",
				"(https://ygojson.github.io) okhttp/${version}"
			),
			Arguments.of(
				new ApplicationInfo("ygojson", null, null),
				"okhttp/${version}",
				"ygojson/unknown okhttp/${version}"
			)
		);
	}

	@ParameterizedTest
	@MethodSource("userAgentArguments")
	void given_applicationInfo_when_userAgentMapperWithOriginalAgent_then_returnExpectedUserAgent(
		final ApplicationInfo info,
		final String originalAgent,
		final String expectedAgent
	) {
		// given
		final Function<String, String> userAgentMapper = new YugipediaConfig()
			.userAgentMapper(info);
		// when
		final String userAgent = userAgentMapper.apply(originalAgent);
		// then
		assertThat(userAgent).isEqualTo(expectedAgent);
	}
}
