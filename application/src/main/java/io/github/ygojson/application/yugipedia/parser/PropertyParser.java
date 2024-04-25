package io.github.ygojson.application.yugipedia.parser;

import java.util.List;
import java.util.regex.Pattern;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class PropertyParser {

	private static final Pattern BULLETED_LIST_PATTERN = Pattern.compile("\\*");

	private static final Pattern COMMA_SEPARATOR_PATTERN = Pattern.compile(", ");

	/**
	 * Types for the parsing of the properties.
	 */
	enum Type {
		TEXT,
		BULLETED_LIST,
		COMMA_LIST,
		SET_ROWS,
	}

	private final WikitextCleaner cleaner;

	PropertyParser() {
		this.cleaner = new WikitextCleaner();
	}

	YugipediaProperty parse(final PropertyParser.Type type, String value) {
		if (value == null) {
			return null;
		}
		return switch (type) {
			case BULLETED_LIST -> parseListProperty(BULLETED_LIST_PATTERN, value);
			case COMMA_LIST -> parseListProperty(COMMA_SEPARATOR_PATTERN, value);
			// TODO: parse set rows
			case SET_ROWS -> parseTextProperty(value);
			// parse as text
			case TEXT -> parseTextProperty(value);
		};
	}

	private YugipediaProperty parseTextProperty(final String value) {
		return new YugipediaProperty.TextProp(parseString(value));
	}

	// helper method to be used to cleanup and trim all String values used in properties
	private String parseString(final String value) {
		return cleaner.cleanupWikitext(value.trim());
	}

	private YugipediaProperty parseListProperty(
		final Pattern pattern,
		final String value
	) {
		final List<String> stringList = pattern
			.splitAsStream(value)
			.map(this::parseString)
			.filter(s -> !s.isBlank()) // remove empty
			.toList();
		return new YugipediaProperty.ListProp(stringList);
	}
}
