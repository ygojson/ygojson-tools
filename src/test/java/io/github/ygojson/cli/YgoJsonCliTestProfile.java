package io.github.ygojson.cli;

import java.util.List;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.yugipedia.testutil.YugipediaMockResource;
import io.github.ygojson.runtime.YugipediaConfig;


public class YgoJsonCliTestProfile implements QuarkusTestProfile {

	private static final Logger LOG = LoggerFactory.getLogger(YgoJsonCliTestProfile.class);


	@Override
	public Map<String, String> getConfigOverrides() {
		return Map.of(YugipediaConfig.LIMIT_PROPERTY, "10");
	}

	@Override
	public List<TestResourceEntry> testResources() {
		LOG.info("Adding resources");
		return List.of(new TestResourceEntry(YugipediaMockResource.class));
	}

}
