package io.github.ygojson.application.logic.mapper;

import io.github.ygojson.model.data.definition.localization.Region;

/**
 * Represents the card number or set-prefix data.
 * <br>
 * Follows the rules of the <a href="https://yugipedia.com/wiki/Card_Number">Yugipedia CardNumber</a> description.

 * @param stringValue the cardNumber as a String
 * @param setCode the code or abbreviation of the set this prints belongs to
 * @param regionCode the region code
 * @param printNumberPrefix the prefix for the print number (i.e., if multiple subsets or promotional cards)
 * @param printNumber the actual print number (without zeroes)
 * @param printNumberSuffix print-number suffix (i.e., for K-series)
 */
public record CardNumber(
	String stringValue,
	String setCode,
	Region regionCode,
	String printNumberPrefix,
	Integer printNumber,
	String printNumberSuffix
) {
	/**
	 * Instance representing no CardNumber.
	 */
	public static final CardNumber NONE = new CardNumber(
		null,
		null,
		null,
		null,
		null,
		null
	);
}
