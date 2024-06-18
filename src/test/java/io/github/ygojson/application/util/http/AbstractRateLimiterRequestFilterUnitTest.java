package io.github.ygojson.application.util.http;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusTest
class AbstractRateLimiterRequestFilterUnitTest {

	private static final Logger LOG = LoggerFactory.getLogger(
		RateLimitInterceptorTest.class
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
			get("/").willReturn(aResponse().withBody("Success").withStatus(200))
		);
	}

	@AfterAll
	static void afterAll() {
		if (MOCK_SERVER != null) {
			MOCK_SERVER.stop();
		}
	}

	private DummyRestClient createClientWithRequestFilter(
		final int maxRequest,
		final Duration duration
	) {
		return QuarkusRestClientBuilder
			.newBuilder()
			.register(
				new AbstractRateLimiterRequestFilter() {
					@Override
					protected RateLimitConfig getConfig() {
						return new RateLimitConfig(maxRequest, duration);
					}
				}
			)
			.baseUri(URI.create(MOCK_SERVER.url("/")))
			.build(DummyRestClient.class);
	}

	private void warmUp(final DummyRestClient client, final int calls) {
		IntStream
			.range(0, calls)
			.forEach(any -> {
				try (final Response response = client.getResponse()) {
					String body = response.readEntity(String.class);
					// do nothing with the response (just try to deserialize)
				} catch (Exception e) {
					LOG.error("warm-up request error");
				}
			});
	}

	@Test
	public void given_oneMaxRequestPer100ms_when_secondRequest_then_callWaits()
		throws IOException {
		// given - client with warmup
		final DummyRestClient client = createClientWithRequestFilter(
			1,
			Duration.ofMillis(100)
		);
		warmUp(client, 3);

		// when
		final Response firstResponse = client.getResponse();
		final Instant afterFirst = Instant.now();
		final Response secondResponse = client.getResponse();
		final Instant afterSecond = Instant.now();

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly
				.assertThat(firstResponse.getStatus())
				.describedAs(" first response status-code")
				.isEqualTo(200);
			softly
				.assertThat(secondResponse.getStatus())
				.describedAs(" second response status-code")
				.isEqualTo(200);
			// testing with -10 milliseconds as the timing wiht mock-server are too fast
			softly
				.assertThat(Duration.between(afterFirst, afterSecond))
				.describedAs("time between responses")
				.isGreaterThanOrEqualTo(Duration.ofMillis(90));
		});
	}
}
