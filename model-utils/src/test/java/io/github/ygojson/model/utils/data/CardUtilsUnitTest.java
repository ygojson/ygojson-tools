package io.github.ygojson.model.utils.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.ygojson.model.data.Card;

class CardUtilsUnitTest {

	@ParameterizedTest
	@ValueSource(strings = { "fusion", "synchro", "xyz", "link" })
	void given_cardWithExtraDeckMonsterType_when_isExtraDeckMonster_then_returnsTrue(
		final String extraDeckType
	) {
		// given
		final Card card = new Card();
		card.setMonsterTypes(List.of("spellcaster", extraDeckType));
		// when
		final boolean isExtraDeckMonster = CardUtils.isExtraDeckMonster(card);
		// then
		assertThat(isExtraDeckMonster).isTrue();
	}

	@Test
	void given_cardWithoutExtraDeckMonsterType_when_isExtraDeckMonster_then_returnsFalse() {
		// given
		final Card card = new Card();
		card.setMonsterTypes(List.of("spellcaster"));
		// when
		final boolean isExtraDeckMonster = CardUtils.isExtraDeckMonster(card);
		// then
		assertThat(isExtraDeckMonster).isFalse();
	}

	@Test
	void given_cardWithNullMonsterTypes_when_isExtraDeckMonster_then_returnsFalse() {
		// given
		final Card card = new Card();
		// when
		final boolean isExtraDeckMonster = CardUtils.isExtraDeckMonster(card);
		// then
		assertThat(isExtraDeckMonster).isFalse();
	}
}
