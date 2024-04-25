package io.github.ygojson.application.yugipedia.parser;

import java.text.MessageFormat;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to help cleaning up the wikitext.
 */
class WikitextCleaner {

	/**
	 * UFF-8 Byte Order Mark (BOM)
	 */
	private static final char UTF8_BOM = '\uFEFF';

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
	 * Pattern for japanese/korean/etc. ruby syntax on the Yugipedia markup format.
	 */
	private static final Pattern MARKUP_RUBY_REGEX = Pattern.compile(
		"\\{\\{Ruby\\|(.*?)\\|(.*?)(\\|.*?)?}}", // third optional group is for the ignored lang part
		Pattern.CASE_INSENSITIVE
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
	 * Message format string for the HTML ruby-syntax (for japanese/korean/etc.).
	 * <br>
	 * The firs argument is the ruby kanji part and the second is the ruby furigana part.
	 */
	private static final String HTML_RUBY_MESSAGE_FORMAT =
		"<ruby>{0}<rt>{1}</rt></ruby>";

	private static final Pattern MARKUP_HTML_COMMENT_REGEX = Pattern.compile(
		"<!--.*?-->"
	);

	/**
	 * Default constructor
	 */
	WikitextCleaner() {
		// NO-OP
	}

	public String cleanupWikitext(String value) {
		if (value == null || value.isEmpty()) {
			return value;
		}
		String cleanup = value;
		// replace special BOM-char marker
		if (cleanup.charAt(0) == UTF8_BOM) {
			cleanup = cleanup.substring(1);
		}
		cleanup =
			replaceRecursively(
				cleanup,
				MARKUP_RUBY_REGEX,
				WikitextCleaner::formatRubyHtmlFromMatcher
			);
		// replace HTML
		cleanup =
			replaceRecursively(cleanup, MARKUP_HTML_COMMENT_REGEX, matcher -> "");
		// first remove internal links
		cleanup =
			replaceAll(
				cleanup,
				MARKUP_INTERNAL_LINK_REGEX,
				MARKUP_INTERNAL_LINK_REPLACEMENT
			);
		// then remove italic
		cleanup =
			replaceAll(cleanup, MARKUP_ITALIC_REGEX, MARKUP_ITALIC_REPLACEMENT);
		// finally remove line-breaks
		return replaceAll(cleanup, MARKUP_BREAK_PATTERN, "\n");
	}

	private static String replaceRecursively(
		final String text,
		Pattern pattern,
		Function<Matcher, String> replaceFunction
	) {
		final Matcher matcher = pattern.matcher(text);
		final StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			matcher.appendReplacement(result, replaceFunction.apply(matcher));
		}
		matcher.appendTail(result);
		return result.toString();
	}

	private static String formatRubyHtmlFromMatcher(final Matcher matcher) {
		final String kanji = matcher.group(MARKUP_RUBY_MATCH_GROUP_KANJI);
		final String furigana = matcher.group(MARKUP_RUBY_MATCH_GROUP_FURIGANA);
		return MessageFormat.format(HTML_RUBY_MESSAGE_FORMAT, kanji, furigana);
	}

	private static String replaceAll(
		final String text,
		final Pattern pattern,
		final String replacement
	) {
		return pattern.matcher(text).replaceAll(replacement);
	}
}
