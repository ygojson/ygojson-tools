package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Category;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.SortDirection;
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
	 * Creates the stream for card fetching.
	 *
	 * @param limit limit for the card fetching query.
	 *
	 * @return the stream with the provided params.
	 */
	public Stream<QueryResponse> ofCards(final Limit limit) {
		return PaginatorStreamFactory.create(new CategoryMembersPageHandler(yugipediaApi, Category.CARDS, limit, SortDirection.NEWER));
	}

	public Stream<QueryResponse> ofSets(final Limit limit) {
		return PaginatorStreamFactory.create(new TemplatePageHandler(yugipediaApi, Template.SETS, limit));
	}

	/**
	 * Creates the stream for title fetching.
	 *
	 * @param titles the titles to fetch
	 * @param batchSize the batch size
	 *
	 * @return the stream with the provided params
	 */
	public Stream<QueryResponse> ofTitles(final Stream<String> titles, final int batchSize) {
		return PaginatorStreamFactory.create(new TitlesPageHandler(yugipediaApi, titles.iterator(), batchSize));
	}

	public Stream<QueryResponse> ofRecentChanges(final Limit limit, final ZonedDateTime endAt) {
		return PaginatorStreamFactory.create(new RecentChangesPageHandler(yugipediaApi, limit, null, endAt));
	}
}
