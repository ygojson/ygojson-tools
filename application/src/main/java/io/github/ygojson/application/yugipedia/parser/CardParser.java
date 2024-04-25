package io.github.ygojson.application.yugipedia.parser;

import java.util.Map;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Parser for the card properties.
 */
class CardParser implements YugipediaParser {

	private final CardTable2Parser cardTable2Parser;

	public CardParser(final PropertyParser parser) {
		cardTable2Parser = new CardTable2Parser(parser);
	}

	@Override
	public Map<String, YugipediaProperty> parse(
		final String title,
		final long pageid,
		final String wikitext
	) {
		final Map<String, YugipediaProperty> initialTemplateProperties =
			cardTable2Parser.parse(wikitext);
		// TODO: handle other properties (tile=name, pageid, etc)
		return initialTemplateProperties;
	}
}
