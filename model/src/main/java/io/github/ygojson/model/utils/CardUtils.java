package io.github.ygojson.model.utils;

import java.util.Set;

import io.github.ygojson.model.data.Card;

/**
 * General utilities for {@link Card}.
 */
public final class CardUtils {

	private static final String NORMAL_MONSTER = "normal";

	// list of extra-deck monsters
	private static final Set<String> EXTRA_DECK = Set.of(
		"fusion",
		"synchro",
		"xyz",
		"link"
	);

	/**
	 * Checks if the card represents a normal monster.
	 *
	 * @param card the card to check.
	 *
	 * @return {@code true} if the card represents a normal monster,
	 * 		   {@code false} otherwise.
	 */
	public static boolean isNormalMonster(final Card card) {
		return (
			card.getMonsterTypes() != null &&
			card.getMonsterTypes().stream().anyMatch(NORMAL_MONSTER::contains)
		);
	}

	/**
	 * Checks if the card represents an extra-deck monster.
	 *
	 * @param card the card to check.
	 *
	 * @return {@code true} if the card represents an extra-deck monster,
	 * 		   {@code false} otherwise.
	 */
	public static boolean isExtraDeckMonster(final Card card) {
		return (
			card.getMonsterTypes() != null &&
			card.getMonsterTypes().stream().anyMatch(EXTRA_DECK::contains)
		);
	}

	private CardUtils() {
		// Utility class
	}
}
