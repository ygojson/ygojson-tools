package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.MarkupString;

class MarkupStringMapperUnitTest {

	private static MarkupStringMapper TEST_MAPPER;

	@BeforeAll
	static void beforeAll() {
		TEST_MAPPER = Mappers.getMapper(MarkupStringMapper.class);
	}

	@Test
	void given_nullProperty_when_map_then_returnObject() {
		// given
		final String property = null;
		// when
		final MarkupString result = TEST_MAPPER.map(property);
		// then
		assertThat(result).isNotNull().extracting(MarkupString::toString).isNull();
	}

	@Test
	void given_nullProperty_when_mapToJapanese_then_returnObject() {
		// given
		final String property = null;
		// when
		final MarkupString result = TEST_MAPPER.mapToJapanese(property);
		// then
		assertThat(result).isNotNull().extracting(MarkupString::toString).isNull();
	}

	@Test
	void given_nullProperty_when_mapToNewLineList_then_returnEmptyList() {
		// given
		final String property = null;
		// when
		final List<MarkupString> result = TEST_MAPPER.mapToNewLineList(property);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_nullProperty_when_mapToCommaSeparatedStringList_then_returnEmptyList() {
		// given
		final String property = null;
		// when
		final List<String> result = TEST_MAPPER.mapToCommaSeparatedStringList(
			property
		);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_nullMarkupString_when_mapToString_then_returnNull() {
		// given
		final MarkupString property = null;
		// when
		final String result = TEST_MAPPER.map(property);
		// then
		assertThat(result).isNull();
	}
}
