package io.github.ygojson.application.util.http;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

class RateLimitInterceptorTest {

	private static final Logger log = LoggerFactory.getLogger(
		RateLimitInterceptorTest.class
	);

	private static MockWebServer MOCK_SERVER;

	@BeforeAll
	static void beforeAll() {
		MOCK_SERVER = new MockWebServer();
		MOCK_SERVER.setDispatcher(
			new Dispatcher() {
				final AtomicLong nextCallId = new AtomicLong(0);

				@Override
				public MockResponse dispatch(RecordedRequest recordedRequest) {
					long callId = nextCallId.getAndIncrement();
					return new MockResponse()
						.setBody(String.format("{testData: \"DUMMY-%s\"}", callId));
				}
			}
		);
	}

	private RateLimitInterceptor createTestInterceptor(
		final int maxRequest,
		final Duration duration
	) {
		return new RateLimitInterceptor(
			RateLimiter.of(
				"test",
				RateLimiterConfig
					.custom()
					.limitForPeriod(maxRequest)
					.limitRefreshPeriod(duration)
					.timeoutDuration(duration)
					.build()
			)
		);
	}

	@AfterAll
	static void afterAll() {
		try {
			MOCK_SERVER.close();
			MOCK_SERVER = null;
		} catch (final IOException e) {
			log.error("Error closing mock-server", e);
		}
	}

	private void warmUp(final OkHttpClient client, final int calls) {
		IntStream
			.range(0, calls)
			.forEach(any -> {
				try(ResponseBody body = doRequest(client).body()) {
					// do nothing with the response (just try to deserialize)
				} catch (IOException e) {
					log.error("warm-up request error");
				}
			});
	}

	private Response doRequest(final OkHttpClient client) throws IOException {
		final Request request = new Request.Builder()
			.url(MOCK_SERVER.url("/test"))
			.build();
		// execute the call
		return client.newCall(request).execute();
	}

	@Test
	public void given_oneMaxRequestPer100ms_when_secondRequest_then_callWaits()
		throws IOException {
		// given
		final OkHttpClient client = new OkHttpClient.Builder()
			.addNetworkInterceptor(createTestInterceptor(1, Duration.ofMillis(100)))
			.build();
		warmUp(client, 3);

		// when
		final Response firstResponse = doRequest(client);
		final Instant afterFirst = Instant.now();
		final Response secondResponse = doRequest(client);
		final Instant afterSecond = Instant.now();

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly
				.assertThat(firstResponse.code())
				.describedAs(" first response code")
				.isEqualTo(200);
			softly
				.assertThat(secondResponse.code())
				.describedAs(" second response code")
				.isEqualTo(200);
			// testing with -10 milliseconds as the timing wiht mock-server are too fast
			softly
				.assertThat(Duration.between(afterFirst, afterSecond))
				.describedAs("time between responses")
				.isGreaterThanOrEqualTo(Duration.ofMillis(90));
		});
	}
}
