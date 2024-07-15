package io.github.ygojson.application.logic.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class MonsterTypesParserUnitTest {

	@Test
	void given_properlyFormedValue_when_toMonsterTypes_then_validReturn() {
		// given
		final String value = "Zombie / Tuner / Effect";
		// when
		final List<String> result = MonsterTypesParser.parse(value);
		// then
		assertThat(result).containsExactly("zombie", "tuner", "effect");
	}

	@Test
	void given_invalidSeparator_when_toMonsterTypes_then_resultIsWrong() {
		// given
		final String value = "Zombie,Tuner,Effect";
		// then
		final List<String> result = MonsterTypesParser.parse(value);
		// then
		assertThat(result).singleElement().isEqualTo("zombie,tuner,effect");
	}
}
