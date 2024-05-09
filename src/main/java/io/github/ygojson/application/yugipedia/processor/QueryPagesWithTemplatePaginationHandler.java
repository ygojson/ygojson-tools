package io.github.ygojson.application.yugipedia.processor;

import retrofit2.Call;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.client.params.Template;
import io.github.ygojson.application.yugipedia.client.response.Continue;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;

/**
 * Handles consequent queries to the {@link YugipediaClient#queryPagesWithTemplate(Template, Limit, String)}.
 */
class QueryPagesWithTemplatePaginationHandler
	extends AbstractYugipediaPaginationHandler<String> {

	private final Template template;
	private final Limit limit;

	QueryPagesWithTemplatePaginationHandler(
		final YugipediaClient api,
		final Template template,
		final Limit limit
	) {
		super(api);
		this.template = template;
		this.limit = limit;
	}

	@Override
	protected String getContinueToken(QueryResponse previous) {
		final Continue previousContinue = previous.getContinue();
		if (previousContinue == null) {
			return null;
		}
		return previousContinue.geicontinue();
	}

	@Override
	protected Call<QueryResponse> callApi(
		YugipediaClient api,
		String continueToken
	) {
		return api.queryPagesWithTemplate(template, limit, continueToken);
	}
}
