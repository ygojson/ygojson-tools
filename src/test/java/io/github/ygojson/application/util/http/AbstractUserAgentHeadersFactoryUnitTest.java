package io.github.ygojson.application.util.http;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.io.IOException;
import java.net.URI;
import java.util.function.Function;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
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
class AbstractUserAgentHeadersFactoryUnitTest {

	private static final Logger LOG = LoggerFactory.getLogger(
		RateLimitInterceptorTest.class
	);

	private static WireMockServer MOCK_SERVER;

	@BeforeAll
	static void beforeAll() {
		MOCK_SERVER = new WireMockServer();
		MOCK_SERVER.start();
		MOCK_SERVER.addMockServiceRequestListener((request, response) -> {
			LOG.debug("Request header(s): {}", request.getHeaders());
		});
		new WireMock(MOCK_SERVER)
			.stubFor(
				get("/")
					.willReturn(
						aResponse()
							.withBody("{{request.headers.User-Agent}}")
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

	private DummyRestClient createClientWithUserAgentHeadersFactory(
		final Function<String, String> userAgentSupplier
	) {
		return QuarkusRestClientBuilder
			.newBuilder()
			.clientHeadersFactory(
				new AbstractUserAgentHeadersFactory() {
					@Override
					protected String getHeaderString(String originalHeader) {
						return userAgentSupplier.apply(originalHeader);
					}
				}
			)
			.baseUri(URI.create(MOCK_SERVER.url("/")))
			.build(DummyRestClient.class);
	}

	@Test
	void given_userAgentAppendingOriginal_when_clientCall_then_userAgentHeaderHasMoreHeaders()
		throws IOException {
		// given
		final DummyRestClient client =
			createClientWithUserAgentHeadersFactory(original ->
				"MyTool/v0.0.0 (http://example.org/MyTool) " + original
			);
		// when
		final Response response = client.getResponse();
		final String body = response.readEntity(String.class);
		// then
		assertSoftly(softly -> {
			softly
				.assertThat(response.getStatus())
				.describedAs("response status-code")
				.isEqualTo(200);
			softly
				.assertThat(body)
				.describedAs("body (user-agent string)")
				.matches("MyTool/v0\\.0\\.0 \\(http://example.org/MyTool\\) .+");
		});
	}

	@Test
	void given_userAgentRemovingOriginal_when_clientCall_then_userAgentHeaderDoesNotContainInfo()
		throws IOException {
		// given
		final DummyRestClient client =
			createClientWithUserAgentHeadersFactory(original ->
				"MyTool/v0.0.0 (http://example.org/MyTool)"
			);
		// when
		final Response response = client.getResponse();
		final String body = response.readEntity(String.class);
		// then
		assertSoftly(softly -> {
			softly
				.assertThat(response.getStatus())
				.describedAs("response status-code")
				.isEqualTo(200);
			softly
				.assertThat(body)
				.describedAs("body (user-agent string)")
				.isEqualTo("MyTool/v0.0.0 (http://example.org/MyTool)");
		});
	}
}
