package io.github.ygojson.application.yugipedia.parser;

import java.util.regex.Pattern;

/**
 * Parser for the Yugipedia's Infobox Set Template.
 *
 * @see <a href=https://yugipedia.com/wiki/Template:Infobox_set>Infobox Set</a>
 */
class InfoboxSetParser extends TemplateParser {

	/**
	 * Pattern for the InfoBoxSet content
	 */
	private static final Pattern INFOBOX_SET_CONTENT_PATTERN = Pattern.compile(
		"(?s).*\\{\\{Infobox set(.*?)\n}}"
	);

	/**
	 * Group on the {@link #INFOBOX_SET_CONTENT_PATTERN} where the content is.
	 */
	private static final int INFOBOX_SET_CONTENT_GROUP = 1;

	protected InfoboxSetParser(PropertyParser parser) {
		super(parser, INFOBOX_SET_CONTENT_PATTERN, INFOBOX_SET_CONTENT_GROUP);
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
