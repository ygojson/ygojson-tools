package io.github.ygojson.application.yugipedia.client;

import org.slf4j.Logger;

import io.github.ygojson.application.testutil.server.MockedServer;

public class YugipediaMockServerFactory {

	private YugipediaMockServerFactory() {
		// cannot be instantiated
	}

	public static MockedServer create(final Logger log) {
		return new MockedServer("yugipedia/mockserver", log);
	}
}
