package io.github.ygojson.application.yugipedia.client;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;

import io.github.ygojson.application.ApplicationInfo;
import io.github.ygojson.application.util.http.ClientFactory;

/**
 * Mother class for yugipedia tests
 */
public class YugipediaClientMother {

	private static final ObjectMapper MAPPER = new ObjectMapper()
		.registerModule(new JavaTimeModule());
	private static final ApplicationInfo TEST_INFO = new ApplicationInfo(
		"ygojson-tools",
		"test",
		"https://github.com/ygojson/ygojson-tools"
	);
	private static final ClientFactory FACTORY = new ClientFactory(
		MAPPER,
		TEST_INFO
	);

	public static YugipediaClient production() {
		return FACTORY.getClient(YugipediaClient.getConfig());
	}

	public static YugipediaClient mocked(final WireMockServer mockServer) {
		return FACTORY.getClient(new MockedConfig(mockServer.url("/")));
	}

	private static final class MockedConfig extends Config {

		private final String baseUrl;

		public MockedConfig(final String baseUrl) {
			this.baseUrl = baseUrl;
		}

		@Override
		public String name() {
			return super.name() + ".mocked@" + baseUrl;
		}

		@Override
		public String baseUrl() {
			return baseUrl;
		}

		@Override
		public RateLimit rateLimit() {
			// do not enforce any rate limit here, as it is the mocked server
			return new RateLimit(100, Duration.ofSeconds(1));
		}
	}
}
