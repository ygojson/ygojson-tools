package io.github.ygojson.application.yugipedia.client;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import io.github.ygojson.application.testutil.server.MockedServer;

class YugipediaClientUnitTest extends AbstractYugipediaClientTest {

	private static final Logger LOG = LoggerFactory.getLogger(
		YugipediaClientUnitTest.class
	);
	private static MockedServer MOCK_SERVER;

	@BeforeAll
	static void beforeEach() {
		MOCK_SERVER = YugipediaMockServerFactory.create(LOG);
	}

	@AfterAll
	static void afterEach() throws IOException {
		MOCK_SERVER.close();
	}

	@Override
	protected YugipediaClient getClient() {
		return YugipediaClientMother.mocked(MOCK_SERVER);
	}

	@Override
	protected void logResponse(Response<?> response) {
		// no logging for the unit tests
	}
}
