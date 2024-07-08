package io.github.ygojson.acceptancetest.yugipedia.mapper;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.acceptancetest.JsonAcceptance;
import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.application.logic.mapper.SetMapper;
import io.github.ygojson.application.yugipedia.YugipediaTestDataRegistry;
import io.github.ygojson.application.yugipedia.mapper.YugipediaSetEntityMapper;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.utils.serialization.JsonUtils;

/**
 * Tests that the mapping to an entity and then to the model
 * always have the same result.
 */
@QuarkusTest
@Tag("acceptance-test")
class YugipediaSetAcceptanceTest {

	private static JsonAcceptance ACCEPTANCE = new JsonAcceptance();
	private static YugipediaParser PARSER;

	@Inject
	private YugipediaSetEntityMapper entityMapper;

	@Inject
	private SetMapper modelMapper;

	@BeforeAll
	static void beforeAll() {
		ACCEPTANCE = new JsonAcceptance(JsonUtils.getObjectMapper());
		PARSER = YugipediaParser.createSetParser();
	}

	static List<YugipediaTestDataRegistry.WikitextPageTestCase> testCases() {
		return YugipediaTestDataRegistry
			.getInstance()
			.getInfoboxSetWikitextTestCase();
	}

	@ParameterizedTest
	@MethodSource("testCases")
	void testPropertiesToSetModel(
		final YugipediaTestDataRegistry.WikitextPageTestCase wikitextTestData
	) throws JsonProcessingException {
		// given
		final Map<String, YugipediaProperty> properties = PARSER.parse(
			wikitextTestData.pageTitle(),
			wikitextTestData.pageId(),
			wikitextTestData.wikitext()
		);
		// when
		final SetEntity entity = entityMapper.toEntity(properties);
		final Set model = modelMapper.toModel(entity);
		// then
		final String testCase = "set/" + wikitextTestData.testName();
		ACCEPTANCE.verify(testCase, model);
	}
}
