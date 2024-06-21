package io.github.ygojson.application.yugipedia.processor;

import java.util.Map;
import java.util.stream.Stream;

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
	 *
	 * @param queryResponse
	 * @return
	 *
	 * @deprecated use the reactrive processor instead ({@link #processQueryReactive(QueryResponse)}).
	 */
	@Deprecated
	Stream<Map<String, YugipediaProperty>> processQuery(
		QueryResponse queryResponse
	);

	default Multi<Map<String, YugipediaProperty>> processQueryReactive(
		QueryResponse queryResponse
	) {
		return Multi.createFrom().items(processQuery(queryResponse));
	}
}
