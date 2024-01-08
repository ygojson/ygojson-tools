package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.InfoboxSet;
import io.github.ygojson.tools.dataprovider.test.ModelTestUtils;

public class InfoboxSetMapperUnitTest {

	private static final InfoboxSetMapper MAPPER = Mappers.getMapper(
		InfoboxSetMapper.class
	);

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData#getInfoboxSetParseWikitextTestData"
	)
	void testAllInfoboxSetPropertiesIncluded(
		final YugipediaTestData.ParseWikitextPageTestData wikitextTestData
	) throws JsonProcessingException {
		// given
		final String wikitext = wikitextTestData.wikitext();
		final Map<String, String> wikitextMap = MAPPER.wikitextToMap(wikitext);
		// when
		final InfoboxSet infoboxSet = MAPPER.mapToInfoboxSet(wikitextMap);
		// then
		final List<String> actualProperties =
			ModelTestUtils.extractSerializedProperties(infoboxSet);
		final List<String> expectedProperties = ModelTestUtils.extractProperties(
			wikitextMap
		);
		assertThat(actualProperties).containsAll(expectedProperties);
	}
}
