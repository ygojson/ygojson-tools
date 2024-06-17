package io.github.ygojson.application.yugipedia.testutil;

import java.util.List;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * Profile to be used on tests interacting with the yugipedia API,
 * to prevent that the real API is hit.
 */
public class YugipediaMockProfile implements QuarkusTestProfile {

	@Override
	public List<TestResourceEntry> testResources() {
		return List.of(new TestResourceEntry(YugipediaWiremockResource.class));
	}
}
