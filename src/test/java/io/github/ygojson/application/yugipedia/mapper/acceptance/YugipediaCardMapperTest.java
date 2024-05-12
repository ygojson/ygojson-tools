package io.github.ygojson.application.yugipedia.mapper.acceptance;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.application.yugipedia.YugipediaTestDataRegistry;
import io.github.ygojson.application.yugipedia.mapper.YugipediaCardMapper;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Card;
import io.github.ygojson.testutil.JsonAcceptance;

@QuarkusTest
class YugipediaCardMapperTest {

	private static JsonAcceptance JSON_ACCEPTANCE;
	private static YugipediaParser PARSER;

	@Inject
	private YugipediaCardMapper mapper;

	@BeforeAll
	static void beforeAll() {
		JSON_ACCEPTANCE =
			JsonAcceptance.fromTestClass(YugipediaCardMapperTest.class);
		PARSER = YugipediaParser.createCardParser();
	}

	static List<YugipediaTestDataRegistry.WikitextPageTestCase> testCases() {
		return YugipediaTestDataRegistry
			.getInstance()
			.getCardTable2WikitextTestCase();
	}

	@ParameterizedTest
	@MethodSource("testCases")
	void testPropertiesToCard(
		final YugipediaTestDataRegistry.WikitextPageTestCase wikitextTestData
	) throws JsonProcessingException {
		// given
		final Map<String, YugipediaProperty> properties = PARSER.parse(
			wikitextTestData.pageTitle(),
			wikitextTestData.pageId(),
			wikitextTestData.wikitext()
		);
		// when
		final Card card = mapper.toCard(properties);
		// then
		final String testCase =
			"YugipediaCardMapper/" + wikitextTestData.testName();
		JSON_ACCEPTANCE.verify(testCase, card);
	}
}
