package io.github.ygojson.application.testutil.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.testutil.TestResourceUtil;

/**
 * Abstract class to implement and start wiremock resources.
 */
public abstract class WiremockResource
	implements QuarkusTestResourceLifecycleManager {

	private static final Logger LOG = LoggerFactory.getLogger(
		WiremockResource.class
	);

	private static final String WIREMOCK_FOLDER = "wiremock";

	WireMockServer mockServer;

	/**
	 * Gets the name of the wiremock resource.
	 * <br>
	 * This is used to get the both the base path under {@code resources/testdata}.
	 *
	 * @return the name of the wiremock resource.
	 */
	public abstract String getName();

	@Override
	public final Map<String, String> start() {
		LOG.info("Starting wiremock test-resource for " + getName());
		mockServer =
			new WireMockServer(
				WireMockConfiguration
					.options()
					.usingFilesUnderDirectory(getWiremockBasePath())
			);
		// log the requests and headers for testing
		mockServer.addMockServiceRequestListener((request, response) -> {
			LOG.debug(
				"Request: {}\nHeaders:\n{}",
				request.getAbsoluteUrl(),
				request.getHeaders()
			);
		});
		mockServer.start();
		// TODO: for https://github.com/ygojson/ygojson-tools/issues/141 a property would be required
		// TODO: to setup the quarkus.rest-client.{name}.url so it will be substittued
		return Map.of();
	}

	private String getWiremockBasePath() {
		final Path path = TestResourceUtil.getTestDataResourcePathByName(
			getName() + "/" + WIREMOCK_FOLDER
		);
		if (!Files.exists(path)) {
			throw new IllegalStateException(
				"Cannot load wiremock mappings from non-existent path: " + path
			);
		}
		return path.toAbsolutePath().toString();
	}

	/**
	 * Injects the WireMockServer (instace {@link WireMockServer} into the test.
	 *
	 * @param testInjector
	 */
	@Override
	public final void inject(final TestInjector testInjector) {
		LOG.info("Injecting {} wiremock", getName());
		testInjector.injectIntoFields(
			mockServer,
			new TestInjector.AnnotatedAndMatchesType(
				InjectWireMock.class,
				WireMockServer.class
			)
		);
	}

	/**
	 * Stops the mock-webserver.
	 */
	@Override
	public final void stop() {
		if (mockServer != null) {
			mockServer.stop();
		}
	}
}
