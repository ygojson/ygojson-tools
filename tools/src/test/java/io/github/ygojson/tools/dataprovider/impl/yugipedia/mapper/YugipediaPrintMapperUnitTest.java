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
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;
import io.github.ygojson.tools.dataprovider.test.CardTable2Mother;

class YugipediaPrintMapperUnitTest {

	private static final YugipediaPrintMapper MAPPER = Mappers.getMapper(
		YugipediaPrintMapper.class
	);

	private static Print expectedFirstSeriesPrint(
		final String setName,
		final String rarity,
		final Language language
	) {
		final Print print = new Print();
		print.setFirstSeriesSet(setName);
		print.setRarity(rarity);
		print.setLanguage(language);
		return print;
	}

	private static Print expectedPrint(
		final String cardNumber,
		final String rarity,
		final Language language
	) {
		final Print print = new Print();
		print.setPrintCode(cardNumber);
		print.setRarity(rarity);
		print.setLanguage(language);
		return print;
	}

	// includes edge-cases for future development when the printCode is split (see #55)
	static Stream<Arguments> printsToTest() {
		return Stream.of(
			// Series 1
			Arguments.of(
				CardTable2Mother.withOnlyJaSets("; Vol.1; Ultra Rare"),
				expectedFirstSeriesPrint("Vol.1", "ultra rare", Language.JA)
			),
			// Series 2
			Arguments.of(
				CardTable2Mother.withOnlyJaSets("MRL-01; Magic Ruler; Common"),
				expectedPrint("MRL-01", "common", Language.JA)
			),
			// Series 3
			Arguments.of(
				CardTable2Mother.withOnlyJaSets("301-001; The New Ruler; Common"),
				expectedPrint("301-001", "common", Language.JA)
			),
			// single letter (for some localization)
			Arguments.of(
				CardTable2Mother.withOnlySpSets(
					"BIP-S001; Starter Deck: Pegasus; Ultra Rare"
				),
				expectedPrint("BIP-S001", "ultra rare", Language.ES)
			),
			// participation card
			Arguments.of(
				CardTable2Mother.withOnlyEnSets(
					"ANPR-ENSP1; Ancient Prophecy Sneak Peek Participation Card; Ultra Rare"
				),
				expectedPrint("ANPR-ENSP1", "ultra rare", Language.EN)
			),
			// legendary deck (have some special cases)
			Arguments.of(
				CardTable2Mother.withOnlyEnSets(
					"YGLD-ENA00; Yugi's Legendary Decks; Secret Rare"
				),
				expectedPrint("YGLD-ENA00", "secret rare", Language.EN)
			),
			Arguments.of(
				CardTable2Mother.withOnlyEnSets(
					"LDK2-ENS01; Legendary Decks II; Ultra Rare"
				),
				expectedPrint("LDK2-ENS01", "ultra rare", Language.EN)
			),
			// K-Series
			Arguments.of(
				CardTable2Mother.withOnlyEnSets("TLM-EN035K; ; Ultra Rare"),
				expectedPrint("TLM-EN035K", "ultra rare", Language.EN)
			)
		);
	}

	@ParameterizedTest
	@MethodSource("printsToTest")
	void given_singleEnPrint_when_mapToPrints_then_correctPrints(
		final CardTable2 cardTable2,
		final Print expected
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
		final ThrowableAssert.ThrowingCallable throwingCallable = () -> MAPPER.mapToPrints(cardTable2);
		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}

	@Test
	void given_cardTable2WithManyFields_when_mapToPrints_then_returnCorrectPrint() {
		// given
		final CardTable2 cardTable2 = CardTable2Mother.withOnlyEnSets("MS-EN001; My set; Rare; New column");
		// when
		final List<Print> actual = MAPPER.mapToPrints(cardTable2);
		// then
		final Print expectedPrint = expectedPrint("MS-EN001", "rare", Language.EN);
		assertThat(actual).singleElement().isEqualTo(expectedPrint);
	}

}
