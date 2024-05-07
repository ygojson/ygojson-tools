package io.github.ygojson.application.yugipedia.parser;

import java.util.Map;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Interface for Yugipedia parser as a set of properties.
 * <br>
 * Include factory methods for the parsers used in YGOJSON.
 */
public interface YugipediaParser {
	/**
	 * Creates a card-parser.
	 *
	 * @return a YugipediaParser for cards
	 */
	static YugipediaParser createCardParser() {
		return new CardParser(new PropertyParser());
	}

	/**
	 * Creates a card-parser.
	 *
	 * @return a YugipediaParser for sets
	 */
	static YugipediaParser createSetParser() {
		return new SetParser(new PropertyParser());
	}

	/**
	 * Gets the name of the parser (for logging purposes).
	 *
	 * @return the name of the parser
	 */
	public String getName();

	/**
	 * Parse the page wikitext.
	 *
	 * @param title title of the page.
	 * @param pageid ID of the page.
	 * @param wikitext  wikitext of the page.
	 *
	 * @return a map of properties representing the page information to be used on YGOJSON.
	 */
	public Map<String, YugipediaProperty> parse(
		final String title,
		final long pageid,
		final String wikitext
	);
}
