package io.github.ygojson.model.utils.data;

import java.util.Set;

import io.github.ygojson.model.data.Card;

public class CardUtils {

	private static final String NORMAL = "normal";

	private static final Set<String> EXTRA_DECK = Set.of(
		"fusion",
		"synchro",
		"xyz",
		"link"
	);

	public static boolean isNormalMonster(final Card card) {
		return (
			card.getMonsterTypes() != null && card.getMonsterTypes().contains(NORMAL)
		);
	}

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
