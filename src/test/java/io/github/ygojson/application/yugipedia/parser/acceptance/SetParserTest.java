package io.github.ygojson.application.yugipedia.parser.acceptance;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.acceptancetest.JsonAcceptance;
import io.github.ygojson.application.yugipedia.YugipediaTestDataRegistry;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class SetParserTest {

	private static JsonAcceptance ACCEPTANCE = new JsonAcceptance();
	private static YugipediaParser PARSER;
	private static YugipediaTestDataRegistry TEST_DATA_REGISTRY;

	@BeforeAll
	static void beforeAll() {
		PARSER = YugipediaParser.createSetParser();
		TEST_DATA_REGISTRY = YugipediaTestDataRegistry.getInstance();
	}

	static List<YugipediaTestDataRegistry.WikitextPageTestCase> testCases() {
		return TEST_DATA_REGISTRY.getInfoboxSetWikitextTestCase();
	}

	@ParameterizedTest
	@MethodSource("testCases")
	void testParse(final YugipediaTestDataRegistry.WikitextPageTestCase testCase)
		throws JsonProcessingException {
		// given - test case
		// when
		final Map<String, YugipediaProperty> cardProperties = PARSER.parse(
			testCase.pageTitle(),
			testCase.pageId(),
			testCase.wikitext()
		);
		// then
		ACCEPTANCE.verify("set_properties/" + testCase.testName(), cardProperties);
	}
}
