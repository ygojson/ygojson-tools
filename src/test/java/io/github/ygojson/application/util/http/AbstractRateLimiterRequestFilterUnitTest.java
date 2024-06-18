package io.github.ygojson.application.util.http;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusTest
class AbstractRateLimiterRequestFilterUnitTest {

	private static final Logger LOG = LoggerFactory.getLogger(
		AbstractRateLimiterRequestFilterUnitTest.class
	);

	private static WireMockServer MOCK_SERVER;

	@BeforeAll
	static void beforeAll() {
		MOCK_SERVER = new WireMockServer();
		MOCK_SERVER.start();
		MOCK_SERVER.addMockServiceRequestListener((request, response) -> {
			LOG.debug("Request time: {}", Instant.now());
		});
		configureFor(MOCK_SERVER.port());
		stubFor(
			get("/")
				.willReturn(
					aResponse()
						.withBody("{{now timezone='UTC' format='epoch'}}")
						.withTransformers(ResponseTemplateTransformer.NAME)
						.withStatus(200)
				)
		);
	}

	@AfterAll
	static void afterAll() {
		if (MOCK_SERVER != null) {
			MOCK_SERVER.stop();
		}
	}

	private AbstractRateLimiterRequestFilter createTestRateLimiter(
		final int maxRequest,
		final Duration duration
	) {
		return new AbstractRateLimiterRequestFilter() {
			@Override
			protected RateLimitConfig getConfig() {
				return new RateLimitConfig(maxRequest, duration);
			}
		};
	}

	private DummyRestClient createClientWithRequestFilter(
		final Duration duration
	) {
		final DummyRestClient restClient = QuarkusRestClientBuilder
			.newBuilder()
			.register(createTestRateLimiter(1, duration))
			.baseUri(URI.create(MOCK_SERVER.url("/")))
			.build(DummyRestClient.class);
		// warmup the server to be sure that the test-requests throttle as expected
		warmUp(restClient, 2);
		return restClient;
	}

	private void warmUp(final DummyRestClient client, final int calls) {
		IntStream
			.range(0, calls)
			.forEach(any -> {
				try (final Response response = client.getResponse()) {
					// do nothing with the response (just try to deserialize)
					String body = response.readEntity(String.class);
				} catch (Exception e) {
					LOG.error("warm-up request error");
				}
			});
	}

	@Test
	public void given_oneMaxRequestPer100ms_when_secondRequest_then_throttles() {
		// given
		final int rateLimitMilliseconds = 100;
		final DummyRestClient client = createClientWithRequestFilter(
			Duration.ofMillis(rateLimitMilliseconds)
		);
		// when
		final Response firstResponse = client.getResponse();
		final Response secondResponse = client.getResponse();
		// then
		final Instant first = Instant.ofEpochMilli(
			Long.parseLong(firstResponse.readEntity(String.class))
		);
		final Instant second = Instant.ofEpochMilli(
			Long.parseLong(secondResponse.readEntity(String.class))
		);
		// testing with -10 milliseconds as the timing with wiremock server are too fast
		assertThat(second)
			.describedAs(
				"second call should be after first call + rateLimitDuration - 10ms (test buffer)"
			)
			.isAfterOrEqualTo(
				first.plus(Duration.ofMillis(rateLimitMilliseconds - 10))
			);
	}
}
