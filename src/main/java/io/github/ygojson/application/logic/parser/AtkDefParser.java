package io.github.ygojson.application.logic.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.logic.AtkDefValues;

/**
 * Parser for the ATK/DEF valuesr as defined in Yu-Gi-Oh cards.
 * <br>
 * The logic should be called in order:
 * <ul>
 *     <li>{@link #parse(String)} to parse the value to an integer (not to the actual expected value)</li>
 *     <li>{@link #convertParsed(Integer)} to convert into the {@link AtkDefParser} model</li>
 * </ul>
 */
public final class AtkDefParser {

	private static final Logger LOG = LoggerFactory.getLogger(AtkDefParser.class);

	private static final Integer UNDEFINED = Integer.MIN_VALUE;

	private static final String UNDEFINED_NUMBER_STRING = "?";

	private AtkDefParser() {
		// no instances
	}

	/**
	 * Converts a value parsed with {@link #parse(String)} to a {@link AtkDefValues}.
	 *
	 * @param parsedValue the parsed value.
	 *
	 * @return the values to be used for ATK/DEF.
	 */
	public static AtkDefValues convertParsed(final Integer parsedValue) {
		if (isUndefined(parsedValue)) {
			return AtkDefValues.ofUndefined();
		}
		return AtkDefValues.ofValue(parsedValue);
	}

	/**
	 * Parse the ATK/DEF fields (whcih might be undefined).
	 * <br>
	 * This value shouldn't be used directly, but in combination with {@link #convertParsed(Integer)}.
	 *
	 * @param value the value to parse.
	 *
	 * @return the parsed value; an undefined marker value to be used.
	 */
	public static Integer parse(final String value) {
		if (value == null) {
			return null;
		}
		if (UNDEFINED_NUMBER_STRING.equals(value)) {
			return UNDEFINED;
		}
		try {
			return Integer.parseInt(value);
		} catch (final NumberFormatException e) {
			LOG.warn("Unknown number format considered null: {}", value);
			LOG.debug("Exception", e);
			return null;
		}
	}

	/**
	 * Checks if the value is undefined.
	 *
	 * @param value the value to check.
	 *
	 * @return {@code true} if the value is undefined; {@code false} otherwise.
	 */
	public static boolean isUndefined(final Integer value) {
		// null is not considered undefined
		if (value == null) {
			return false;
		}
		return value == UNDEFINED.intValue();
	}
}
