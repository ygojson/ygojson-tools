package io.github.ygojson.application.logic.cardnumber;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.model.data.definition.localization.Region;

class CardNumberParserUnitTest {

	static CardNumber expectedPrefix(
		final String stringValue,
		final String setCode,
		final Region regionCode,
		final String printNumberPrefix
	) {
		return new CardNumber(
			stringValue,
			setCode,
			regionCode,
			printNumberPrefix,
			null,
			null
		);
	}

	static CardNumber expectedPrintNumberOnly(
		final String stringValue,
		final String setCode,
		final Region regionCode,
		final Integer printNumber
	) {
		return new CardNumber(
			stringValue,
			setCode,
			regionCode,
			null,
			printNumber,
			null
		);
	}

	static CardNumber expectedSpecialEdition(
		final String stringValue,
		final String setCode,
		final Region regionCode,
		final String specialEditionCode,
		final Integer printNumber
	) {
		return new CardNumber(
			stringValue,
			setCode,
			regionCode,
			specialEditionCode,
			printNumber,
			null
		);
	}

	static CardNumber expectedWithSuffix(
		final String stringValue,
		final String setCode,
		final Region regionCode,
		final Integer printNumber,
		final String suffix
	) {
		return new CardNumber(
			stringValue,
			setCode,
			regionCode,
			null,
			printNumber,
			suffix
		);
	}

	static CardNumber expectedUnreleased(
		final String stringValue,
		final String setCode,
		final Region regionCode,
		final Integer printNumber
	) {
		return new CardNumber(
			stringValue,
			setCode,
			regionCode,
			null,
			printNumber,
			"K"
		);
	}

	static Stream<String> noneStrings() {
		return Stream.of(null, "");
	}

	@ParameterizedTest
	@MethodSource("noneStrings")
	void given_noCardNumberValues_when_mapPrintCodeToCardNumber_then_cardNumberNone(
		final String value
	) {
		// given - params
		// when
		final CardNumber actual = CardNumberParser.parseCardNumber(
			value,
			List.of(Region.JA, Region.JP)
		);
		// then
		assertThat(actual).isEqualTo(CardNumber.NONE);
	}

	@ParameterizedTest
	@MethodSource("noneStrings")
	void given_noCardNumberValues_when_mapSetPrefixToCardNumber_then_cardNumberNone(
		final String value
	) {
		// given - params
		// when
		final CardNumber actual = CardNumberParser.parseCardNumber(
			value,
			List.of(Region.JA, Region.JP)
		);
		// then
		assertThat(actual).isEqualTo(CardNumber.NONE);
	}

	@Test
	void given_cardNumberWithAllFields_when_mapPrintCodeToCardNumber_then_correctCardNumber() {
		// given
		final String printCode = "AAAA-ENSP000S";
		// when
		final CardNumber actual = CardNumberParser.parseCardNumber(
			printCode,
			List.of(Region.EN)
		);
		// then
		assertThat(actual)
			.isEqualTo(new CardNumber(printCode, "AAAA", Region.EN, "SP", 0, "S"));
	}

	private static Stream<Arguments> unreleasedPrintCodes() {
		return Stream.of(
			Arguments.of(
				expectedPrefix("???-EN???", "???", Region.EN, "???"),
				"???-EN???",
				List.of(Region.EN)
			),
			Arguments.of(
				expectedPrintNumberOnly("????-JP001", "????", Region.JP, 1),
				"????-JP001",
				List.of(Region.JA, Region.JP)
			),
			// considered a special-edition as '??' is not really known yet
			// in that case, this would need to be parsed afterward,
			// meaning that the '?' string and the printNumber=0 should be cleanup
			Arguments.of(
				expectedWithSuffix("STP7-EN0??", "STP7", Region.EN, 0, "??"),
				"STP7-EN0??",
				List.of(Region.EN)
			)
		);
	}

	@ParameterizedTest
	@MethodSource("unreleasedPrintCodes")
	void given_unreleasedPrintCode_when_mapPrintCodeToCardNumber_then_cardNumberNone(
		final CardNumber expected,
		final String printCode,
		final List<Region> possibleRegions
	) {
		// given - params
		// when
		final CardNumber actual = CardNumberParser.parseCardNumber(
			printCode,
			possibleRegions
		);
		// then
		assertThat(actual).isEqualTo(expected);
	}

	static Stream<Arguments> printCodes() {
		return Stream.of(
			// Series 1 have its onw test cause it provides a CardNumber.NONE
			// Series 2
			Arguments.of(
				expectedPrintNumberOnly("MRL-01", "MRL", Region.NONE, 1),
				List.of(Region.JA, Region.JP)
			),
			// Series 3
			Arguments.of(
				expectedPrintNumberOnly("301-001", "301", Region.NONE, 1),
				List.of(Region.JA, Region.JP)
			),
			// single letter (for some localization)
			Arguments.of(
				expectedPrintNumberOnly("BIP-S001", "BIP", Region.S, 1),
				List.of(Region.SP, Region.S)
			),
			// participation card
			Arguments.of(
				expectedSpecialEdition("ANPR-ENSP1", "ANPR", Region.EN, "SP", 1),
				List.of(Region.EN)
			),
			// legendary deck (have some special cases)
			Arguments.of(
				expectedSpecialEdition("YGLD-ENA00", "YGLD", Region.EN, "A", 0),
				List.of(Region.EN)
			),
			Arguments.of(
				expectedSpecialEdition("LDK2-ENS01", "LDK2", Region.EN, "S", 1),
				List.of(Region.EN)
			),
			// K-Series
			Arguments.of(
				expectedWithSuffix("TLM-EN035K", "TLM", Region.EN, 35, "K"),
				List.of(Region.EN)
			)
		);
	}

	@ParameterizedTest
	@MethodSource("printCodes")
	void given_printCode_when_mapToCardNumber_then_correctCardNumber(
		final CardNumber expected,
		final List<Region> possibleRegions
	) {
		// given - params
		final String cardNumber = expected.stringValue();
		// when
		final CardNumber actual = CardNumberParser.parseCardNumber(
			cardNumber,
			possibleRegions
		);
		// then
		assertThat(actual).isEqualTo(expected);
	}

	static Stream<Arguments> setPrefixes() {
		return Stream.of(
			Arguments.of(
				expectedPrefix("MRL", "MRL", Region.NONE, null),
				List.of(Region.JA, Region.JP)
			),
			Arguments.of(
				expectedPrefix("301", "301", Region.NONE, null),
				List.of(Region.JA, Region.JP)
			),
			Arguments.of(
				expectedPrefix("BIP-S", "BIP", Region.S, null),
				List.of(Region.SP, Region.S)
			),
			Arguments.of(
				expectedPrefix("ANPR-ENSP", "ANPR", Region.EN, "SP"),
				List.of(Region.EN)
			),
			Arguments.of(
				expectedPrefix("ANPR-ENSE", "ANPR", Region.EN, "SE"),
				List.of(Region.EN)
			),
			Arguments.of(
				expectedPrefix("NECH-ENS", "NECH", Region.EN, "S"),
				List.of(Region.EN)
			),
			Arguments.of(
				expectedPrefix("SOVR-ENTK", "SOVR", Region.EN, "TK"),
				List.of(Region.EN)
			)
		);
	}

	@ParameterizedTest
	@MethodSource("setPrefixes")
	void given_setPrefix_when_mapToCardNumber_then_correctCardNumber(
		final CardNumber expected,
		final List<Region> possibleRegions
	) {
		// given - params
		final String setPrefix = expected.stringValue();
		// when
		final CardNumber actual = CardNumberParser.parseCardNumber(
			setPrefix,
			possibleRegions
		);
		// then
		assertThat(actual).isEqualTo(expected);
	}
}
