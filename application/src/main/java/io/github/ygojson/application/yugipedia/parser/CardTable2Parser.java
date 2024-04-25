package io.github.ygojson.application.yugipedia.parser;

import java.util.List;
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

	// IGNORE VIDEO GAME PROPERTIES AS THEY ARE DEPRECATED
	// WE CANNOT RELY ON THEM DUE TO THAT INFORMATION
	// TODO: maybe contribute to yugipedia to remove for pages where we find them
	private static List<Pattern> IGNORED_PATTERNS = List.of(
		Pattern.compile("^dar_.*"),
		Pattern.compile("^ntr_.*"),
		Pattern.compile("^ygo_.*"),
		Pattern.compile("^gx[0-9]+_.*")
	);

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
		return IGNORED_PATTERNS
			.stream()
			.map(pattern -> pattern.matcher(property).find())
			.filter(val -> val)
			.findAny()
			.orElse(false);
	}

	@Override
	protected PropertyParser.Type getPropertyType(String property) {
		// TODO: handle some special properties
		return PropertyParser.Type.TEXT;
	}
}
