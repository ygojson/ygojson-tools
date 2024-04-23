package io.github.ygojson.application.yugipedia.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

class YugipediaClientIT extends AbstractYugipediaClientTest {

	private static final Logger LOG = LoggerFactory.getLogger(YugipediaClientIT.class);

	@Override
	protected YugipediaClient getClient() {
		return YugipediaClientMother.production();
	}

	@Override
	protected void logResponse(Response<?> response) {
		LOG.info("URL\t:{}", response.raw().request().url());
		LOG.info("Code\t:{}", response.code());
		LOG.info("Body\t:\t{}", response.body());
	}
}