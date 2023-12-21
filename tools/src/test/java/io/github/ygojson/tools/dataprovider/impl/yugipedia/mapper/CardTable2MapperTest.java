package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;

class CardTable2MapperTest {

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getParseWikitextTestData"
	)
	void testMapWikitextToCardTable2(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) {
		// given
		String wikitext = wikitextTestData.wikitext();
		// when
		CardTable2 cardTable2 = CardTable2Mapper.INSTANCE.mapWikitextToCardTable2(
			wikitext
		);
		// then
		Approvals.verify(
			cardTable2,
			Approvals.NAMES.withParameters(wikitextTestData.testName())
		);
	}

	@Test
	void testMapNullWikitextToCardTable2() {
		// given
		String wikitext = null;
		// when
		CardTable2 cardTable2 = CardTable2Mapper.INSTANCE.mapWikitextToCardTable2(
			wikitext
		);
		// then
		assertThat(cardTable2).isNull();
	}
}
