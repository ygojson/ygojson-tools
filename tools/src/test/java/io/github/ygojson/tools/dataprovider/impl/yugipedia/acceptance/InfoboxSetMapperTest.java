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
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.InfoboxSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;

class InfoboxSetMapperTest {

	private static final InfoboxSetMapper MAPPER = Mappers.getMapper(
		InfoboxSetMapper.class
	);

	private static ObjectMapper OBJECT_MAPPER;

	@BeforeAll
	static void beforeAll() {
		OBJECT_MAPPER =
			new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getInfoboxSetParseWikitextTestData"
	)
	void testMapWikitextToInfoboxSet(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		// when
		final InfoboxSet infoboxSet = MAPPER.mapWikitextToInfoboxSet(wikitext);
		final String asJsonString = OBJECT_MAPPER.writeValueAsString(infoboxSet);
		// then
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters()
				.forFile()
				.withBaseName("InfoboxSetMapper/" + wikitextTestData.testName())
				.forFile()
				.withExtension(".json")
		);
	}

	@Test
	void testMapNullWikitextToInfoboxSet() {
		// given
		final String wikitext = null;
		// when
		final InfoboxSet infoboxSet = MAPPER.mapWikitextToInfoboxSet(wikitext);
		// then
		assertThat(infoboxSet).isNull();
	}

	@Test
	void testMapArbitraryStringToinfoboxSet() {
		// given
		final String wikitext = "arbitrary string";
		// when
		final InfoboxSet infoboxSet = MAPPER.mapWikitextToInfoboxSet(wikitext);
		// then
		assertThat(infoboxSet).isNull();
	}
}
