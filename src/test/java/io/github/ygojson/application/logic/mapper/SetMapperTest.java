package io.github.ygojson.application.logic.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import org.instancio.Instancio;
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
			// TODO: remove with https://github.com/ygojson/ygojson-tools/issues/148
			.ignore(field(SetInfo.class, "nameAlt"))
			.create();
		// when
		final SetEntity entity = MAPPER.toEntity(model);
		final Set result = MAPPER.toModel(entity);
		// then
		assertThat(result).isEqualTo(model);
	}

	@Test
	void given_sameSetCodeOn2Languages_when_mapToSet_then_setInfoSetCodeNotPresentInNonEnglish() {
		// given
		final String setCode = "My Set";
		final SetEntity entity = Instancio
			.of(SetEntity.class)
			.set(field(SetEntity.class, "enSetCode"), setCode)
			.set(field(SetEntity.class, "deSetCode"), setCode)
			.create();
		// when
		final Set result = MAPPER.toModel(entity);
		// then
		assertThat(result.getLocalizedData().getDe())
			.isNotNull()
			.extracting(SetInfo::getSetCode)
			.isNull();
	}

	@Test
	void given_noLocalizedDataForLanguage_when_mapToSet_then_localizedDataIsNull() {
		// given
		final SetEntity entity = Instancio
			.of(SetEntity.class)
			.ignore(field(SetEntity.class, "deName"))
			.ignore(field(SetEntity.class, "deSetCode"))
			.ignore(field(SetEntity.class, "deSetCodeAlt"))
			.create();
		// when
		final Set result = MAPPER.toModel(entity);
		// then
		assertThat(result.getLocalizedData().getDe()).isNull();
	}
}
