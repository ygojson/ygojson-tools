package io.github.ygojson.application.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaSetMapperUnitTest {

	private static YugipediaSetMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		// do not use CDI here as this is just unit-testing
		MAPPER = new YugipediaSetMapperImpl(new YugipediaPropertyBaseMapper());
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
