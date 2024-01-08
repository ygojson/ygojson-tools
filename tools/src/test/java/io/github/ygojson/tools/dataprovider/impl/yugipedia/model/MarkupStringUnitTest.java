package io.github.ygojson.tools.dataprovider.impl.yugipedia.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MarkupStringUnitTest {

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
			Arguments.of("Before HTML-comment<!-- some comment-->", "Before HTML-comment"),
			Arguments.of("<!-- some comment-->After HTML-comment", "After HTML-comment"),
			Arguments.of("<!-- some comment-->Middle HTML-comment<!--other comment-->", "Middle HTML-comment"),
			Arguments.of("Before <!--comment1-->Middle <!--comment2-->After", "Before Middle After")
		);
	}

	@ParameterizedTest
	@MethodSource("consideredMarkups")
	void given_consideredMarkupFromString_when_withoutMarkup_then_correctlyCleanup(
		final String maybeMaybeRubyCharacters,
		final String expected
	) {
		// given
		final MarkupString markupString = MarkupString.of(maybeMaybeRubyCharacters);
		// when
		final String withoutMarkup = markupString.withoutMarkup();
		// then
		assertThat(withoutMarkup).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("consideredMarkups")
	void given_consideredMarkupFromMaybeRubyCharactersString_when_withoutMarkup_then_correctlyCleanup(
		final String maybeMaybeRubyCharacters,
		final String expected
	) {
		// given
		final MarkupString markupString = MarkupString.ofMaybeRubyCharacters(
			maybeMaybeRubyCharacters
		);
		// when
		final String withoutMarkup = markupString.withoutMarkup();
		// then
		assertThat(withoutMarkup).isEqualTo(expected);
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
	void given_fromMaybeRubyCharactersString_when_withoutMarkup_then_correctlyCleanup(
		final String maybeMaybeRubyCharacters,
		final String expected
	) {
		// given
		final MarkupString markupString = MarkupString.ofMaybeRubyCharacters(
			maybeMaybeRubyCharacters
		);
		// when
		final String withoutMarkup = markupString.withoutMarkup();
		// then
		assertThat(withoutMarkup).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("rubyMarkupProvider")
	void given_wrongFromStringForMaybeRubyCharacters_when_withoutMarkup_then_sameString(
		final String maybeMaybeRubyCharacters,
		final String expected
	) {
		// given
		final MarkupString markupString = MarkupString.of(maybeMaybeRubyCharacters);
		// when
		final String withoutMarkup = markupString.withoutMarkup();
		// then
		assertThat(withoutMarkup).isEqualTo(maybeMaybeRubyCharacters);
	}

	@Test
	void given_fromMaybeRubyCharactersStringWithRubyInsideMarkup_when_withoutMarkup_then_cleanupAndWithRubyTags() {
		// given
		final MarkupString markupString = MarkupString.ofMaybeRubyCharacters(
			"[[{{Ruby|kanji1|furigana1}}]]<br />''{{Ruby|kanji2|furigana2}}''"
		);
		// when
		final String withoutMarkup = markupString.withoutMarkup();
		// then
		assertThat(withoutMarkup)
			.isEqualTo(
				"<ruby>kanji1<rt>furigana1</rt></ruby>\n<ruby>kanji2<rt>furigana2</rt></ruby>"
			);
	}

	static Stream<Arguments> newLinesProvider() {
		return Stream.of(
			Arguments.of(null, false, List.of()),
			Arguments.of("", false, List.of("")),
			Arguments.of("", true, List.of()),
			Arguments.of("first second", false, List.of("first second")),
			Arguments.of("first\n", false, List.of("first")),
			Arguments.of("first\n", true, List.of("first")),
			Arguments.of("first\nsecond", false, List.of("first", "second")),
			Arguments.of("first\nsecond", true, List.of("first", "second")),
			Arguments.of(
				"first\nsecond\nthird",
				false,
				List.of("first", "second", "third")
			),
			Arguments.of("first\n\nthird", false, List.of("first", "", "third")),
			Arguments.of("first\n\nthird", true, List.of("first", "third"))
		);
	}

	@ParameterizedTest
	@MethodSource("newLinesProvider")
	void given_newLineSeparator_when_splitByNewLine_then_correctSplit(
		final String string,
		final boolean skipBlanks,
		final List<String> expectedResult
	) {
		// given
		final MarkupString markupString = MarkupString.of(string);
		// when
		final List<MarkupString> split = markupString
			.splitByNewLine(skipBlanks)
			.toList();
		// then
		final List<MarkupString> expected = expectedResult
			.stream()
			.map(MarkupString::of)
			.toList();
		assertThat(split)
			.describedAs("size")
			.hasSameSizeAs(expected)
			.describedAs("contents")
			.isEqualTo(expected);
	}

	static Stream<Arguments> breakLinesProvider() {
		return Stream.of(
			Arguments.of(null, 3, List.of()),
			Arguments.of("", 3, List.of()),
			Arguments.of("<br />", 2, List.of()),
			Arguments.of("<br /><br />", 2, List.of("<br />")),
			Arguments.of("<br /><br />", 3, List.of()),
			Arguments.of("first second", 3, List.of("first second")),
			Arguments.of("first<br />second", 1, List.of("first<br />second")),
			Arguments.of("first<br />second", 2, List.of("first", "second")),
			Arguments.of("first<br />second", 3, List.of("first", "second")),
			Arguments.of(
				"first<br />second<br />third",
				2,
				List.of("first", "second<br />third")
			),
			Arguments.of("first<br /><br />", 5, List.of("first"))
		);
	}

	@ParameterizedTest
	@MethodSource("breakLinesProvider")
	void given_stringWithBreaks_when_split_then_correctSplit(
		final String string,
		final int maxItems,
		final List<String> expectedResult
	) {
		// given
		final MarkupString markupString = MarkupString.of(string);
		// when
		final List<MarkupString> split = markupString.splitByBreak(maxItems);
		// then
		final List<MarkupString> expected = expectedResult
			.stream()
			.map(MarkupString::of)
			.toList();
		assertThat(split)
			.describedAs("size")
			.hasSameSizeAs(expected)
			.describedAs("contents")
			.isEqualTo(expected);
	}

	@Test
	void given_stringWithComma_when_splitByComma_then_correctSplit() {
		// given
		final MarkupString markupString = MarkupString.of("first, second");
		// when
		final List<MarkupString> split = markupString.splitByComma().toList();
		// then
		final List<MarkupString> expected = List.of(
			MarkupString.of("first"),
			MarkupString.of("second")
		);
		assertThat(split).isEqualTo(expected);
	}

	@Test
	void given_stringWithSemicolon_when_splitBySemicolon_then_correctSplit() {
		// given
		final MarkupString markupString = MarkupString.of("first; second");
		// when
		final List<MarkupString> split = markupString.splitBySemicolon().toList();
		// then
		final List<MarkupString> expected = List.of(
			MarkupString.of("first"),
			MarkupString.of("second")
		);
		assertThat(split).isEqualTo(expected);
	}

	@Test
	void given_fullItalicString_when_isAllItalic_then_returnTrue() {
		// given
		final MarkupString markupString = MarkupString.of("''All this is italic''");
		// when
		final boolean isAllItalic = markupString.isAllItalic();
		// then
		assertThat(isAllItalic).isTrue();
	}

	@Test
	void given_italicWithPreviousText_when_isAllItalic_then_returnFalse() {
		// given
		final MarkupString markupString = MarkupString.of(
			"This is a first sentence<br>''All this is italic''"
		);
		// when
		final boolean isAllItalic = markupString.isAllItalic();
		// then
		assertThat(isAllItalic).isFalse();
	}

	@Test
	void given_nullValueMarkupString_when_isAllItalic_then_returnFalse() {
		// given
		final MarkupString markupString = MarkupString.of(null);
		// when
		final boolean isAllItalic = markupString.isAllItalic();
		// then
		assertThat(isAllItalic).isFalse();
	}
}
