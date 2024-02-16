package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import java.util.Objects;
import java.util.stream.Stream;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.InitialDataEntry;
import io.github.ygojson.tools.dataprovider.InitialDataProvider;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.InitialDataProcessor;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.SetProcessor;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination.YugipediaStreamFactory;

public class YugipediaInitialDataProvider implements InitialDataProvider {

	private final YugipediaStreamFactory factory;
	private final Limit limit;
	private final int setBatchSize;

	public YugipediaInitialDataProvider(final YugipediaApi yugipediaApi, final Limit limit, final int setBatchSize) {
		this.factory = new YugipediaStreamFactory(yugipediaApi);
		this.limit = limit;
		this.setBatchSize = setBatchSize;
	}

	@Override
	public Stream<InitialDataEntry> fetchInitialData() {
		final InitialDataProcessor initialDataProcessor = new InitialDataProcessor();
		return factory.ofCards(limit)
			.flatMap(initialDataProcessor::processQuery)
			.filter(Objects::nonNull);
	}

	@Override
	public boolean requiresSetFetch() {
		return true;
	}

	@Override
	public Stream<Set> fetchSets(final Stream<Set> sets) {
		final SetProcessor setProcessor = new SetProcessor();
		return factory.ofTitles(sets.map(Set::getName), setBatchSize)
			.flatMap(setProcessor::processQuery)
			.filter(Objects::nonNull);
	}
}
