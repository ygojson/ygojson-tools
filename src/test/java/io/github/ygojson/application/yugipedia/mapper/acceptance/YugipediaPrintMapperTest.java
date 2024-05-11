package io.github.ygojson.application.yugipedia.mapper.acceptance;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.application.yugipedia.YugipediaTestDataRegistry;
import io.github.ygojson.application.yugipedia.mapper.YugipediaPrintMapper;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.utils.serialization.JsonUtils;

class YugipediaPrintMapperTest {

	private static ObjectWriter OBJECT_WRITER;
	private static YugipediaPrintMapper MAPPER;
	private static YugipediaParser PARSER;

	@BeforeAll
	static void beforeAll() {
		OBJECT_WRITER =
			JsonUtils.getObjectMapper().writerWithDefaultPrettyPrinter();
		PARSER = YugipediaParser.createCardParser();
		MAPPER = Mappers.getMapper(YugipediaPrintMapper.class);
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
		final List<Print> prints = MAPPER.toPrints(properties);
		final String asJsonString = OBJECT_WRITER.writeValueAsString(prints);
		// then
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters()
				.forFile()
				.withBaseName("YugipediaPrintMapper/" + wikitextTestData.testName())
				.forFile()
				.withExtension(".json")
		);
	}
}
