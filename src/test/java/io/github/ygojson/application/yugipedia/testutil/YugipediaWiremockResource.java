package io.github.ygojson.application.yugipedia.testutil;

import io.github.ygojson.application.testutil.server.WiremockResource;
import io.github.ygojson.application.yugipedia.client.YugipediaClient;

/**
 * The Yugipedia {@link WiremockResource}.
 * <br>
 * IMPORTANT: this resource should not be used globally with the
 * {@link io.quarkus.test.common.QuarkusTestResource} annotation,
 * but rather with the provided {@link YugipediaMockProfile}.
 */
public class YugipediaWiremockResource extends WiremockResource {

	@Override
	public String getName() {
		return YugipediaClient.NAME;
	}
}
