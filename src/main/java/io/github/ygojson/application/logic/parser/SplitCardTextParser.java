package io.github.ygojson.application.logic.parser;

import static io.github.ygojson.application.logic.parser.MonsterTypesParser.*;

import java.util.List;

import io.github.ygojson.application.logic.SplitCardText;

/**
 * Parser for the parts of the card-text as defined in Yu-Gi-Oh cards.
 */
public final class SplitCardTextParser {

	private SplitCardTextParser() {
		// no instances
	}

	/**
	 * Parse splitting the full card-text in combination with the monster types.
	 *
	 * @param fullCardText the full card-text.
	 * @param monsterTypes the monster types (as parsed by {@link MonsterTypesParser#parse(String)}
	 * @return the split-text for the card.
	 */
	public static SplitCardText parse(
		final String fullCardText,
		List<String> monsterTypes
	) {
		SplitCardText splitCardText = new SplitCardText(fullCardText, null, null);
		// process extra-deck if necessary
		if (isExtraDeckMonster(monsterTypes)) {
			splitCardText = splitExtraDeckMonster(fullCardText);
		}
		// process flavor if necessary
		if (hasFlavorText(monsterTypes)) {
			splitCardText =
				new SplitCardText(
					null,
					splitCardText.materials(),
					splitCardText.effect()
				);
		}
		// set using the mapper, which considers null values already
		return splitCardText;
	}

	private static boolean hasFlavorText(List<String> monsterTypes) {
		// only if the effect is present or the card is a monster card
		if (monsterTypes == null || monsterTypes.isEmpty()) {
			return false;
		}
		final Boolean normalMonster = isCodedNormalMonster(monsterTypes);
		// null in case that the card
		if (normalMonster != null) {
			return normalMonster;
		}
		// if the normal-monster status is not coded, then we need to follow other rules
		// if the type contains an ability, then it is an effect monster
		if (!containsAbility(monsterTypes)) {
			return true;
		}
		return false;
	}

	private static SplitCardText splitExtraDeckMonster(
		final String fullCardText
	) {
		final String[] materialAndEffect = fullCardText.split("\n", 2);
		return switch (materialAndEffect.length) {
			// material and effect present
			case 2 -> new SplitCardText(
				materialAndEffect[1],
				materialAndEffect[0],
				null
			);
			// only materials are present (normal extra deck monster)
			case 1 -> new SplitCardText(null, materialAndEffect[0], null);
			// do nothing if the actual string is null/empty
			case 0 -> new SplitCardText(null, null, null);
			// this should not happen at all, so crash in case that it does!
			default -> throw new IllegalStateException(
				"should not happen: split-with-limit should return 0-2 items"
			);
		};
	}
}
