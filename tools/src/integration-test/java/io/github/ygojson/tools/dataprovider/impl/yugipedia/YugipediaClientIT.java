package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import org.junit.jupiter.api.BeforeAll;

import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.AbstractYugipediaClientTest;
import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.YugipediaApiMother;

class YugipediaClientIT extends AbstractYugipediaClientTest {

	private static YugipediaClient PROD_API;

	@BeforeAll
	static void beforeAll() {
		PROD_API = YugipediaApiMother.productionTestClient();
	}


	@Override
	protected YugipediaClient getApi() {
		return PROD_API;
	}
}
