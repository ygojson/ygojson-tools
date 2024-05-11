package io.github.ygojson.application.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaSetMapperUnitTest {

	private static YugipediaSetMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = Mappers.getMapper(YugipediaSetMapper.class);
	}

	@Test
	void given_nullProperties_when_toCard_then_nullPrints() {
		// given
		final Map<String, YugipediaProperty> properties = null;
		// when
		final var set = MAPPER.toSet(properties);
		// then
		assertThat(set).isNull();
	}
}
