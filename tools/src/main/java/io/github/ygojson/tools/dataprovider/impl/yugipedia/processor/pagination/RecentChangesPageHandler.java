package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination;

import java.time.ZonedDateTime;

import retrofit2.Call;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Continue;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Timestamp;

/**
 * Page handler for the {@link YugipediaApi#queryRecentChanges(Limit, Timestamp, Timestamp, String)} API call.
 */
class RecentChangesPageHandler extends AbstractYugipediaQueryPageHandler<String> {

	private final Limit limit;
	private final Timestamp startAt;
	private final Timestamp endAt;

	RecentChangesPageHandler(final YugipediaApi api, final Limit limit, final ZonedDateTime startAt, final ZonedDateTime endAt) {
		super(api);
		this.limit = limit;
		this.startAt = startAt == null ? null : Timestamp.of(startAt);
		this.endAt = endAt == null ? null : Timestamp.of(endAt);
	}
	@Override
	protected String getContinueToken(QueryResponse previous) {
		final Continue previousContinue = previous.getContinue();
		if (previousContinue == null) {
			return null;
		}
		return previousContinue.grccontinue();
	}

	@Override
	protected Call<QueryResponse> callApi(YugipediaApi api, String continueToken) {
		return api.queryRecentChanges(limit, startAt, endAt, continueToken);
	}
}
