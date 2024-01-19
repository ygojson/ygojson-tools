package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import org.junit.jupiter.api.BeforeAll;

import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.AbstractYugipediaApiTest;

class YugipediaApiIT extends AbstractYugipediaApiTest {

	private static YugipediaApi PROD_API;

	@BeforeAll
	static void beforeAll() {
		PROD_API = YugipediaApiMother.productionTestClient();
	}


	@Override
	protected YugipediaApi getApi() {
		return PROD_API;
	}
}
