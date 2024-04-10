package io.github.ygojson.tools.dataprovider.impl.yugipedia.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaPrintMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.CardTable2Mapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class YugipediaPrintMapperTest {

	private static ObjectWriter OBJECT_WRITER;
	private static YugipediaPrintMapper MAPPER;
	private static CardTable2Mapper CARDTABLE2_MAPPER;

	@BeforeAll
	static void beforeAll() {
		OBJECT_WRITER =
			JsonUtils.getObjectMapper().writerWithDefaultPrettyPrinter();
		MAPPER = Mappers.getMapper(YugipediaPrintMapper.class);
		CARDTABLE2_MAPPER = Mappers.getMapper(CardTable2Mapper.class);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getCardTable2ParseWikitextTestData"
	)
	void testMapToPrints(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		final String title = wikitextTestData.pageTitle();
		final CardTable2 cardTable2 = CARDTABLE2_MAPPER.mapWikitextToCardTable2(
			title,
			wikitext
		);
		// when
		final List<Print> card = MAPPER.mapToPrints(cardTable2);
		final String asJsonString = OBJECT_WRITER.writeValueAsString(card);
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

	@Test
	void testMapNullToPrints() {
		// given
		final CardTable2 cardTable2 = null;
		// when
		final List<Print> prints = MAPPER.mapToPrints(cardTable2);
		// then
		assertThat(prints).isNull();
	}
}
