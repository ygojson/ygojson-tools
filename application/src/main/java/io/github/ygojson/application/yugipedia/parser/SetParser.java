package io.github.ygojson.application.yugipedia.parser;

import java.util.Map;

import io.github.ygojson.application.yugipedia.parser.model.CustomProperties;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Parser for the set properties.
 */
class SetParser implements YugipediaParser {

	private static final String NAME = "set";

	private static final String NAME_PROPERTY = "en_name";

	private final InfoboxSetParser infoboxSetParser;

	public SetParser(final PropertyParser parser) {
		infoboxSetParser = new InfoboxSetParser(parser);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Map<String, YugipediaProperty> parse(
		final String title,
		final long pageid,
		final String wikitext
	) {
		if (wikitext == null) {
			return null;
		}
		final Map<String, YugipediaProperty> initialTemplateProperties =
			infoboxSetParser.parse(wikitext);
		if (initialTemplateProperties == null) {
			return null;
		}
		initialTemplateProperties.put(
			CustomProperties.PAGE_ID,
			YugipediaProperty.text(Long.toString(pageid))
		);
		initialTemplateProperties.computeIfAbsent(
			NAME_PROPERTY,
			key -> YugipediaProperty.text(title)
		);
		return initialTemplateProperties;
	}
}
