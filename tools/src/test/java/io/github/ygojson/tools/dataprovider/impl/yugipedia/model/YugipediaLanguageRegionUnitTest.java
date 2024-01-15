package io.github.ygojson.tools.dataprovider.impl.yugipedia.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.MarkupString;
import io.github.ygojson.tools.dataprovider.test.CardTable2Mother;
import io.github.ygojson.tools.dataprovider.test.InfoboxSetMother;

class YugipediaLanguageRegionUnitTest {

	@ParameterizedTest
	@EnumSource(YugipediaLanguageRegion.class)
	void given_cardTable2withoutSets_when_getCardTable2Prints_then_returnNull(
		final YugipediaLanguageRegion region
	) {
		// given
		final CardTable2 cardTable2 = CardTable2Mother.withoutSets();
		// when
		final List<MarkupString> actual = region.getCardTable2Prints(cardTable2);
		// then
		assertThat(actual).isNull();
	}

	@ParameterizedTest
	@EnumSource(YugipediaLanguageRegion.class)
	void given_infoboxSetWithoutPrefixes_when_getInfoboxSetPrefixes_then_returnEmptyList(
		final YugipediaLanguageRegion region
	) {
		// given
		final InfoboxSet infoboxSet = InfoboxSetMother.withoutPrefixes();
		// when
		final List<String> actual = region.getInfoboxSetPrefixes(infoboxSet);
		// then
		assertThat(actual).isNull();
	}
}
