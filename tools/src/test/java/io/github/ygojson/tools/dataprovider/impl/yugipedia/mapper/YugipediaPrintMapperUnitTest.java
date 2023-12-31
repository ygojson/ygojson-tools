package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.MarkupString;

class YugipediaPrintMapperUnitTest {

	private static YugipediaPrintMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = Mappers.getMapper(YugipediaPrintMapper.class);
	}

	private CardTable2 randomWithOnlyJaSets(final String jaSets) {
		return Instancio
			.of(CardTable2.class)
			.set(field(CardTable2::ja_sets), List.of(MarkupString.of(jaSets)))
			.ignore(field(CardTable2::en_sets))
			.ignore(field(CardTable2::na_sets))
			.ignore(field(CardTable2::eu_sets))
			.ignore(field(CardTable2::au_sets))
			.ignore(field(CardTable2::ae_sets))
			.ignore(field(CardTable2::de_sets))
			.ignore(field(CardTable2::sp_sets))
			.ignore(field(CardTable2::fr_sets))
			.ignore(field(CardTable2::fc_sets))
			.ignore(field(CardTable2::it_sets))
			.ignore(field(CardTable2::pt_sets))
			.ignore(field(CardTable2::jp_sets))
			.ignore(field(CardTable2::kr_sets))
			.ignore(field(CardTable2::sc_sets))
			.ignore(field(CardTable2::tc_sets))
			.create();
	}

	private static Print expectedJaPrint(
		final String setCode,
		final String regionCode,
		final String setNumber,
		final String rarity
	) {
		final Print print = new Print();
		print.setSetCode(setCode);
		print.setRegionCode(regionCode);
		print.setSetNumber(setNumber);
		print.setRarity(rarity);
		print.setLanguage(Language.JA);
		return print;
	}

	private static Print expectedJaPrint(
		final String firstSeriesSet,
		final String rarity
	) {
		final Print print = new Print();
		print.setFirstSeriesSet(firstSeriesSet);
		print.setRarity(rarity);
		print.setLanguage(Language.JA);
		return print;
	}

	static Stream<Arguments> singleEnPrints() {
		return Stream.of(
			// first-series test
			Arguments.of(
				"; Vol.1; Ultra Rare",
				expectedJaPrint("Vol.1", "ultra rare")
			),
			// series 2
			Arguments.of(
				"MRL-01; Magic Ruler; Common",
				expectedJaPrint("MRL", null, "01", "common")
			),
			// series 3
			Arguments.of(
				"301-001; The New Ruler; Common",
				expectedJaPrint("301", null, "001", "common")
			),
			// single letter (for some localization)
			Arguments.of(
				"BIP-S001; Starter Deck: Pegasus; Ultra Rare",
				expectedJaPrint("BIP", "S", "001", "ultra rare")
			),
			// participation card
			Arguments.of(
				"ANPR-ENSP1; Ancient Prophecy Sneak Peek Participation Card; Ultra Rare",
				expectedJaPrint("ANPR", "EN", "SP1", "ultra rare")
			),
			// legendary deck (have some special cases)
			Arguments.of(
				"YGLD-ENA00; Yugi's Legendary Decks; Secret Rare",
				expectedJaPrint("YGLD", "EN", "A00", "secret rare")
			),
			Arguments.of(
				"LDK2-ENS01; Legendary Decks II; Ultra Rare",
				expectedJaPrint("LDK2", "EN", "S01", "ultra rare")
			),
			// K-Series
			Arguments.of(
				"TLM-EN035K; ; Ultra Rare",
				expectedJaPrint("TLM", "EN", "035K", "ultra rare")
			)
		);
	}

	@ParameterizedTest
	@MethodSource("singleEnPrints")
	void given_singleEnPrint_when_mapToPrints_then_correctPrints(
		final String enSetsString,
		final Print expected
	) {
		final CardTable2 cardTable2 = randomWithOnlyJaSets(enSetsString);
		final List<Print> actual = MAPPER.mapToPrints(cardTable2);
		assertThat(actual).singleElement().isEqualTo(expected);
	}
}
