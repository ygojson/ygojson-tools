package io.github.ygojson.acceptancetest.yugipedia.mapper;

import java.util.List;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.acceptancetest.JsonAcceptance;
import io.github.ygojson.application.core.datastore.db.set.RawSet;
import io.github.ygojson.application.yugipedia.YugipediaTestDataRegistry;
import io.github.ygojson.application.yugipedia.mapper.YugipediaSetEntityMapper;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

@QuarkusTest
@Tag("acceptance-test")
class YugipediaEntitySetMapperAcceptanceTest {

	private static JsonAcceptance ACCEPTANCE = new JsonAcceptance();
	private static YugipediaParser PARSER;

	@Inject
	private YugipediaSetEntityMapper entityMapper;

	@BeforeAll
	static void beforeAll() {
		ACCEPTANCE = new JsonAcceptance();
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
	) {
		// given
		final Map<String, YugipediaProperty> properties = PARSER.parse(
			wikitextTestData.pageTitle(),
			wikitextTestData.pageId(),
			wikitextTestData.wikitext()
		);
		// when
		final RawSet entity = entityMapper.toEntity(properties);
		// then
		final String testCase = "set_entity/" + wikitextTestData.testName();
		ACCEPTANCE.verify(testCase, entity);
	}
}
