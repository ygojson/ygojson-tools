package io.github.ygojson.application.yugipedia.processor;

import java.io.IOException;
import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Response;

import io.github.ygojson.application.util.stream.PaginatorStreamFactory;
import io.github.ygojson.application.yugipedia.YugipediaException;
import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;

/**
 * Abstract page handler for Yugipedia {@link QueryResponse}.
 *
 * @param <T> type of the token to continue iteration
 */
abstract class AbstractYugipediaPaginationHandler<T>
	implements PaginatorStreamFactory.PageHandler<QueryResponse> {

	private final YugipediaClient api;

	AbstractYugipediaPaginationHandler(final YugipediaClient api) {
		this.api = api;
	}

	/**
	 * Implements the current getter for the {@link QueryResponse} continue token to be used.
	 *
	 * @param previous the previous query response
	 * @return the continue token; {@code null} if there is no next page.
	 */
	protected abstract T getContinueToken(final QueryResponse previous);

	/**
	 * Calls the Yugipedia API with the given {@link T} continue token.
	 *
	 * @param api the API
	 * @param continueToken the continue token; {@code null} for first call.
	 *
	 * @return the query response call object
	 */
	protected abstract Call<QueryResponse> callApi(
		final YugipediaClient api,
		final T continueToken
	);

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
			final Response<QueryResponse> response = callApi(api, continueToken)
				.execute();
			if (!response.isSuccessful()) {
				throw new YugipediaException(
					MessageFormat.format(
						"Yugipedia error {0}: {1}",
						response.code(),
						response.errorBody()
					)
				);
			}
			return response.body();
		} catch (final IOException e) {
			throw new YugipediaException(
				"Unexpected error on Yugipedia API query",
				e
			);
		}
	}

	@Override
	public final void handleError(Throwable e) {
		PaginatorStreamFactory.PageHandler.super.handleError(e);
		if (e instanceof YugipediaException) {
			throw (YugipediaException) e;
		}
		throw new YugipediaException("Unexpected error", e);
	}
}
