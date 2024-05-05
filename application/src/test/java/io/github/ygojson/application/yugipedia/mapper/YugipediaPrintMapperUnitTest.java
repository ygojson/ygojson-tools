package io.github.ygojson.application.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.application.testutil.model.PrintMother;
import io.github.ygojson.application.yugipedia.parser.model.SetRow;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.Region;

class YugipediaPrintMapperUnitTest {

	private static YugipediaPrintMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = Mappers.getMapper(YugipediaPrintMapper.class);
	}

	// includes edge-cases for future development when the printCode is split (see #55)
	static Stream<Arguments> printsToTest() {
		return Stream.of(
			// Series 1
			Arguments.of(
				"ja_sets",
				new SetRow(null, "Vol.1", List.of("Ultra Rare")),
				PrintMother.firstSeriesOf("Vol.1", "ultra rare", Language.JA)
			),
			// Series 2
			Arguments.of(
				"ja_sets",
				new SetRow("MRL-01", "Magic Ruler", List.of("Common")),
				PrintMother.of("MRL-01", "MRL", 1, "common", Language.JA, Region.NONE)
			),
			// Series 3
			Arguments.of(
				"ja_sets",
				new SetRow("301-001", "The New Ruler", List.of("Common")),
				PrintMother.of("301-001", "301", 1, "common", Language.JA, Region.NONE)
			),
			// single letter (for some localization)
			Arguments.of(
				"sp_sets",
				new SetRow("BIP-S001", "Starter Deck: Pegasus", List.of("Ultra Rare")),
				PrintMother.of(
					"BIP-S001",
					"BIP",
					1,
					"ultra rare",
					Language.ES,
					Region.S
				)
			),
			// participation card
			Arguments.of(
				"en_sets",
				new SetRow(
					"ANPR-ENSP1",
					"Ancient Prophecy Sneak Peek Participation Card",
					List.of("Ultra Rare")
				),
				PrintMother.specialOf(
					"ANPR-ENSP1",
					"ANPR",
					"SP",
					1,
					"ultra rare",
					Language.EN,
					Region.EN
				)
			),
			// legendary deck (have some special cases)
			Arguments.of(
				"en_sets",
				new SetRow(
					"YGLD-ENA00",
					"Yugi's Legendary Decks",
					List.of("Secret Rare")
				),
				PrintMother.specialOf(
					"YGLD-ENA00",
					"YGLD",
					"A",
					0,
					"secret rare",
					Language.EN,
					Region.EN
				)
			),
			Arguments.of(
				"en_sets",
				new SetRow("LDK2-ENS01", "Link Decks", List.of("Ultra Rare")),
				PrintMother.specialOf(
					"LDK2-ENS01",
					"LDK2",
					"S",
					1,
					"ultra rare",
					Language.EN,
					Region.EN
				)
			),
			// K-series
			Arguments.of(
				"en_sets",
				new SetRow("TLM-EN035K", "Tiamat Link Master", List.of("Ultra Rare")),
				PrintMother.kSeriesOf(
					"TLM-EN035K",
					"TLM",
					35,
					"ultra rare",
					Language.EN,
					Region.EN
				)
			),
			// unreleased
			// FIXME - decide if we should really keep this kind of prints as only a link between card-set
			// FIXME - https://github.com/ygojson/ygojson-tools/issues/5
			Arguments.of(
				"en_sets",
				new SetRow("????-EN0??", "Unknown Set", List.of("Super Rare")),
				PrintMother.of(null, null, null, "super rare", Language.EN, Region.EN)
			),
			Arguments.of(
				"en_sets",
				new SetRow("RA02-EN???", "25th Anniversary Rarity Collection II", null),
				PrintMother.of(null, "RA02", null, null, Language.EN, Region.EN)
			),
			Arguments.of(
				"en_sets",
				new SetRow(
					"RA02-EN???",
					"25th Anniversary Rarity Collection II",
					List.of("Super Rare")
				),
				PrintMother.of(null, "RA02", null, "super rare", Language.EN, Region.EN)
			)
		);
	}

	@ParameterizedTest
	@MethodSource("printsToTest")
	void given_singleEnPrint_when_mapToPrints_then_correctPrints(
		String setProperty,
		final SetRow row,
		final Print expected
	) {
		// given
		final Map<String, YugipediaProperty> properties = Map.of(
			setProperty,
			new YugipediaProperty.SetsProp(List.of(row))
		);
		// when
		final List<Print> actual = MAPPER.toPrints(properties);
		// then
		assertThat(actual).singleElement().isEqualTo(expected);
	}

	@Test
	void given_noSetProperties_when_mapToPrints_then_returnsEmptyList() {
		// given
		final Map<String, YugipediaProperty> properties = Map.of();
		// when
		final List<Print> actual = MAPPER.toPrints(properties);
		// then
		assertThat(actual).isEmpty();
	}

	@Test
	void given_cardTable2WithPrintWithSeveralRarities_wwhen_mapToPrints_then_returnAll() {
		// given
		final Map<String, YugipediaProperty> properties = Map.of(
			"en_sets",
			YugipediaProperty.sets(
				List.of(new SetRow("MS-EN001", "My set", List.of("Common", "Rare")))
			)
		);
		// when
		final List<Print> actual = MAPPER.toPrints(properties);
		// then
		final List<Print> expected = Stream
			.of("common", "rare")
			.map(rarity ->
				PrintMother.of("MS-EN001", "MS", 1, rarity, Language.EN, Region.EN)
			)
			.toList();
		assertThat(actual).isEqualTo(expected);
	}
}
