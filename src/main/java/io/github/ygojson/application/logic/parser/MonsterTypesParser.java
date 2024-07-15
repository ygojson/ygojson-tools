package io.github.ygojson.application.logic.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.ygojson.application.logic.mapper.MappingException;


/**
 * Parser for the YGOJSON considered monster-types (as defined in Yu-Gi-Oh cards).
 */
public final class MonsterTypesParser {

	// monster type separator
	private static final String MONSTER_TYPE_SEPARATOR = "/";

	private static final String NORMAL_MONSTER = "normal";
	private static final String EFFECT_MONSTER = "effect";
	// list of extra-deck monsters
	private static final Set<String> EXTRA_DECK = Set.of(
		"fusion",
		"synchro",
		"xyz",
		"link"
	);
	private static final Set<String> ABILITY_TYPES = Set.of(
		"toon",
		"spirit",
		"union",
		"gemini",
		"flip"
	);

	private MonsterTypesParser() {
		// no instances
	}

	/**
	 * Parse the monster-type string.
	 *
	 * @param typesString the monster-type line to parse.
	 * @return the list of monster-types.
	 * @throws MappingException if the card type is unknown.
	 */
	public static List<String> parse(String typesString) {
		if (typesString != null) {
			return Arrays
				.stream(typesString.split(MONSTER_TYPE_SEPARATOR))
				.map(value -> value.trim().toLowerCase())
				.collect(Collectors.toList());
		}
		return null;
	}

	static boolean isExtraDeckMonster(final List<String> monsterTypes) {
		return (
			monsterTypes != null &&
			monsterTypes.stream().anyMatch(EXTRA_DECK::contains)
		);
	}

	static Boolean isCodedNormalMonster(List<String> monsterTypes) {
		for (final String monsterType : monsterTypes) {
			if (NORMAL_MONSTER.equals(monsterType)) {
				return true;
			}
			if (EFFECT_MONSTER.equals(monsterType)) {
				return false;
			}
		}
		return null;
	}

	static boolean containsAbility(List<String> monsterTypes) {
		return monsterTypes.stream().anyMatch(ABILITY_TYPES::contains);
	}
}
