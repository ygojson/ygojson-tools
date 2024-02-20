package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import java.util.Objects;
import java.util.stream.Stream;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.DataProvider;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.SetProcessor;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination.YugipediaStreamFactory;

/**
 * {@link DataProvider} implementation for Yugipedia.
 */
public class YugipediaDataProvider implements DataProvider {

	private final YugipediaStreamFactory factory;
	private final Limit limit;

	private final SetProcessor setProcessor = new SetProcessor();

	/**
	 * Constructor for the given API instance and limit.
	 *
	 * @param yugipediaApi the API instance to use
	 * @param limit limit for the queries
	 */
	public YugipediaDataProvider(
		final YugipediaApi yugipediaApi,
		final Limit limit
	) {
		this.factory = new YugipediaStreamFactory(yugipediaApi);
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
