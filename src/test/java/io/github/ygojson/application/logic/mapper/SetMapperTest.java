package io.github.ygojson.application.logic.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.SetInfo;

class SetMapperTest {

	private static SetMapper MAPPER;

	@BeforeAll
	static void beforeAll() {
		MAPPER = new SetMapperImpl();
	}

	@Test
	void given_instanceModel_when_roundtripMapping_then_equalObject() {
		// given
		final Set model = Instancio
			.of(Set.class)
			// TODO: this demonstrates that the nameAlt should not be part of the localizedData by now
			.ignore(Select.field(SetInfo.class, "nameAlt"))
			.create();
		// when
		final SetEntity entity = MAPPER.toEntity(model);
		final Set result = MAPPER.toModel(entity);
		// then
		assertThat(result).isEqualTo(model);
	}
}
