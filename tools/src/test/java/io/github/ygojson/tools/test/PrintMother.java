package io.github.ygojson.tools.test;

import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.Region;

/**
 * Mother for {@link Print} test data.
 */
public class PrintMother {

	private PrintMother() {
		// cannot be instantiated - mother class
	}

	/**
	 * Creates a {@link Print}.
	 */
	public static Print of(
		final String cardNumber,
		final String setCode,
		final Integer printNumber,
		final String rarity,
		final Language language,
		final Region region
	) {
		final Print print = new Print();
		print.setPrintCode(cardNumber);
		print.setSetCode(setCode);
		print.setPrintNumber(printNumber);
		print.setRarity(rarity);
		print.setLanguage(language);
		print.setRegionCode(region);
		return print;
	}

	/**
	 * Creates a first-series {@link Print}.
	 */
	public static Print firstSeriesOf(
		final String setName,
		final String rarity,
		final Language language
	) {
		final Print print = new Print();
		print.setSetCode(setName);
		print.setRarity(rarity);
		print.setLanguage(language);
		return print;
	}

	/**
	 * Creates a special {@link Print}.
	 */
	public static Print specialOf(
		final String cardNumber,
		final String setCode,
		final String printPrefix,
		final Integer printNumber,
		final String rarity,
		final Language language,
		final Region region
	) {
		final Print print = of(
			cardNumber,
			setCode,
			printNumber,
			rarity,
			language,
			region
		);
		print.setPrintNumberPrefix(printPrefix);
		return print;
	}

	/**
	 * Creates a K-series {@link Print}.
	 */
	public static Print kSeriesOf(
		final String cardNumber,
		final String setCode,
		final Integer printNumber,
		final String rarity,
		final Language language,
		final Region region
	) {
		final Print print = of(
			cardNumber,
			setCode,
			printNumber,
			rarity,
			language,
			region
		);
		print.setPrintNumberSuffix("K");
		return print;
	}
}
