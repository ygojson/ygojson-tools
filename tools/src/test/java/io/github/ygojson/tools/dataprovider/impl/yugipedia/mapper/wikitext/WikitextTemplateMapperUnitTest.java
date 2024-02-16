package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class WikitextTemplateMapperUnitTest {

	private WikitextTemplateMapper wikitextTemplateMapper = Mappers.getMapper(WikitextTemplateMapper.class);

	@Test
	void given_cardTableWithSingleProperty_when_mapCardTable2Template_then_resultIsCorrect() {
		// given
		String wikitext = "{{CardTable2\n| en_name = Cool Name\n}}";
		// when
		final Map<String, String> actual = wikitextTemplateMapper.mapCardTable2Template(wikitext);
		// then
		assertThat(actual).isEqualTo(Map.of("en_name", "Cool Name"));
	}

	@Test
	void given_cardTableWithPropertyWhitespaceSeparated_when_mapCardTable2Template_then_resultIsCorrect() {
		// given
		String wikitext = "{{CardTable2\n| en_name =           Cool Name\n}}";
		// when
		final Map<String, String> actual = wikitextTemplateMapper.mapCardTable2Template(wikitext);
		// then
		assertThat(actual).isEqualTo(Map.of("en_name", "Cool Name"));
	}

	@Test
	void given_cardTableWithMultipleProperties_when_mapCardTable2Template_then_resultIsCorrect() {
		// given
		String wikitext = "{{CardTable2\n| en_name = Cool Name\n| en_lore = Cool Lore\n}}";
		// when
		final Map<String, String> actual = wikitextTemplateMapper.mapCardTable2Template(wikitext);
		// then
		final Map<String, String> expected = Map.of("en_name", "Cool Name", "en_lore", "Cool Lore");
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void given_cardTableWithOneFieldWithoutSeparation_when_mapCardTable2Template_then_resultIsCorrect() {
		// given
		String wikitext = "{{CardTable2\n|en_name = Cool Name\n}}";
		// when
		final Map<String, String> actual = wikitextTemplateMapper.mapCardTable2Template(wikitext);
		// then
		final Map<String, String> expected = Map.of("en_name", "Cool Name");
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void given_cardTableWithSecondFieldWithoutSeparation_when_mapCardTable2Template_then_resultIsCorrect() {
		// given
		String wikitext = "{{CardTable2\n| en_name = Cool Name\n|en_lore = Cool Lore\n}}";
		// when
		final Map<String, String> actual = wikitextTemplateMapper.mapCardTable2Template(wikitext);
		// then
		final Map<String, String> expected = Map.of("en_name", "Cool Name", "en_lore", "Cool Lore");
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void given_cardTableWithCommentedProperty_when_mapCardTable2Template_then_resultIsCorrect() {
		// given
		String wikitext = "{{CardTable2\n| database_id = 4999\n<!-- alt_art_3 = 3869 -->\n| en_name = Slifer the Sky Dragon\n}}";
		// when
		final Map<String, String> actual = wikitextTemplateMapper.mapCardTable2Template(wikitext);
		// then
		final Map<String, String> expected = Map.of(
			"database_id", "4999",
			"en_name", "Slifer the Sky Dragon");
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void given_cardTableWithMultiLineComment_when_mapCardTable2Template_then_resultIsCorrect() {
		// given
		String wikitext = "{{CardTable2\n| database_id = 4999\n<!-- \nfirst\nsecond\nthird\n-->\n| en_name = Slifer the Sky Dragon\n}}";
		// when
		final Map<String, String> actual = wikitextTemplateMapper.mapCardTable2Template(wikitext);
		// then
		final Map<String, String> expected = Map.of(
			"database_id", "4999",
			"en_name", "Slifer the Sky Dragon");
		assertThat(actual).isEqualTo(expected);
	}
}
