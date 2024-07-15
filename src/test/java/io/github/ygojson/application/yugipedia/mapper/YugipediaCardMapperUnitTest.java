package io.github.ygojson.application.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.logic.mapper.MappingException;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaCardEntityMapperUnitTest {

	private static YugipediaCardEntityMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		// do not use CDI here as this is just unit-testing
		MAPPER =
			new YugipediaCardEntityMapperImpl(
				new YugipediaPropertyBaseMapper(),
				new YugipediaLocalizedDataMapper()
			);
	}

	@Test
	void given_nullProperties_when_toEntity_then_nullEntity() {
		// given
		final Map<String, YugipediaProperty> properties = null;
		// when
		final CardEntity cardEntity = MAPPER.toEntity(properties);
		// then
		assertThat(cardEntity).isNull();
	}

	@Test
	void given_cardTypePropertyUnknown_when_toEntity_then_throwsMappingException() {
		// given - i.e., https://yugipedia.com/wiki/Command_Duel-Use_Card
		final Map<String, YugipediaProperty> properties = Map.of(
			"card_type",
			YugipediaProperty.text("Command")
		);
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> {
			MAPPER.toEntity(properties);
		};
		// then
		assertThatThrownBy(callable).isInstanceOf(MappingException.class);
	}
}
