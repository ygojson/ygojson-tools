package io.github.ygojson.application.logic.mapper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.ygojson.model.data.definition.localization.Region;

/**
 * Parser for the card-number as defined in Yu-Gi-Oh cards.
 * <br>
 * The logic of {@link CardNumberParser#parseCardNumber(String, List)} can be used for
 * both print-numbers and set-prefixes.
 */
public class CardNumberParser {

	// Pattern to split the set-code from the rest of the print-number/set-suffix.
	private static final Pattern SET_CODE_SPLIT_PATTERN = Pattern.compile("-");

	// Pattern to divide the print-code to prefix, number and suffix.
	private static final Pattern PRINT_CODE_PATTERN = Pattern.compile(
		"(\\D*)(\\d*)(\\D*)"
	);
	// Group on {@link #PRINT_CODE_PATTERN} identifying the prefix.
	private static final int PRINT_CODE_PREFIX_GROUP = 1;
	// Group on {@link #PRINT_CODE_PATTERN} identifying the number.
	private static final int PRINT_CODE_NUMBER_GROUP = 2;
	// Group on {@link #PRINT_CODE_PATTERN} identifying the suffix.
	private static final int PRINT_CODE_SUFFIX_GROUP = 3;

	private CardNumberParser() {
		// no instances
	}

	/**
	 * Parse the card-number as defined in Yu-Gi-Oh cards.
	 *
	 * @param cardNumber the card number
	 * @param possibleRegions possible regions to match the print-number region code.
	 *
	 * @return the parsed card number.
	 */
	public static CardNumber parseCardNumber(
		final String cardNumber,
		final List<Region> possibleRegions
	) {
		// map first as a set-prefix to re-use the functionality
		final CardNumber setCodeParsed = parseSetCode(cardNumber, possibleRegions);
		if (setCodeParsed == CardNumber.NONE) {
			return CardNumber.NONE; // return the NONE
		}
		// no set-code, return as-is
		if (setCodeParsed.printNumberPrefix() == null) {
			return setCodeParsed;
		}
		// the prefix is considered a set-prefix
		final Matcher matcher = PRINT_CODE_PATTERN.matcher(
			setCodeParsed.printNumberPrefix()
		);
		// no match, no print-number information
		if (!matcher.matches()) {
			// return as-is
			return setCodeParsed;
		}
		final String prefix = matcher.group(PRINT_CODE_PREFIX_GROUP);
		final String number = matcher.group(PRINT_CODE_NUMBER_GROUP);
		final String suffix = matcher.group(PRINT_CODE_SUFFIX_GROUP);
		return new CardNumber(
			setCodeParsed.stringValue(),
			setCodeParsed.setCode(),
			setCodeParsed.regionCode(),
			blankToNull(prefix),
			parseNullableInteger(number),
			blankToNull(suffix)
		);
	}

	private static CardNumber parseSetCode(
		final String value,
		final List<Region> possibleRegions
	) {
		if (value == null || value.isBlank()) {
			return CardNumber.NONE;
		}
		// split the set-code from the rest of the print-number
		final String[] setCodeSplit = SET_CODE_SPLIT_PATTERN.split(value, 2);
		final String setCode = blankToNull(setCodeSplit[0]);
		final String printNumber = setCodeSplit.length == 1
			? null
			: blankToNull(setCodeSplit[1]);
		if (printNumber == null) { // only one split, no print-number
			return new CardNumber(value, setCode, Region.NONE, null, null, null);
		}
		// otherwise, we have a print number
		return parseSetCodeWithPrintNumber(
			value,
			setCode,
			printNumber,
			possibleRegions
		);
	}

	private static CardNumber parseSetCodeWithPrintNumber(
		final String value,
		final String setCode,
		final String printNumberPrefix,
		final List<Region> possibleRegions
	) {
		final Region regionCode = detectRegion(printNumberPrefix, possibleRegions);
		if (regionCode == null || regionCode == Region.NONE) {
			return new CardNumber(
				value,
				setCode,
				Region.NONE,
				printNumberPrefix,
				null,
				null
			);
		}

		final String printNumberPrefixWithoutRegionCode =
			printNumberPrefix.substring(regionCode.value().length());
		return new CardNumber(
			value,
			setCode,
			regionCode,
			blankToNull(printNumberPrefixWithoutRegionCode),
			null,
			null
		);
	}

	private static Region detectRegion(
		final String value,
		final List<Region> possibleRegions
	) {
		if (value == null) {
			return null;
		}
		Region regionCode = Region.NONE;
		for (final Region region : possibleRegions) {
			final String codeString = region.name();
			if (value.startsWith(codeString)) {
				regionCode = region;
				break;
			}
		}
		return regionCode;
	}

	private static String blankToNull(final String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value;
	}

	private static Integer parseNullableInteger(final String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return Integer.valueOf(value);
	}
}
