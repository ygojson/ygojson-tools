package io.github.ygojson.application.yugipedia.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import io.github.ygojson.application.testutil.server.InjectWireMock;
import io.github.ygojson.application.yugipedia.testutil.YugipediaMockProfile;

@QuarkusTest
@TestProfile(YugipediaMockProfile.class)
class YugipediaClientUnitTest extends AbstractYugipediaClientTest {

	private static final Logger LOG = LoggerFactory.getLogger(
		YugipediaClientUnitTest.class
	);

	@InjectWireMock
	WireMockServer mockServer;

	@Override
	protected YugipediaClient getClient() {
		return YugipediaClientMother.mocked(mockServer);
	}

	@Override
	protected void logResponse(Response<?> response) {
		// no logging for the unit tests
	}
}
