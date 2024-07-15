package io.github.ygojson.application.logic.parser;

import java.util.List;

import io.github.ygojson.application.logic.mapper.MappingException;

/**
 * Parser for the YGOJSON considered card-types (as defined in Yu-Gi-Oh cards)
 */
public final class CardTypeParser {

	private static final List<String> ACCEPTED_CARD_TYPES = List.of(
		"monster",
		"spell",
		"trap"
	);

	private CardTypeParser() {
		// no instances
	}

	/**
	 * Parse the card type.
	 *
	 * @param cardType the card type to parse.
	 * @return the card type.
	 * @throws MappingException if the card type is unknown
	 */
	public static String parse(String cardType) {
		if (cardType == null) {
			return null;
		}
		final String value = cardType.toLowerCase();
		if (ACCEPTED_CARD_TYPES.contains(value)) {
			return value;
		}
		throw new MappingException("Unknown card type: " + cardType);
	}
}
