package io.github.ygojson.application.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaSetEntityMapperUnitTest {

	private static YugipediaSetEntityMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		// do not use CDI here as this is just unit-testing
		MAPPER =
			new YugipediaSetEntityMapperImpl(
				new YugipediaPropertyBaseMapper(),
				new YugipediaLocalizedDataMapper()
			);
	}

	@Test
	void given_nullProperties_when_toSetEntity_then_nullSet() {
		// given
		final Map<String, YugipediaProperty> properties = null;
		// when
		final var setEntity = MAPPER.toEntity(properties);
		// then
		assertThat(setEntity).isNull();
	}

	@Test
	void given_multipleEnSetCodes_when_toSetEntity_then_containSetCodes() {
		// given
		final Map<String, YugipediaProperty> properties = new HashMap<>(2);
		properties.put("prefix", YugipediaProperty.text("SRL"));
		properties.put("en_prefix", YugipediaProperty.text("MRL"));
		// when
		final var setEntity = MAPPER.toEntity(properties);
		// then
		assertSoftly(softly -> {
			softly.assertThat(setEntity.en.setCode).isEqualTo("SRL");
			softly.assertThat(setEntity.en.setCodeAlt).isEqualTo("MRL");
		});
	}
}
