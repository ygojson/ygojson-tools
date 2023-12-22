package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.IntegerOrUndefined;

class IntegerOrUndefinedMapperUnitTest {

	private static IntegerOrUndefinedMapper TEST_MAPPER;

	@BeforeAll
	static void beforeAll() {
		TEST_MAPPER = Mappers.getMapper(IntegerOrUndefinedMapper.class);
	}

	@Test
	void given_nullValue_when_mapToInt_then_returnNull() {
		// given
		final IntegerOrUndefined nullValue = null;
		// when
		Integer result = TEST_MAPPER.mapToInt(nullValue);
		// then
		assertThat(result).isNull();
	}

	@Test
	void given_nullValue_when_mapToUndefined_then_returnFalsel() {
		// given
		final IntegerOrUndefined nullValue = null;
		// when
		boolean result = TEST_MAPPER.mapToUndefined(nullValue);
		// then
		assertThat(result).isFalse();
	}
}
