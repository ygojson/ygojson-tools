package io.github.ygojson.model.utils.data.utils.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;
import static org.instancio.Select.field;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.model.utils.serialization.JsonUtils;

public class CardSerializationUnitTest {

	private static final List<String> FIELDS_TO_EXCLUDE = List.of(
		"name",
		"pendulumEffect",
		"effectText",
		"flavorText",
		"materials"
	);

	@Test
	void given_cardWithNullCardText_when_serialize_then_textNotPresent()
		throws JsonProcessingException {
		// given
		final var instancioBuilder = Instancio.of(Card.class);
		FIELDS_TO_EXCLUDE.forEach(f -> instancioBuilder.ignore(field(f)));
		final Card card = instancioBuilder.ignore(all(CardText.class)).create();
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(card);
		// then
		assertThat(value).doesNotContain(FIELDS_TO_EXCLUDE);
	}

	@Test
	void given_cardWithNonNullMainCardText_when_serialize_then_textPresent()
		throws JsonProcessingException {
		// given
		final Card card = Instancio
			.of(Card.class)
			.ignore(all(CardText.class))
			.create();
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(card);
		// then
		assertThat(value).contains(FIELDS_TO_EXCLUDE);
	}

	@Test
	void given_cardWithEmptyProperty_when_serialize_then_propertyNotPresent()
		throws JsonProcessingException {
		// given
		final Card card = new Card();
		card.setProperty("");
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(card);
		// then
		assertThat(value).isEqualTo("{}");
	}

	@Test
	void given_cardWithEmptyMonsterTypes_when_serialize_then_monsterTyepsNotPresent()
		throws JsonProcessingException {
		// given
		final Card card = new Card();
		card.setMonsterTypes(List.of());
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(card);
		// then
		assertThat(value).isEqualTo("{}");
	}

	@Test
	void given_cardWithNullMonsterTypes_when_serialize_then_monsterTyepsNotPresent()
		throws JsonProcessingException {
		// given
		final Card card = new Card();
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(card);
		// then
		assertThat(value).isEqualTo("{}");
	}
}
