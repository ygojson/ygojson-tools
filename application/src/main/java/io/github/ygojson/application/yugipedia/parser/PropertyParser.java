package io.github.ygojson.application.yugipedia.parser;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import io.github.ygojson.application.yugipedia.parser.model.SetRow;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class PropertyParser {

	private static final Pattern BULLETED_LIST_PATTERN = Pattern.compile("\\*");

	private static final Pattern COMMA_SEPARATOR_PATTERN = Pattern.compile(", ");

	private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\n");

	private static final Pattern SEMICOLON_SEPARATOR_PATTERN = Pattern.compile(
		"; "
	);

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
			case SET_ROWS -> parseSetRowsProperty(value);
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

	private YugipediaProperty parseSetRowsProperty(String value) {
		final List<SetRow> setRows = NEW_LINE_PATTERN
			.splitAsStream(value)
			.filter(line -> !line.isBlank())
			.map(this::parseSingleSetRow)
			.filter(Objects::nonNull)
			.toList();
		return new YugipediaProperty.SetsProp(setRows);
	}

	private SetRow parseSingleSetRow(String line) {
		final String[] split = SEMICOLON_SEPARATOR_PATTERN.split(
			line,
			4 // parse one more field to remove extra columsn that might be not expected
		);
		return switch (split.length) {
			case 0 -> null;
			case 1 -> new SetRow(parseNullableText(split[0]), null, List.of());
			case 2 -> new SetRow(
				parseNullableText(split[0]),
				parseNullableText(split[1]),
				List.of()
			);
			default -> new SetRow(
				parseNullableText(split[0]),
				parseNullableText(split[1]),
				COMMA_SEPARATOR_PATTERN
					.splitAsStream(split[2])
					.filter(s -> !s.isBlank())
					.toList()
			);
		};
	}

	private String parseNullableText(final String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		if (trimmed.isEmpty()) {
			return null;
		}
		return trimmed;
	}
}
