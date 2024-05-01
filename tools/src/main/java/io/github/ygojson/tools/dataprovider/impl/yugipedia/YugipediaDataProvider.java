package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import java.util.Objects;
import java.util.stream.Stream;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.processor.YugipediaStreamFactory;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.DataProvider;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.SetProcessor;

/**
 * {@link DataProvider} implementation for Yugipedia.
 *
 * @deprecated use {@link io.github.ygojson.application.yugipedia.YugipediaProvider} instead (combined with business-logic mappers)
 */
@Deprecated
public class YugipediaDataProvider implements DataProvider {

	private final YugipediaStreamFactory factory;
	private final Limit limit;

	private final SetProcessor setProcessor = new SetProcessor();

	/**
	 * Constructor for the given API instance and limit.
	 *
	 * @param yugipediaClient the API instance to use
	 * @param limit limit for the queries
	 */
	public YugipediaDataProvider(
		final YugipediaClient yugipediaClient,
		final Limit limit
	) {
		this.factory = new YugipediaStreamFactory(yugipediaClient);
		this.limit = limit;
	}

	@Override
	public Stream<Set> fetchSets() {
		return factory
			.ofSets(limit)
			.flatMap(setProcessor::processQuery)
			.filter(Objects::nonNull);
	}
}
