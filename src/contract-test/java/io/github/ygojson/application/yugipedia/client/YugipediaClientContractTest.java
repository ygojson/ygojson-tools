package io.github.ygojson.application.yugipedia.client;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusTest
@Tag("contract-test")
class YugipediaClientContractTest extends AbstractYugipediaClientTest {

	private static final Logger LOG = LoggerFactory.getLogger(YugipediaClientContractTest.class);

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
