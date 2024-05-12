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
import io.github.ygojson.application.yugipedia.mapper.YugipediaSetMapper;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.testutil.JsonAcceptance;

@QuarkusTest
class YugipediaSetMapperTest {

	private static JsonAcceptance JSON_ACCEPTANCE;
	private static YugipediaParser PARSER;

	@Inject
	private YugipediaSetMapper mapper;

	@BeforeAll
	static void beforeAll() {
		JSON_ACCEPTANCE =
			JsonAcceptance.fromTestClass(YugipediaSetMapperTest.class);
		PARSER = YugipediaParser.createSetParser();
	}

	static List<YugipediaTestDataRegistry.WikitextPageTestCase> testCases() {
		return YugipediaTestDataRegistry
			.getInstance()
			.getInfoboxSetWikitextTestCase();
	}

	@ParameterizedTest
	@MethodSource("testCases")
	void testPropertiesToSet(
		final YugipediaTestDataRegistry.WikitextPageTestCase wikitextTestData
	) throws JsonProcessingException {
		// given
		final Map<String, YugipediaProperty> properties = PARSER.parse(
			wikitextTestData.pageTitle(),
			wikitextTestData.pageId(),
			wikitextTestData.wikitext()
		);
		// when
		final Set set = mapper.toSet(properties);
		// then
		final String testCase = "YugipediaSetMapper/" + wikitextTestData.testName();
		JSON_ACCEPTANCE.verify(testCase, set);
	}
}
