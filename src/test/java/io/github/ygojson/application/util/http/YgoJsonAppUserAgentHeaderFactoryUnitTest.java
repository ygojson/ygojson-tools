package io.github.ygojson.application.util.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.application.ApplicationInfo;

class YgoJsonAppUserAgentHeaderFactoryUnitTest {

	private YgoJsonAppUserAgentHeaderFactory factory;

	@BeforeEach
	void beforeEach() {
		factory = new YgoJsonAppUserAgentHeaderFactory();
	}

	public static Stream<Arguments> userAgentArguments() {
		return Stream.of(
			Arguments.of(
				new ApplicationInfo("ygojson", "v0.0.0", "https://ygojson.github.io"),
				"Quarkus REST Client",
				"ygojson/v0.0.0 (https://ygojson.github.io) Quarkus REST Client"
			),
			Arguments.of(
				new ApplicationInfo("ygojson", "v0.0.0", "https://ygojson.github.io"),
				null,
				"ygojson/v0.0.0 (https://ygojson.github.io)"
			),
			Arguments.of(new ApplicationInfo(null, null, null), null, ""),
			Arguments.of(
				new ApplicationInfo(null, "v0.0.0", "https://ygojson.github.io"),
				"Quarkus REST Client",
				"(https://ygojson.github.io) Quarkus REST Client"
			),
			Arguments.of(
				new ApplicationInfo("ygojson", null, null),
				"Quarkus REST Client",
				"ygojson/unknown Quarkus REST Client"
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
		factory.info = info;
		// when
		final String userAgent = factory.getHeaderString(originalAgent);
		// then
		assertThat(userAgent).isEqualTo(expectedAgent);
	}
}
