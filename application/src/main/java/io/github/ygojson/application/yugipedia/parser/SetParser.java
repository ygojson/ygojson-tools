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
		final Map<String, YugipediaProperty> initialTemplateProperties =
			infoboxSetParser.parse(wikitext);
		initialTemplateProperties.put(
			CustomProperties.PAGE_ID,
			YugipediaProperty.number(pageid)
		);
		initialTemplateProperties.computeIfAbsent(
			NAME_PROPERTY,
			key -> YugipediaProperty.text(title)
		);
		return initialTemplateProperties;
	}
}
