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

	PropertyParser() {
		// TODO: add some dependencies
	}

	YugipediaProperty parse(final Type type, String value) {
		// TODO: handle types
		return new YugipediaProperty.TextProp(value.trim());
	}
}
