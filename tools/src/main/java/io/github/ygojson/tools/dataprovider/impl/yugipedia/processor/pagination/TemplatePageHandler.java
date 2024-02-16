package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination;

import retrofit2.Call;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Continue;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Template;

public class TemplatePageHandler extends AbstractYugipediaQueryPageHandler<String> {

	private final Template template;
	private final Limit limit;

	TemplatePageHandler(final YugipediaApi api, final Template template, final Limit limit) {
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
	protected Call<QueryResponse> callApi(YugipediaApi api, String continueToken) {
		return api.queryPagesWithTemplate(template, limit, continueToken);
	}

}
