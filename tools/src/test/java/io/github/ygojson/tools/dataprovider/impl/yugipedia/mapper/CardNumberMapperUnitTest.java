package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardNumber;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.YugipediaLanguageRegion;
import io.github.ygojson.tools.dataprovider.test.CardNumberMother;

class CardNumberMapperUnitTest {

	private static final CardNumberMapper MAPPER = Mappers.getMapper(
		CardNumberMapper.class
	);

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
		final CardNumber actual = MAPPER.mapPrintCodeToCardNumber(
			value,
			YugipediaLanguageRegion.JP
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
		final CardNumber actual = MAPPER.mapSetPrefixToCardNumber(
			value,
			YugipediaLanguageRegion.JP
		);
		// then
		assertThat(actual).isEqualTo(CardNumber.NONE);
	}

	@Test
	void given_cardNumberWithAllFields_when_mapPrintCodeToCardNumber_then_correctCardNumber() {
		// given
		final String printCode = "AAAA-ENSP000S";
		// when
		final CardNumber actual = MAPPER.mapPrintCodeToCardNumber(
			printCode,
			YugipediaLanguageRegion.EN
		);
		// then
		assertThat(actual)
			.isEqualTo(new CardNumber(printCode, "AAAA", Region.EN, "SP", 0, "S"));
	}

	static Stream<Arguments> printCodes() {
		return Stream.of(
			// Series 1 have its onw test cause it provides a CardNumber.NONE
			// Series 2
			Arguments.of(
				CardNumberMother.ofPrintNumberOnly("MRL-01", "MRL", Region.NONE, 1),
				YugipediaLanguageRegion.JP
			),
			// Series 3
			Arguments.of(
				CardNumberMother.ofPrintNumberOnly("301-001", "301", Region.NONE, 1),
				YugipediaLanguageRegion.JP
			),
			// single letter (for some localization)
			Arguments.of(
				CardNumberMother.ofPrintNumberOnly("BIP-S001", "BIP", Region.S, 1),
				YugipediaLanguageRegion.ES
			),
			// participation card
			Arguments.of(
				CardNumberMother.ofSpecialEdition(
					"ANPR-ENSP1",
					"ANPR",
					Region.EN,
					"SP",
					1
				),
				YugipediaLanguageRegion.EN
			),
			// legendary deck (have some special cases)
			Arguments.of(
				CardNumberMother.ofSpecialEdition(
					"YGLD-ENA00",
					"YGLD",
					Region.EN,
					"A",
					0
				),
				YugipediaLanguageRegion.EN
			),
			Arguments.of(
				CardNumberMother.ofSpecialEdition(
					"LDK2-ENS01",
					"LDK2",
					Region.EN,
					"S",
					1
				),
				YugipediaLanguageRegion.EN
			),
			// K-Series
			Arguments.of(
				CardNumberMother.ofKSeries("TLM-EN035K", "TLM", Region.EN, 35),
				YugipediaLanguageRegion.EN
			)
		);
	}

	@ParameterizedTest
	@MethodSource("printCodes")
	void given_printCode_when_mapToCardNumber_then_correctCardNumber(
		final CardNumber expected,
		final YugipediaLanguageRegion region
	) {
		// given - params
		final String cardNumber = expected.stringValue();
		// when
		final CardNumber actual = MAPPER.mapPrintCodeToCardNumber(
			cardNumber,
			region
		);
		// then
		assertThat(actual).isEqualTo(expected);
	}

	static Stream<Arguments> setPrefixes() {
		return Stream.of(
			Arguments.of(
				CardNumberMother.ofPrefix("MRL", "MRL", Region.NONE, null),
				YugipediaLanguageRegion.JA
			),
			Arguments.of(
				CardNumberMother.ofPrefix("301", "301", Region.NONE, null),
				YugipediaLanguageRegion.JA
			),
			Arguments.of(
				CardNumberMother.ofPrefix("BIP-S", "BIP", Region.S, null),
				YugipediaLanguageRegion.ES
			),
			Arguments.of(
				CardNumberMother.ofPrefix("ANPR-ENSP", "ANPR", Region.EN, "SP"),
				YugipediaLanguageRegion.EN
			),
			Arguments.of(
				CardNumberMother.ofPrefix("ANPR-ENSE", "ANPR", Region.EN, "SE"),
				YugipediaLanguageRegion.EN
			),
			Arguments.of(
				CardNumberMother.ofPrefix("NECH-ENS", "NECH", Region.EN, "S"),
				YugipediaLanguageRegion.EN
			),
			Arguments.of(
				CardNumberMother.ofPrefix("SOVR-ENTK", "SOVR", Region.EN, "TK"),
				YugipediaLanguageRegion.EN
			)
		);
	}

	@ParameterizedTest
	@MethodSource("setPrefixes")
	void given_setPrefix_when_mapToCardNumber_then_correctCardNumber(
		final CardNumber expected,
		final YugipediaLanguageRegion region
	) {
		// given - params
		final String setPrefix = expected.stringValue();
		// when
		final CardNumber actual = MAPPER.mapSetPrefixToCardNumber(
			setPrefix,
			region
		);
		// then
		assertThat(actual).isEqualTo(expected);
	}
}
