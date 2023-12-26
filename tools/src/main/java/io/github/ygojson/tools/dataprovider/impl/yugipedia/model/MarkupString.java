package io.github.ygojson.tools.dataprovider.impl.yugipedia.model;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * String that might contain markup from the Yugipedia API
 * that can be clenaup.
 */
@JsonSerialize(using = ToStringSerializer.class)
public final class MarkupString {

	/**
	 * Creates a new instance from the given string.
	 *
	 * @param value string value with possible markup.
	 *
	 * @return new instance.
	 */
	public static MarkupString of(final String value) {
		return new MarkupString(value, false);
	}

	/**
	 * Creates a new instance from the given string that might
	 * contain some japanese-specific markup too.
	 *
	 * @param value string value with possible markup.
	 *
	 * @return new instance (japanese-aware).
	 */
	public static MarkupString ofJapanese(final String value) {
		return new MarkupString(value, true);
	}

	/**
	 * New line pattern.
	 */
	private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\n");

	/**
	 * Comma separator pattern.
	 */
	private static final Pattern COMMA_SEPARATOR_PATTERN = Pattern.compile(", ");

	/**
	 * Semicolon separator pattern.
	 */
	private static final Pattern SEMICOLON_SEPARATOR_PATTERN = Pattern.compile(
		"; "
	);

	/**
	 * Line break markup pattern used along the wikitext.
	 */
	private static final Pattern MARKUP_BREAK_PATTERN = Pattern.compile("<br />");

	/**
	 * Internal link markup pattern used along the wikitext.
	 */
	private static final Pattern MARKUP_INTERNAL_LINK_REGEX = Pattern.compile(
		"\\[\\[(?:[^|\\]]+\\|)?([^\\]]+)\\]\\]"
	);

	/**
	 * Internal link markup replacement to be used with {@link #MARKUP_INTERNAL_LINK_REGEX}
	 */
	private static final String MARKUP_INTERNAL_LINK_REPLACEMENT = "$1";

	/**
	 * Italic markup pattern used along the wikitext.
	 */
	private static final Pattern MARKUP_ITALIC_REGEX = Pattern.compile(
		"''(.*?)''"
	);

	/**
	 * Italic markup replacement to be used with {@link #MARKUP_ITALIC_REGEX}
	 */
	private static final String MARKUP_ITALIC_REPLACEMENT = "$1";

	/**
	 * Pattern for japanese ruby syntax on the Yugipedia markup format.
	 */
	private static final Pattern MARKUP_RUBY_REGEX = Pattern.compile(
		"\\{\\{Ruby\\|(.*?)\\|(.*?)}}", Pattern.CASE_INSENSITIVE
	);

	/**
	 * Match group for the ruby kanji part of the {@link #MARKUP_RUBY_REGEX}.
	 */
	private static final int MARKUP_RUBY_MATCH_GROUP_KANJI = 1;
	/**
	 * Match group for the ruby furigana part of the {@link #MARKUP_RUBY_REGEX}.
	 */
	private static final int MARKUP_RUBY_MATCH_GROUP_FURIGANA = 2;

	/**
	 * Message format string for the HTML forma of japanese ruby.
	 * <br>
	 * The firs argument is the ruby kanji part and the second is the ruby furigana part.
	 */
	private static final String HTML_RUBY_MESSAGE_FORMAT =
		"<ruby>{0}<rt>{1}</rt></ruby>";

	private final String value;
	private final boolean expectedJapanese;

	private MarkupString(final String value, final boolean expectedJapanese) {
		this.value = value;
		this.expectedJapanese = expectedJapanese;
	}

	private Stream<MarkupString> splitByPattern(final Pattern pattern) {
		if (value == null) {
			return Stream.empty();
		}
		return pattern
			.splitAsStream(value)
			.map(value -> new MarkupString(value, this.expectedJapanese));
	}

	/**
	 * Checks if the string is all in italics.
	 *
	 * @return {@code true} if it starts and end in italic; {@code false} otherwise.
	 */
	public boolean isAllItalic() {
		if (value == null) {
			return false;
		}
		return MARKUP_ITALIC_REGEX.matcher(value).matches();
	}

	/**
	 * Splits the current markup string by new line.
	 *
	 * @param skipBlanks if {@code true}, skip blank lines;
	 *                   otherwise, keep them on the returned value.
	 * @return a list of the values separated by new lines.
	 */
	public Stream<MarkupString> splitByNewLine(final boolean skipBlanks) {
		final Stream<MarkupString> stream = splitByPattern(NEW_LINE_PATTERN);

		return skipBlanks ? stream.filter(ms -> !ms.value.isBlank()) : stream;
	}

	/**
	 * Splits the current markup string by comma.
	 *
	 * @return a list with the comma-separated values.
	 */
	public Stream<MarkupString> splitByComma() {
		return splitByPattern(COMMA_SEPARATOR_PATTERN);
	}

	/**
	 * Splits the current markup string by semicolon.
	 *
	 * @return a list with the semicolon-separated values.
	 */
	public Stream<MarkupString> splitBySemicolon() {
		return splitByPattern(SEMICOLON_SEPARATOR_PATTERN);
	}

	/**
	 * Splits the current markup string by breaks.
	 * <br>
	 * Note: empty lines are always removed.
	 *
	 * @param maxItems the maximum items to return
	 *                 (last one might contain breaks still if there are more).
	 * @return a list with the break-separated values.
	 */
	public List<MarkupString> splitByBreak(final int maxItems) {
		if (value == null) {
			return List.of();
		}
		return Arrays
			.stream(MARKUP_BREAK_PATTERN.split(value, maxItems))
			.filter(item -> !item.isEmpty()) // remove empty lines
			.map(value -> new MarkupString(value, this.expectedJapanese))
			.toList();
	}

	/**
	 * Returns as a string where all the markup have been removed.
	 * <br>
	 * Note that for japanese, this might not be true, as the ruby-encoding
	 * would be returned in the html-form.
	 *
	 * @return the string without (or with minimum required) markup.
	 */
	public String withoutMarkup() {
		if (value == null || value.isEmpty()) {
			return value;
		}
		String cleanup = value;
		if (expectedJapanese) {
			cleanup = replaceJapaneseRubyByTags(cleanup);
		}
		// first remove internal links
		cleanup =
			MARKUP_INTERNAL_LINK_REGEX
				.matcher(cleanup)
				.replaceAll(MARKUP_INTERNAL_LINK_REPLACEMENT);
		// then remove italic
		cleanup =
			MARKUP_ITALIC_REGEX
				.matcher(cleanup)
				.replaceAll(MARKUP_ITALIC_REPLACEMENT);

		// finally remove line-breaks
		return MARKUP_BREAK_PATTERN.matcher(cleanup).replaceAll("\n");
	}

	private String replaceJapaneseRubyByTags(final String text) {
		final Matcher matcher = MARKUP_RUBY_REGEX.matcher(text);
		final StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			final String kanji = matcher.group(MARKUP_RUBY_MATCH_GROUP_KANJI);
			final String furigana = matcher.group(MARKUP_RUBY_MATCH_GROUP_FURIGANA);
			matcher.appendReplacement(
				result,
				MessageFormat.format(HTML_RUBY_MESSAGE_FORMAT, kanji, furigana)
			);
		}
		matcher.appendTail(result);
		return result.isEmpty() ? text : result.toString();
	}

	/**
	 * Returns the string as-is (including the markup).
	 *
	 * @return the string as-is.
	 */
	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MarkupString that)) return false;
		return (
			expectedJapanese == that.expectedJapanese &&
			Objects.equals(value, that.value)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, expectedJapanese);
	}
}
