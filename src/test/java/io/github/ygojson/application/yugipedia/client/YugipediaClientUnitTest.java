package io.github.ygojson.application.yugipedia.client;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.github.ygojson.application.yugipedia.testutil.YugipediaMockProfile;

@QuarkusTest
@TestProfile(YugipediaMockProfile.class)
class YugipediaClientUnitTest extends AbstractYugipediaClientTest {

	@RestClient
	YugipediaClient client;

	@Override
	protected YugipediaClient getClient() {
		if (client == null) {
			throw new IllegalStateException("client not injected");
		}
		return client;
	}
}
