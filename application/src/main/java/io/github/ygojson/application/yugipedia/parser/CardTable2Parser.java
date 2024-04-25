package io.github.ygojson.application.yugipedia.parser;

import java.util.regex.Pattern;

/**
 * Parser for the Yugipedia's CardTable2 Template.
 *
 * @see <a href=https://yugipedia.com/wiki/Template:CardTable2/doc>CardTable2</a>
 */
class CardTable2Parser extends TemplateParser {

	// Pattern for the CardTable2 content
	private static final Pattern CARDTABLE2_CONTENT_PATTERN = Pattern.compile(
		"(?s).*\\{\\{CardTable2(.*)\n}}"
	);

	// Group on the CARDTABLE2_CONTENT_PATTERN where the actual content is
	private static final int CARDTABLE2_CONTENT_GROUP = 1;

	/**
	 * Default constructor.
	 *
	 * @param parser parser to use.
	 */
	protected CardTable2Parser(PropertyParser parser) {
		super(parser, CARDTABLE2_CONTENT_PATTERN, CARDTABLE2_CONTENT_GROUP);
	}

	@Override
	protected boolean ignoreProperty(String property) {
		// TODO: ignore some properties
		return false;
	}

	@Override
	protected PropertyParser.Type getPropertyType(String property) {
		// TODO: handle some special properties
		return PropertyParser.Type.TEXT;
	}
}
