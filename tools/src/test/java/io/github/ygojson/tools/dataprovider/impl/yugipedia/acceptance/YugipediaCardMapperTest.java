package io.github.ygojson.tools.dataprovider.impl.yugipedia.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.utils.serialization.JsonUtils;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaCardMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.CardTable2Mapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;

class YugipediaCardMapperTest {

	private static ObjectWriter OBJECT_WRITER;
	private static YugipediaCardMapper MAPPER;
	private static CardTable2Mapper CARDTABLE2_MAPPER;

	@BeforeAll
	static void beforeAll() {
		OBJECT_WRITER =
			JsonUtils.getObjectMapper().writerWithDefaultPrettyPrinter();
		MAPPER = Mappers.getMapper(YugipediaCardMapper.class);
		CARDTABLE2_MAPPER = Mappers.getMapper(CardTable2Mapper.class);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getCardTable2ParseWikitextTestData"
	)
	void testMapToCardWithPageTitleAndId(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		final String pageTitle = wikitextTestData.pageTitle();
		final Long pageId = wikitextTestData.pageId();
		final CardTable2 cardTable2 = CARDTABLE2_MAPPER.mapWikitextToCardTable2(
			wikitext
		);
		// when
		final Card card = MAPPER.mapToCard(cardTable2, pageTitle, pageId);
		final String asJsonString = OBJECT_WRITER.writeValueAsString(card);
		// then
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters()
				.forFile()
				.withBaseName("YugipediaCardMapper/" + wikitextTestData.testName())
				.forFile()
				.withExtension(".json")
		);
	}
}
