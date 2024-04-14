package io.github.ygojson.tools.dataprovider.impl.yugipedia.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.utils.serialization.JsonUtils;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.InfoboxSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;

class YugipediaSetMapperTest {

	private static ObjectWriter OBJECT_WRITER;
	private static YugipediaSetMapper MAPPER;
	private static InfoboxSetMapper INFOBOXSET_MAPPER;

	@BeforeAll
	static void beforeAll() {
		OBJECT_WRITER =
			JsonUtils.getObjectMapper().writerWithDefaultPrettyPrinter();
		MAPPER = Mappers.getMapper(YugipediaSetMapper.class);
		INFOBOXSET_MAPPER = Mappers.getMapper(InfoboxSetMapper.class);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getInfoboxSetParseWikitextTestData"
	)
	void testMapToSet(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		final String pageTitle = wikitextTestData.pageTitle();
		final InfoboxSet infoboxSet = INFOBOXSET_MAPPER.mapWikitextToInfoboxSet(
			wikitext
		);
		// when
		final Set set = MAPPER.mapToSet(infoboxSet, pageTitle);
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
