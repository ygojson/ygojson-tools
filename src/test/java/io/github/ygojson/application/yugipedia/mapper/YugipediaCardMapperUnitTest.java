package io.github.ygojson.application.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.application.logic.mapper.MappingException;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaCardMapperUnitTest {

	private static YugipediaCardMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = Mappers.getMapper(YugipediaCardMapper.class);
	}

	@Test
	void given_nullProperties_when_toCard_then_nullPrints() {
		// given
		final Map<String, YugipediaProperty> properties = null;
		// when
		final var card = MAPPER.toCard(properties);
		// then
		assertThat(card).isNull();
	}

	@Test
	void given_cardTypePropertyUnknown_when_toCard_then_throwsMappingException() {
		// given - i.e., https://yugipedia.com/wiki/Command_Duel-Use_Card
		final Map<String, YugipediaProperty> properties = Map.of(
			"card_type",
			YugipediaProperty.text("Command")
		);
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> {
			MAPPER.toCard(properties);
		};
		// then
		assertThatThrownBy(callable).isInstanceOf(MappingException.class);
	}
}
