package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination;

import java.io.IOException;
import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Response;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.utils.PaginatorStreamFactory;

/**
 * Page handler for Yugipedia {@link QueryResponse}.
 *
 * @param <T> type of the token to continue iteration
 */
abstract class AbstractYugipediaQueryPageHandler<T> implements PaginatorStreamFactory.PageHandler<QueryResponse> {

	private final YugipediaApi api;

    AbstractYugipediaQueryPageHandler(final YugipediaApi api) {
        this.api = api;
    }


    protected abstract T getContinueToken(final QueryResponse previous);
	protected abstract Call<QueryResponse> callApi(final YugipediaApi api, final T continueToken);

	@Override
	public final QueryResponse getNext(final QueryResponse previous) {
		if (previous == null) {
			return doQuery(null);
		}
		final T continueToken = getContinueToken(previous);
		return continueToken == null ? null : doQuery(continueToken);
	}

	private QueryResponse doQuery(final T continueToken) {
		try {
			final Response<QueryResponse> response = callApi(api, continueToken).execute();
			if (!response.isSuccessful()) {
				// TODO: should create a custom exception
				throw new RuntimeException(MessageFormat.format(
					"Yugipedia error {0}: {1}",
					response.code(),
					response.errorBody()
				));
			}
			return response.body();
		} catch (final IOException e) {
			// TODO: should create a custom exception
			throw new RuntimeException("Unexpected error on yugipedia API", e);
		}
	}

	@Override
	public final void handleError(Throwable e) {
		PaginatorStreamFactory.PageHandler.super.handleError(e);
		// TODO: should create a custom exception and handle also here
		throw new RuntimeException("Unexpected error on yugipedia API", e);
	}
}
