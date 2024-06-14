package io.github.ygojson.application.yugipedia.testutil;

import java.util.Map;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.testutil.server.InjectMockedServer;
import io.github.ygojson.application.testutil.server.MockedServer;
import io.github.ygojson.application.yugipedia.client.YugipediaMockServerFactory;
import io.github.ygojson.runtime.YugipediaConfig;

public class YugipediaMockResource implements QuarkusTestResourceLifecycleManager {
	private static final Logger LOG = LoggerFactory.getLogger(YugipediaMockResource.class);

	private MockedServer server;

	@Override
	public Map<String, String> start() {
		LOG.info("Starting yugipedia mock resource");
		server = YugipediaMockServerFactory.create(LOG);
		return Map.of(YugipediaConfig.URL_PROPERTY, server.getUrl());
	}

	@Override
	public void stop() {
		LOG.info("Stopping yugipedia mock resource");
		try {
			server.close();
		} catch (Exception e) {
			LOG.error("Failed to close mocked-server", e);
		}
	}

	@Override
	public void inject(final TestInjector testInjector) {
		testInjector.injectIntoFields(server, new TestInjector.AnnotatedAndMatchesType(InjectMockedServer.class, MockedServer.class));
	}

}
