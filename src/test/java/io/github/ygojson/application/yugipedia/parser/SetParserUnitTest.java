package io.github.ygojson.application.yugipedia.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SetParserUnitTest {

	private static YugipediaParser PARSER;

	@BeforeAll
	static void beforeAll() {
		PARSER = YugipediaParser.createSetParser();
	}

	@Test
	void given_nullWikitext_when_parse_then_returnNull() {
		// given
		final String title = "Hello World";
		final long pageId = 100L;
		final String wikitext = null;
		// when
		final var result = PARSER.parse(title, pageId, wikitext);
		// then
		assertThat(result).isNull();
	}

	@ParameterizedTest
	@ValueSource(
		strings = {
			"", // empty
			"   ", // blank
			"hello world", // any without expected format
		}
	)
	void given_arbitraryWikitext_when_parse_then_returnNull(
		final String wikitext
	) {
		// given
		final String title = "Hello World";
		final long pageId = 100L;
		// when
		final var result = PARSER.parse(title, pageId, wikitext);
		// then
		assertThat(result).isNull();
	}
}
