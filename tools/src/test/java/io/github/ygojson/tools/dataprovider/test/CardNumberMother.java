package io.github.ygojson.tools.dataprovider.test;

import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardNumber;

/**
 * Mother for {@link CardNumber} test data.
 */
public class CardNumberMother {

	private CardNumberMother() {
		// cannot be instantiated - mother class
	}

	public static CardNumber ofUnreleasedSetCode(
		final Region region,
		final Integer printNumber
	) {
		return new CardNumber(null, null, region, null, printNumber, null);
	}

	public static CardNumber ofUnreleasedPrintNumber(
		final Region region,
		final String setCode
	) {
		return new CardNumber(null, setCode, region, null, null, null);
	}

	public static CardNumber ofPrefix(
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

	public static CardNumber ofPrintNumberOnly(
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

	public static CardNumber ofSpecialEdition(
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

	public static CardNumber ofKSeries(
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
}
