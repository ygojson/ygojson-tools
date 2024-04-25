package io.github.ygojson.application.yugipedia.parser;

import java.util.List;
import java.util.Set;
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

	private static final Set<String> COMMA_LIST_PROPERTIES = Set.of(
		"link_arrows",
		"effect_types"
	);

	private static final Set<String> BULLETED_LIST_PROPERTIES = Set.of(
		"archseries",
		"supports_archetypes",
		"anti-supports_archetypes",
		"related_to_archseries",
		"supports",
		"anti-supports",
		"action",
		"attack",
		"banished",
		"counter",
		"fm_for",
		"sm_for",
		"life_points",
		"m/s/t",
		"stat_change",
		"summoning",
		"misc"
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
	protected PropertyParser.Type getPropertyType(final String property) {
		if (COMMA_LIST_PROPERTIES.contains(property)) {
			return PropertyParser.Type.COMMA_LIST;
		}
		if (BULLETED_LIST_PROPERTIES.contains(property)) {
			return PropertyParser.Type.BULLETED_LIST;
		}
		// TODO: handle some special properties
		return PropertyParser.Type.TEXT;
	}
}
