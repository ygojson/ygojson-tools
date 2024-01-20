package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.AbstractYugipediaApiTest;
import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.YugipediaApiMother;
import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.YugipediaMockServer;

class YugipediaApiUnitTest extends AbstractYugipediaApiTest {

	private static YugipediaMockServer MOCK_SERVER;

	@BeforeAll
	static void beforeEach() {
		MOCK_SERVER = new YugipediaMockServer();
	}

	@AfterAll
	static void afterEach() throws IOException {
		MOCK_SERVER.close();
	}

	protected YugipediaApi getApi() {
		return YugipediaApiMother.mockTestClient(MOCK_SERVER.getUrl());
	}
}
