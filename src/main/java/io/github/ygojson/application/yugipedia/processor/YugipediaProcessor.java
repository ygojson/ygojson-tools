package io.github.ygojson.application.yugipedia.processor;

import java.util.Map;

import io.smallrye.mutiny.Multi;

import io.github.ygojson.application.yugipedia.client.response.QueryResponse;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

public interface YugipediaProcessor {
	/**
	 * Creates a card-processor.
	 *
	 * @return a YugipediaProcessor for cards
	 */
	static YugipediaProcessor createCardProcessor() {
		return new YugipediaParserProcessor(YugipediaParser.createCardParser());
	}

	/**
	 * Creates a card-processor.
	 *
	 * @return a YugipediaProcessor for sets
	 */
	static YugipediaProcessor createSetProcessor() {
		return new YugipediaParserProcessor(YugipediaParser.createSetParser());
	}

	/**
	 * Process the query in asyncronious way.
	 *
	 * @param queryResponse parse the query in async way.
	 * @return the result as a multi.
	 */
	Multi<Map<String, YugipediaProperty>> processQuery(
		QueryResponse queryResponse
	);
}
