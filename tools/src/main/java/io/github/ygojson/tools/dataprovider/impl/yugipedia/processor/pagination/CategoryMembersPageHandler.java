package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination;

import retrofit2.Call;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Continue;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Category;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.SortDirection;

/**
 * Page handler for the {@link YugipediaApi#queryCategoryMembersByTimestamp(Category, Limit, SortDirection, String)}
 * API call.
 */
class CategoryMembersPageHandler extends AbstractYugipediaQueryPageHandler<String> {

	private final Category category;
	private final Limit limit;
	private final SortDirection direction;

	 CategoryMembersPageHandler(final YugipediaApi api,
									   final Category category,
									   final Limit limit,
									   final SortDirection direction) {
		super(api);
		this.category = category;
		this.limit = limit;
		this.direction = direction;
	}

	@Override
	protected String getContinueToken(final QueryResponse previous) {
		final Continue previousContinue = previous.getContinue();
		if (previousContinue == null) {
			return null;
		}
		return previousContinue.gcmcontinue();
	}

	@Override
	protected Call<QueryResponse> callApi(final YugipediaApi api, final String continueToken) {
		return api.queryCategoryMembersByTimestamp(category, limit, direction, continueToken);
	}
}
