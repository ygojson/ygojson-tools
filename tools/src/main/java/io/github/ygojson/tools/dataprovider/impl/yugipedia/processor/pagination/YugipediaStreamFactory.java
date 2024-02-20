package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination;

import java.util.stream.Stream;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Template;
import io.github.ygojson.tools.dataprovider.utils.PaginatorStreamFactory;

/**
 * Stream factory for {@link YugipediaApi} query methods.
 */
public class YugipediaStreamFactory {

	private final YugipediaApi yugipediaApi;

	public YugipediaStreamFactory(final YugipediaApi yugipediaApi) {
		this.yugipediaApi = yugipediaApi;
	}

	/**
	 * Creates the stream for set fetching.
	 *
	 * @param limit the limit for fetching the sets.
	 *
	 * @return the stream for set fetching.
	 */
	public Stream<QueryResponse> ofSets(final Limit limit) {
		return PaginatorStreamFactory.create(
			new TemplatePageHandler(yugipediaApi, Template.SETS, limit)
		);
	}
}
