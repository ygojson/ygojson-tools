package io.github.ygojson.tools.dataprovider.utils.client;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

class UserAgentInterceptorTest {

	private static final Logger log = LoggerFactory.getLogger(UserAgentInterceptorTest.class);

	private static MockWebServer MOCK_SERVER;

	@BeforeAll
	static void beforeAll() {
		MOCK_SERVER = new MockWebServer();
		MOCK_SERVER.setDispatcher(
			new Dispatcher() {
				@Override
				public MockResponse dispatch(RecordedRequest recordedRequest)
					throws InterruptedException {
					final String header = recordedRequest.getHeader("User-Agent");
					if (header == null) {
						return new MockResponse().setResponseCode(400);
					} else {
						return new MockResponse().setResponseCode(200).setBody(header);
					}
				}
			}
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

	private void assertUserAgentResponse(
		final Response response,
		final String expectedStartOfAgent
	) throws IOException {
		final String body = response.body().string();
		SoftAssertions.assertSoftly(softly -> {
			softly
				.assertThat(response.code())
				.describedAs("response code")
				.isEqualTo(200);
			softly
				.assertThat(body)
				.describedAs("body (agent string)")
				.startsWith(expectedStartOfAgent);
		});
	}

	@Test
	void given_userAgentAppendingToOriginal_when_executeWithNetworkInterceptor_then_userAgentHeaderContainsOkhttp()
		throws IOException {
		// given
		final UserAgentInterceptor testInterceptor =
			new UserAgentInterceptor(original ->
				"MyTool/v0.0.0 (http://example.org/MyTool) " + original
			);
		// when
		final Response response = new OkHttpClient.Builder()
			.addNetworkInterceptor(testInterceptor)
			.build()
			.newCall(new Request.Builder().url(MOCK_SERVER.url("/")).build())
			.execute();
		// then
		assertUserAgentResponse(
			response,
			"MyTool/v0.0.0 (http://example.org/MyTool) okhttp/"
		);
	}

	@Test
	void given_userAgentAppendingToOriginal_when_executeWithInterceptor_then_userAgentHeaderDoesNotContainOkHttp()
		throws IOException {
		// given
		final UserAgentInterceptor testInterceptor =
			new UserAgentInterceptor(original ->
				"MyTool/v0.0.0 (http://example.org/MyTool) " + original
			);
		// when
		final Response response = new OkHttpClient.Builder()
			.addInterceptor(testInterceptor)
			.build()
			.newCall(new Request.Builder().url(MOCK_SERVER.url("/")).build())
			.execute();
		// then
		assertUserAgentResponse(
			response,
			"MyTool/v0.0.0 (http://example.org/MyTool) null"
		);
	}
}
