package io.github.ygojson.application.logic.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.ygojson.application.core.datastore.db.card.RawCard;
import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardType;
import io.github.ygojson.model.data.definition.LinkArrow;

class CardMapperUnitTest {

	private static CardMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = new CardMapperImpl();
	}

	@Test
	void given_instanceModel_when_roundtripMapping_then_equalObject() {
		// given
		final Card model = Instancio.of(Card.class).create();
		// when
		final RawCard entity = MAPPER.toEntity(model);
		final Card result = MAPPER.toModel(entity);
		// then
		assertThat(result).isEqualTo(model);
	}

	@Test
	void given_noLocalizedDataForLanguage_when_mapToModel_then_localizedDataIsNull() {
		// given
		final RawCard entity = Instancio
			.of(RawCard.class)
			.set(field(RawCard.class, "cardType"), CardType.SPELL.value())
			.set(
				field(RawCard.class, "linkArrows"),
				List.of(LinkArrow.BOTTOM_CENTER.value(), LinkArrow.BOTTOM_RIGHT.value())
			)
			.create();
		entity.de.name = null;
		entity.de.effectText = null;
		entity.de.flavorText = null;
		entity.de.materialsText = null;
		entity.de.pendulumEffectText = null;
		// when
		final Card result = MAPPER.toModel(entity);
		// then
		assertThat(result.getLocalizedData().getDe()).isNull();
	}

	@ParameterizedTest
	@ValueSource(strings = { "unknown", "MONSTER", "SPELL", "TRAP" })
	void given_unknownCardType_when_mapToModel_then_throwMappingException(
		final String unknownValue
	) {
		// given
		final RawCard entity = new RawCard();
		entity.cardType = unknownValue;
		// when
		final ThrowableAssert.ThrowingCallable callable = () ->
			MAPPER.toModel(entity);
		// then
		assertThatThrownBy(callable).isInstanceOf(MappingException.class);
	}

	@ParameterizedTest
	@ValueSource(strings = { "unknown", "BOTTOM_CENTER", "BOTTOM_RIGHT" })
	void given_unknownLinkArrows_when_mapToModel_then_throwMappingException(
		final String unknownValue
	) {
		// given
		final RawCard entity = new RawCard();
		entity.linkArrows = List.of(unknownValue);
		// when
		final ThrowableAssert.ThrowingCallable callable = () ->
			MAPPER.toModel(entity);
		// then
		assertThatThrownBy(callable).isInstanceOf(MappingException.class);
	}
}
