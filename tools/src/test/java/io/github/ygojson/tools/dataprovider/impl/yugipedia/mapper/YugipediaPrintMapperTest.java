package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;

class YugipediaPrintMapperTest {

	private static ObjectWriter OBJECT_WRITER;
	private static YugipediaPrintMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		OBJECT_WRITER =
			JsonUtils.getObjectMapper().writerWithDefaultPrettyPrinter();
		MAPPER = Mappers.getMapper(YugipediaPrintMapper.class);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getParseWikitextTestData"
	)
	void testMapToPrints(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		final CardTable2 cardTable2 =
			CardTable2Mapper.INSTANCE.mapWikitextToCardTable2(wikitext);
		// when
		final List<Print> card = MAPPER.mapToPrints(cardTable2);
		final String asJsonString = OBJECT_WRITER.writeValueAsString(card);
		// then
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters(wikitextTestData.testName())
				.forFile()
				.withExtension(".json")
		);
	}
}
