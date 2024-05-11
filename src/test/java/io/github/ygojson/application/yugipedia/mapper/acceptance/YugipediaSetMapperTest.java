package io.github.ygojson.application.yugipedia.mapper.acceptance;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.application.yugipedia.YugipediaTestDataRegistry;
import io.github.ygojson.application.yugipedia.mapper.YugipediaSetMapper;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.utils.serialization.JsonUtils;

@QuarkusTest
class YugipediaSetMapperTest {

	private static ObjectWriter OBJECT_WRITER;
	private static YugipediaParser PARSER;

	@Inject
	private YugipediaSetMapper mapper;

	@BeforeAll
	static void beforeAll() {
		OBJECT_WRITER =
			JsonUtils.getObjectMapper().writerWithDefaultPrettyPrinter();
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
		final String asJsonString = OBJECT_WRITER.writeValueAsString(set);
		// then
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters()
				.forFile()
				.withBaseName("YugipediaSetMapper/" + wikitextTestData.testName())
				.forFile()
				.withExtension(".json")
		);
	}
}
