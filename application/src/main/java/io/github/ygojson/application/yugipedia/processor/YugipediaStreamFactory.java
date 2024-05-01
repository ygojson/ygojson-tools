package io.github.ygojson.application.yugipedia.processor;

import java.util.stream.Stream;

import io.github.ygojson.application.util.stream.PaginatorStreamFactory;
import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.client.params.Template;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;

/**
 * Stream factory for {@link YugipediaClient} query methods.
 */
public class YugipediaStreamFactory {

	private final YugipediaClient yugipediaClient;

	public YugipediaStreamFactory(final YugipediaClient yugipediaClient) {
		this.yugipediaClient = yugipediaClient;
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
			new QueryPagesWithTemplatePaginationHandler(
				yugipediaClient,
				Template.SETS,
				limit
			)
		);
	}
}
