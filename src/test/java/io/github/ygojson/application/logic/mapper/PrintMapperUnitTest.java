package io.github.ygojson.application.logic.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.core.db.print.PrintEntity;
import io.github.ygojson.model.data.Print;

class PrintMapperUnitTest {

	private static PrintMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = new PrintMapperImpl();
	}

	@Test
	void given_instanceModel_when_roundtripMapping_then_equalObject() {
		// given
		final Print model = Instancio.of(Print.class).create();
		// when
		final PrintEntity entity = MAPPER.toEntity(model);
		final Print result = MAPPER.toModel(entity);
		// then
		assertThat(result).isEqualTo(model);
	}
}
