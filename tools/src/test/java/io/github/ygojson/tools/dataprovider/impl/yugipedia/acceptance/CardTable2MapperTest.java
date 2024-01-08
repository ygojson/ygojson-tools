package io.github.ygojson.tools.dataprovider.impl.yugipedia.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.CardTable2Mapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;

class CardTable2MapperTest {

	private static CardTable2Mapper MAPPER;
	private static ObjectMapper OBJECT_MAPPER;

	@BeforeAll
	static void beforeAll() {
		OBJECT_MAPPER =
			new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		MAPPER = Mappers.getMapper(CardTable2Mapper.class);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getCardTable2ParseWikitextTestData"
	)
	void testMapWikitextToCardTable2(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		// when
		final CardTable2 cardTable2 = MAPPER.mapWikitextToCardTable2(wikitext);
		final String asJsonString = OBJECT_MAPPER.writeValueAsString(cardTable2);
		// then
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters()
				.forFile()
				.withBaseName("CardTable2Mapper/" + wikitextTestData.testName())
				.forFile()
				.withExtension(".json")
		);
	}

	@Test
	void testMapNullWikitextToCardTable2() {
		// given
		final String wikitext = null;
		// when
		final CardTable2 cardTable2 = MAPPER.mapWikitextToCardTable2(wikitext);
		// then
		assertThat(cardTable2).isNull();
	}

	@Test
	void testMapArbitraryStringToCardTable2() {
		// given
		final String wikitext = "arbitrary string";
		// when
		final CardTable2 cardTable2 = MAPPER.mapWikitextToCardTable2(wikitext);
		// then
		assertThat(cardTable2).isNull();
	}
}
