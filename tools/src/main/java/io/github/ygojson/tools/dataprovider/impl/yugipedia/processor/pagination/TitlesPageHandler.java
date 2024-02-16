package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.PipeSeparated;

/**
 * Page handler for the {@link YugipediaApi#queryPagesByTitle(PipeSeparated)} API call.
 */
class TitlesPageHandler extends AbstractYugipediaQueryPageHandler<Boolean> {

	private final Iterator<String> titlesIterator;
	private final int batchSize;

	TitlesPageHandler(final YugipediaApi api, final Iterator<String> titlesIterator, final int batchSize) {
		super(api);
		this.titlesIterator = titlesIterator;
		this.batchSize = batchSize;
	}

	@Override
	protected Boolean getContinueToken(final QueryResponse previousContinue) {
		if (titlesIterator.hasNext()) {
			return true;
		}
		// over
		return null;
	}

	@Override
	protected Call<QueryResponse> callApi(final YugipediaApi api, final Boolean continueToken) {
		final List<String> titles = new ArrayList<>(batchSize);
		while (titlesIterator.hasNext()) {
			if (titles.size() == batchSize) {
				break;
			}
			titles.add(titlesIterator.next());
		}
		final String[] nextTitles = titles.toArray(String[]::new);
		return api.queryPagesByTitle(PipeSeparated.of(nextTitles));
	}
}
