package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import org.approvaltests.Approvals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;

class CardTable2MapperTest {

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getParseWikitextTestData"
	)
	void testWikitextToCardTable2(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) {
		// given
		String wikitext = wikitextTestData.wikitext();
		// when
		CardTable2 cardTable2 = CardTable2Mapper.INSTANCE.wikitextToCardTable2(
			wikitext
		);
		// then
		Approvals.verify(
			cardTable2,
			Approvals.NAMES.withParameters(wikitextTestData.testName())
		);
	}
}
