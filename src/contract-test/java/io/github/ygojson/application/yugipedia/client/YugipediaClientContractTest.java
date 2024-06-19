package io.github.ygojson.application.yugipedia.client;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Tag;

@QuarkusTest
@Tag("contract-test")
class YugipediaClientContractTest extends AbstractYugipediaClientTest {

	@RestClient
	YugipediaClient yugipediaClient;

	@Override
	protected YugipediaClient getClient() {
		if (yugipediaClient == null) {
			throw new IllegalStateException("client not injected");
		}
		return yugipediaClient;
	}

}
