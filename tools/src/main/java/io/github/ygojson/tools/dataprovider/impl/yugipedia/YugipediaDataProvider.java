package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.stream.Stream;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.DataProvider;
import io.github.ygojson.tools.dataprovider.PrintData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.FetchDataProcessor;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.SetProcessor;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination.YugipediaStreamFactory;

public class YugipediaDataProvider implements DataProvider {

	private final YugipediaStreamFactory factory;
	private final Limit limit;
	private final int setBatchSize;

	public YugipediaDataProvider(final YugipediaApi yugipediaApi, final Limit limit, final int setBatchSize) {
		this.factory = new YugipediaStreamFactory(yugipediaApi);
		this.limit = limit;
		this.setBatchSize = setBatchSize;
	}

	@Override
	public Stream<PrintData> fetchData() {
		final FetchDataProcessor fetchDataProcessor = new FetchDataProcessor();
		return factory.ofCards(limit)
			.flatMap(fetchDataProcessor::processQuery)
			.filter(Objects::nonNull);
	}

	@Override
	public Stream<PrintData> fetchData(ZonedDateTime lastUpdated) {
		final FetchDataProcessor fetchDataProcessor = new FetchDataProcessor();
		return factory.ofRecentChanges(limit, lastUpdated)
			.flatMap(fetchDataProcessor::processQuery)
			.filter(Objects::nonNull);
	}

	@Override
	public boolean shouldFinalizeSets() {
		return true;
	}

	@Override
	public Stream<Set> finalizeSets(final Stream<Set> data) {
		final SetProcessor setProcessor = new SetProcessor();
		return factory.ofTitles(data.map(Set::getName), setBatchSize)
			.flatMap(setProcessor::processQuery)
			.filter(Objects::nonNull);
	}

}
