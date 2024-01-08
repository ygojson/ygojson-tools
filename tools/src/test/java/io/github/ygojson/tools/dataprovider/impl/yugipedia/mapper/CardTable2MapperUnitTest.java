package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;
import io.github.ygojson.tools.dataprovider.test.ModelTestUtils;

class CardTable2MapperUnitTest {

	private static CardTable2Mapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = Mappers.getMapper(CardTable2Mapper.class);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getCardTable2ParseWikitextTestData"
	)
	void given_wikitextMap_when_mapToCardTable2_then_allPropertiesContained(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) {
		// given
		final String wikitext = wikitextTestData.wikitext();
		final Map<String, String> wikitextMap = MAPPER.wikitextToMap(wikitext);
		// when
		final CardTable2 cardTable2 = MAPPER.mapToCardTable2(wikitextMap);
		// then
		final List<String> actualProperties =
			ModelTestUtils.extractSerializedProperties(cardTable2);
		final List<String> expectedProperties = ModelTestUtils.extractProperties(
			wikitextMap
		);
		assertThat(actualProperties).containsAll(expectedProperties);
	}
}
