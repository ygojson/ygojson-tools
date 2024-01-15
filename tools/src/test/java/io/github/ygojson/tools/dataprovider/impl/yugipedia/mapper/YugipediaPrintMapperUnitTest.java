package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.test.CardTable2Mother;
import io.github.ygojson.tools.test.PrintMother;

class YugipediaPrintMapperUnitTest {

	private static final YugipediaPrintMapper MAPPER = Mappers.getMapper(
		YugipediaPrintMapper.class
	);

	// includes edge-cases for future development when the printCode is split (see #55)
	static Stream<Arguments> printsToTest() {
		return Stream.of(
			// Series 1
			Arguments.of(
				PrintMother.firstSeriesOf("Vol.1", "ultra rare", Language.JA),
				CardTable2Mother.withOnlyJaSets("; Vol.1; Ultra Rare")
			),
			// Series 2
			Arguments.of(
				PrintMother.of("MRL-01", "MRL", 1, "common", Language.JA, Region.NONE),
				CardTable2Mother.withOnlyJaSets("MRL-01; Magic Ruler; Common")
			),
			// Series 3
			Arguments.of(
				PrintMother.of("301-001", "301", 1, "common", Language.JA, Region.NONE),
				CardTable2Mother.withOnlyJaSets("301-001; The New Ruler; Common")
			),
			// single letter (for some localization)
			Arguments.of(
				 PrintMother.of(
					"BIP-S001",
					"BIP",
					1,
					"ultra rare",
					Language.ES,
					Region.S
				),
				CardTable2Mother.withOnlySpSets(
					"BIP-S001; Starter Deck: Pegasus; Ultra Rare"
				)
			),
			// participation card
			Arguments.of(
				PrintMother.specialOf(
					"ANPR-ENSP1",
					"ANPR",
					"SP",
					1,
					"ultra rare",
					Language.EN,
					Region.EN
				),
				CardTable2Mother.withOnlyEnSets(
					"ANPR-ENSP1; Ancient Prophecy Sneak Peek Participation Card; Ultra Rare"
				)
			),
			// legendary deck (have some special cases)
			Arguments.of(
				PrintMother.specialOf(
					"YGLD-ENA00",
					"YGLD",
					"A",
					0,
					"secret rare",
					Language.EN,
					Region.EN
				),
				CardTable2Mother.withOnlyEnSets(
					"YGLD-ENA00; Yugi's Legendary Decks; Secret Rare"
				)
			),
			Arguments.of(
				PrintMother.specialOf(
					"LDK2-ENS01",
					"LDK2",
					"S",
					1,
					"ultra rare",
					Language.EN,
					Region.EN
				),
				CardTable2Mother.withOnlyEnSets(
					"LDK2-ENS01; Legendary Decks II; Ultra Rare"
				)
			),
			// K-Series
			Arguments.of(
				PrintMother.kSeriesOf(
					"TLM-EN035K",
					"TLM",
					35,
					"ultra rare",
					Language.EN,
					Region.EN
				),
				CardTable2Mother.withOnlyEnSets("TLM-EN035K; ; Ultra Rare")
			)
		);
	}

	@ParameterizedTest
	@MethodSource("printsToTest")
	void given_singleEnPrint_when_mapToPrints_then_correctPrints(
		final Print expected,
		final CardTable2 cardTable2
	) {
		// given - params
		// when
		final List<Print> actual = MAPPER.mapToPrints(cardTable2);
		// then
		assertThat(actual).singleElement().isEqualTo(expected);
	}

	@Test
	void given_CardTable2WithoutSets_when_mapToPrints_then_returnsEmptyList() {
		// given
		final CardTable2 cardTable2 = CardTable2Mother.withoutSets();
		// when
		final List<Print> actual = MAPPER.mapToPrints(cardTable2);
		// then
		assertThat(actual).isEmpty();
	}

	@Test
	void given_cardTable2WithNotEnoughFields_when_mapToPrints_then_throwsIllegalStateException() {
		// given
		final CardTable2 cardTable2 = CardTable2Mother.withOnlyEnSets("No set");
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			MAPPER.mapToPrints(cardTable2);
		// then
		assertThatThrownBy(throwingCallable)
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void given_cardTable2WithManyFields_when_mapToPrints_then_returnCorrectPrint() {
		// given
		final CardTable2 cardTable2 = CardTable2Mother.withOnlyEnSets(
			"MS-EN001; My set; Rare; New column"
		);
		// when
		final List<Print> actual = MAPPER.mapToPrints(cardTable2);
		// then
		final Print expectedPrint = PrintMother.of(
			"MS-EN001",
			"MS",
			1,
			"rare",
			Language.EN,
			Region.EN
		);
		assertThat(actual).singleElement().isEqualTo(expectedPrint);
	}
}
