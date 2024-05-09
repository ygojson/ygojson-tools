package io.github.ygojson.application.yugipedia.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WikitextCleanerUnitTest {

	private static WikitextCleaner CLEANER;

	@BeforeAll
	static void beforeAll() {
		CLEANER = new WikitextCleaner();
	}

	static Stream<Arguments> consideredMarkups() {
		return Stream.of(
			Arguments.of(null, null),
			Arguments.of("", ""),
			Arguments.of("No markup", "No markup"),
			Arguments.of("Breaks:<br />first<br />second", "Breaks:\nfirst\nsecond"),
			Arguments.of("[[Link]]", "Link"),
			Arguments.of("Before[[Link]]", "BeforeLink"),
			Arguments.of("[[Link]]After", "LinkAfter"),
			Arguments.of("[[Link]]Middle[[Link]]", "LinkMiddleLink"),
			Arguments.of("[[Alt|Link]]", "Link"),
			Arguments.of("Before[[Alt|Link]]", "BeforeLink"),
			Arguments.of("[[Alt|Link]]After", "LinkAfter"),
			Arguments.of("[[Alt|Link]]Middle[[Alt|Link]]", "LinkMiddleLink"),
			Arguments.of("''Italic''", "Italic"),
			Arguments.of("Before''Italic''", "BeforeItalic"),
			Arguments.of("''Italic''After", "ItalicAfter"),
			Arguments.of("''Italic''Middle''Italic''", "ItalicMiddleItalic"),
			Arguments.of(
				"Before HTML-comment<!-- some comment-->",
				"Before HTML-comment"
			),
			Arguments.of(
				"<!-- some comment-->After HTML-comment",
				"After HTML-comment"
			),
			Arguments.of(
				"<!-- some comment-->Middle HTML-comment<!--other comment-->",
				"Middle HTML-comment"
			),
			Arguments.of(
				"Before <!--comment1-->Middle <!--comment2-->After",
				"Before Middle After"
			),
			Arguments.of("\ufeffBOM-string", "BOM-string")
		);
	}

	@ParameterizedTest
	@MethodSource("consideredMarkups")
	void given_consideredMarkupFromString_when_cleaned_then_correctlyCleanup(
		final String maybeMaybeRubyCharacters,
		final String expected
	) {
		// given - string
		// when
		final String cleaned = CLEANER.cleanupWikitext(maybeMaybeRubyCharacters);
		// then
		assertThat(cleaned).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("consideredMarkups")
	void given_consideredMarkupFromMaybeRubyCharactersString_when_cleaned_then_correctlyCleanup(
		final String maybeMaybeRubyCharacters,
		final String expected
	) {
		// given - string
		// when
		final String cleaned = CLEANER.cleanupWikitext(maybeMaybeRubyCharacters);
		// then
		assertThat(cleaned).isEqualTo(expected);
	}

	static Stream<Arguments> rubyMarkupProvider() {
		return Stream.of(
			Arguments.of(null, null),
			Arguments.of("", ""),
			Arguments.of("No markup", "No markup"), // exactly the same
			Arguments.of("{{ruby|創|そう}}", "<ruby>創<rt>そう</rt></ruby>"), // lower case
			Arguments.of("{{Ruby|創|そう}}", "<ruby>創<rt>そう</rt></ruby>"),
			Arguments.of(
				"Before{{Ruby|創|そう}}",
				"Before<ruby>創<rt>そう</rt></ruby>"
			),
			Arguments.of(
				"{{Ruby|創|そう}}After",
				"<ruby>創<rt>そう</rt></ruby>After"
			),
			Arguments.of(
				"{{Ruby|創|そう}}Middle{{Ruby|創|そう}}",
				"<ruby>創<rt>そう</rt></ruby>Middle<ruby>創<rt>そう</rt></ruby>"
			),
			Arguments.of( // real case
				"{{Ruby|創|そう}}{{Ruby|聖|せい}}{{Ruby|魔|ま}}{{Ruby|導|どう}}{{Ruby|王|おう}} エンディミオン",
				"<ruby>創<rt>そう</rt></ruby><ruby>聖<rt>せい</rt></ruby><ruby>魔<rt>ま</rt></ruby><ruby>導<rt>どう</rt></ruby><ruby>王<rt>おう</rt></ruby> エンディミオン"
			),
			Arguments.of( // real case - korean
				"{{Ruby|ＳＴＡＲＴＥＲ ＤＥＣＫ|조이 덱|lang=ko}} —페가수스 편—",
				"<ruby>ＳＴＡＲＴＥＲ ＤＥＣＫ<rt>조이 덱</rt></ruby> —페가수스 편—"
			)
		);
	}

	@ParameterizedTest
	@MethodSource("rubyMarkupProvider")
	void given_fromMaybeRubyCharactersString_when_cleaned_then_correctlyCleanup(
		final String maybeMaybeRubyCharacters,
		final String expected
	) {
		// given - string
		// when
		final String cleaned = CLEANER.cleanupWikitext(maybeMaybeRubyCharacters);
		// then
		assertThat(cleaned).isEqualTo(expected);
	}

	@Test
	void given_fromMaybeRubyCharactersStringWithRubyInsideMarkup_when_cleaned_then_cleanupAndWithRubyTags() {
		// given
		final String value =
			"[[{{Ruby|kanji1|furigana1}}]]<br />''{{Ruby|kanji2|furigana2}}''";
		// when
		final String cleaned = CLEANER.cleanupWikitext(value);
		// then
		assertThat(cleaned)
			.isEqualTo(
				"<ruby>kanji1<rt>furigana1</rt></ruby>\n<ruby>kanji2<rt>furigana2</rt></ruby>"
			);
	}
}
