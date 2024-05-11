package io.github.ygojson.application.yugipedia;

import java.util.Map;
import java.util.stream.Stream;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.application.yugipedia.processor.YugipediaProcessor;
import io.github.ygojson.application.yugipedia.processor.YugipediaStreamFactory;

/**
 * Yugipedia provider to fetch the Yugipedia model.
 */
public class YugipediaProvider {

	private final YugipediaStreamFactory factory;
	private final Limit limit;

	public YugipediaProvider(final YugipediaClient client, final Limit limit) {
		this.factory = new YugipediaStreamFactory(client);
		this.limit = limit;
	}

	/**
	 * Fetch the sets from Yugipedia as a map of properties.
	 *
	 * @return lazy-loaded stream with the sets.
	 */
	public Stream<Map<String, YugipediaProperty>> fetchSets() {
		final YugipediaProcessor processor =
			YugipediaProcessor.createSetProcessor();
		return factory.ofSets(limit).flatMap(processor::processQuery);
	}
}
