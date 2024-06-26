package io.github.ygojson.application.yugipedia.parser;

import java.util.Map;

import io.github.ygojson.application.yugipedia.parser.model.CustomProperties;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Parser for the card properties.
 */
class CardParser implements YugipediaParser {

	private static final String NAME = "card";

	private static final String NAME_PROPERTY = "name";

	private final CardTable2Parser cardTable2Parser;

	public CardParser(final PropertyParser parser) {
		cardTable2Parser = new CardTable2Parser(parser);
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
			cardTable2Parser.parse(wikitext);
		if (initialTemplateProperties == null) {
			return null;
		}
		// add always the page ID to the properties
		initialTemplateProperties.put(
			CustomProperties.PAGE_ID,
			YugipediaProperty.text(Long.toString(pageid))
		);
		// add the title to the properties if name is not present
		initialTemplateProperties.computeIfAbsent(
			NAME_PROPERTY,
			key -> YugipediaProperty.text(title)
		);
		return initialTemplateProperties;
	}
}
