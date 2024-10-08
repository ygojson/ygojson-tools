package io.github.ygojson.application.logic.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.core.datastore.db.set.RawSet;
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
		final RawSet entity = MAPPER.toEntity(model);
		final Set result = MAPPER.toModel(entity);
		// then
		assertThat(result).isEqualTo(model);
	}

	@Test
	void given_sameSetCodeOn2Languages_when_mapToSet_then_setInfoSetCodeNotPresentInNonEnglish() {
		// given
		final String setCode = "My Set";
		final RawSet entity = Instancio.of(RawSet.class).create();
		entity.en.setCode = setCode;
		entity.de.setCode = setCode;
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
		final RawSet entity = Instancio.of(RawSet.class).create();
		entity.de.name = null;
		entity.de.setCode = null;
		entity.de.setCodeAlt = null;
		// when
		final Set result = MAPPER.toModel(entity);
		// then
		assertThat(result.getLocalizedData().getDe()).isNull();
	}
}
