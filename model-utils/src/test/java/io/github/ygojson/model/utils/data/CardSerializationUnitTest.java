package io.github.ygojson.model.utils.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.model.utils.JsonUtils;

public class CardSerializationUnitTest {

	@Test
	void given_cardWithNullCardText_when_serialize_then_textNotPresent()
		throws JsonProcessingException {
		// given
		final Card card = Instancio
			.of(Card.class)
			.ignore(all(CardText.class))
			.create();
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(card);
		// then
		assertThat(value)
			.doesNotContain(
				"name",
				"pendulumEffect",
				"effectText",
				"flavorText",
				"materials"
			);
	}
}
