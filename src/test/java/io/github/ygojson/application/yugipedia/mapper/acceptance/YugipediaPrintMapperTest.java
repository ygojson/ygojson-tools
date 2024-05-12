package io.github.ygojson.application.yugipedia.mapper.acceptance;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.acceptancetest.JsonAcceptance;
import io.github.ygojson.application.yugipedia.YugipediaTestDataRegistry;
import io.github.ygojson.application.yugipedia.mapper.YugipediaPrintMapper;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.utils.serialization.JsonUtils;

@QuarkusTest
class YugipediaPrintMapperTest {

	private static JsonAcceptance ACCEPTANCE = new JsonAcceptance();
	private static YugipediaParser PARSER;

	@Inject
	private YugipediaPrintMapper mapper;

	@BeforeAll
	static void beforeAll() {
		ACCEPTANCE = new JsonAcceptance(JsonUtils.getObjectMapper());
		PARSER = YugipediaParser.createCardParser();
	}

	static List<YugipediaTestDataRegistry.WikitextPageTestCase> testCases() {
		return YugipediaTestDataRegistry
			.getInstance()
			.getCardTable2WikitextTestCase();
	}

	@ParameterizedTest
	@MethodSource("testCases")
	void testPropertiesToPrint(
		final YugipediaTestDataRegistry.WikitextPageTestCase wikitextTestData
	) throws JsonProcessingException {
		// given
		final Map<String, YugipediaProperty> properties = PARSER.parse(
			wikitextTestData.pageTitle(),
			wikitextTestData.pageId(),
			wikitextTestData.wikitext()
		);
		// when
		final List<Print> prints = mapper.toPrints(properties);
		// then
		final String testCase =
			"YugipediaPrintMapper/" + wikitextTestData.testName();
		ACCEPTANCE.verify(testCase, prints);
	}
}
