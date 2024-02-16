package io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia;

import java.time.Duration;

import okhttp3.mockwebserver.MockWebServer;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaConfig;

public class YugipediaMockConfig extends YugipediaConfig {

	private final MockWebServer server;

	public YugipediaMockConfig(final MockWebServer server) {
		this.server = server;
	}

	@Override
	public String name() {
		return "yugipedia-mock";
	}

	@Override
	public String baseUrl() {
		return server.url("/").toString();
	}

	public RateLimit rateLimit() {
		return new RateLimit(1, Duration.ofMillis(1));
	}

}
