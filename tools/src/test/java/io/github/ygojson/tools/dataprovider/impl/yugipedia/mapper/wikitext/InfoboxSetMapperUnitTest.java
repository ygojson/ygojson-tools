package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaTestData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;
import io.github.ygojson.tools.dataprovider.test.ModelTestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class InfoboxSetMapperUnitTest {

	private static final WikitextTemplateMapper WIKITEXT_MAPPER =
		Mappers.getMapper(WikitextTemplateMapper.class);
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
		final Map<String, String> wikitextMap =
			WIKITEXT_MAPPER.mapInfoboxSetTemplate(wikitext);
		// when
		final InfoboxSet infoboxSet = null; // MAPPER.mapToInfoboxSet(wikitextMap);
		// then
		final List<String> actualProperties =
			ModelTestUtils.extractSerializedProperties(infoboxSet);
		final List<String> expectedProperties = ModelTestUtils.extractProperties(
			wikitextMap
		);
		assertThat(actualProperties).containsAll(expectedProperties);
	}
}
