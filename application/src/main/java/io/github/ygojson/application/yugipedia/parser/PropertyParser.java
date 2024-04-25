package io.github.ygojson.application.yugipedia.parser;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class PropertyParser {

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
		return switch (type) {
			// TODO: parse lists
			case BULLETED_LIST -> parseTextProperty(value);
			case COMMA_LIST -> parseTextProperty(value);
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
		// TODO: cleanup wikitext here
		return cleaner.cleanupWikitext(value.trim());
	}
}
