package io.github.ygojson.tools.dataprovider.impl.yugipedia.acceptance;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.WikitextTemplateMapper;

class WikitextTemplateMapperTest {

	private static WikitextTemplateMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = Mappers.getMapper(WikitextTemplateMapper.class);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getCardTable2ParseWikitextTestData"
	)
	void testMapWikitextToCardTable2Template(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		// when
		final Map<String, String> cardTable2 = MAPPER.mapCardTable2Template(
			wikitext
		);
		ParseVerifier
			.getInstance()
			.verifySerializedProperties(
				"card_properties/" + wikitextTestData.testName(),
				cardTable2
			);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getInfoboxSetParseWikitextTestData"
	)
	void testMapWikitextToInfoboxSetTemplate(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		// when
		final Map<String, String> infoboxSet = MAPPER.mapInfoboxSetTemplate(
			wikitext
		);
		// then
		ParseVerifier
			.getInstance()
			.verifySerializedProperties(
				"set_properties/" + wikitextTestData.testName(),
				infoboxSet
			);
	}
}
