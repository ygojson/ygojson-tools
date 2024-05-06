package io.github.ygojson.application.logic.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardType;
import io.github.ygojson.model.data.definition.LinkArrow;

class BaseCardMapperUnitTest {

	private static BaseCardMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = new BaseCardMapper();
	}

	@Test
	void given_questionMarkValue_when_toMaybeUndefinedLong_then_returnUndefined() {
		// given
		final String value = "?";
		// when
		final Long result = MAPPER.toMaybeUndefinedLong(value);
		// then
		assertThat(result).isEqualTo(-1L);
	}

	@ParameterizedTest
	@ValueSource(strings = { "null", "something", "???" })
	void given_nonParsableValue_when_toMaybeUndefinedLong_then_returnNull(
		final String nullableValue
	) {
		// given - value
		// when
		final Long result = MAPPER.toMaybeUndefinedLong(nullableValue);
		// then
		assertThat(result).isNull();
	}

	@ParameterizedTest
	@ValueSource(
		strings = {
			"MONSTER",
			"TRAP",
			"SPELL",
			"monster",
			"trap",
			"spell",
			"MoNsTeR",
			"TrAp",
			"SpElL",
		}
	)
	void given_cardType_when_toCardTypeEnum_then_returnValidEnum(
		final String value
	) {
		// given
		// when
		final CardType result = MAPPER.toCardTypeEnum(value);
		// then
		assertThat(result).isNotNull();
	}

	@ParameterizedTest
	@ValueSource(strings = { "counter", "token", "skill", "Command" })
	void given_invalidCardType_when_toCardTypeEnum_then_throwIllegalArgumentException(
		final String value
	) {
		// given
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			MAPPER.toCardTypeEnum(value);
		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(MappingException.class);
	}

	@Test
	void given_properlyFormedValue_when_toMonsterTypes_then_validReturn() {
		// given
		final String value = "Zombie / Tuner / Effect";
		// when
		final List<String> result = MAPPER.toMonsterTypes(value);
		// then
		assertThat(result).containsExactly("zombie", "tuner", "effect");
	}

	@Test
	void given_invalidSeparator_when_toMonsterTypes_then_resultIsWrong() {
		// given
		final String value = "Zombie,Tuner,Effect";
		// then
		final List<String> result = MAPPER.toMonsterTypes(value);
		// then
		assertThat(result).singleElement().isEqualTo("zombie,tuner,effect");
	}

	@Test
	void given_cardWithLinkArrows_when_computeLinkRating_then_linkRatingCorrect() {
		// given
		final Card card = new Card();
		card.setLinkArrows(List.of(LinkArrow.BOTTOM_CENTER, LinkArrow.BOTTOM_LEFT));
		// when
		MAPPER.computeLinkRating(card);
		// then
		assertThat(card).extracting(Card::getLinkRating).isEqualTo(2);
	}

	@Test
	void given_undefinedAtk_when_updateUndefinedAtk_then_correctCard() {
		// given
		final Card card = new Card();
		card.setAtk(-1);
		// when
		MAPPER.updateUndefinedAtk(card);
		// then
		assertThat(card)
			.extracting(Card::getAtkUndefined, Card::getAtk)
			.describedAs("atkUndefined, atk")
			.containsExactly(true, 0);
	}

	@Test
	void given_emptyCard_when_updateUndefinedAtk_then_correctCard() {
		// given
		final Card card = new Card();
		// when
		MAPPER.updateUndefinedAtk(card);
		// then
		assertThat(card).isEqualTo(new Card());
	}

	@Test
	void given_cardWithAtk_when_updateUndefinedAtk_then_correctCard() {
		// given
		final Card card = new Card();
		card.setAtk(1000);
		// when
		MAPPER.updateUndefinedAtk(card);
		// then
		assertThat(card).extracting(Card::getAtk).isEqualTo(1000);
	}

	@Test
	void given_undefinedDef_when_updateUndefinedDef_then_correctCard() {
		// given
		final Card card = new Card();
		card.setDef(-1);
		// when
		MAPPER.updateUndefinedDef(card);
		// then
		assertThat(card)
			.extracting(Card::getDefUndefined, Card::getDef)
			.describedAs("defUndefined, def")
			.containsExactly(true, 0);
	}

	@Test
	void given_emptyCard_when_updateUndefinedDef_then_correctCard() {
		// given
		final Card card = new Card();
		// when
		MAPPER.updateUndefinedDef(card);
		// then
		assertThat(card).isEqualTo(new Card());
	}

	@Test
	void given_cardWithDef_when_updateUndefinedDef_then_correctCard() {
		// given
		final Card card = new Card();
		card.setDef(1000);
		// when
		MAPPER.updateUndefinedDef(card);
		// then
		assertThat(card).extracting(Card::getDef).isEqualTo(1000);
	}
}
