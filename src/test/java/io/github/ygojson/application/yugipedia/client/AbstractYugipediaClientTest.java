package io.github.ygojson.application.yugipedia.client;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import okhttp3.Request;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;

import io.github.ygojson.application.yugipedia.client.params.*;
import io.github.ygojson.application.yugipedia.client.response.Continue;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;

public abstract class AbstractYugipediaClientTest {

	protected abstract YugipediaClient getClient();

	protected void logRequest(Request request) {
		// NO-OP
	}

	protected abstract void logResponse(final Response<?> response);

	private <T> Response<T> callWithLog(final Call<T> call) throws IOException {
		logRequest(call.request());
		final Response<T> response = call.execute();
		logResponse(response);
		return response;
	}

	private Response<QueryResponse> doExecuteTestQueryCategoryMembersByTimestamp(
		final String grccontinue
	) throws IOException {
		return callWithLog(
			getClient()
				.queryCategoryMembersByTimestamp(
					Category.CARDS,
					Limit.getDefault(),
					SortDirection.NEWER,
					grccontinue
				)
		);
	}

	private Response<QueryResponse> doExecuteTestQueryPagesWithTemplate(
		final String geicontinue
	) throws IOException {
		return callWithLog(
			getClient()
				.queryPagesWithTemplate(Template.SETS, Limit.getDefault(), geicontinue)
		);
	}

	private Response<QueryResponse> doExecuteTestQueryRecentChanges(
		String grccontinue
	) throws IOException {
		return doExecuteTestQueryRecentChanges(null, null, grccontinue);
	}

	public Response<QueryResponse> doExecuteTestQueryRecentChanges(
		final Timestamp startAt,
		final Timestamp endAt,
		final String grccontinue
	) throws IOException {
		return callWithLog(
			getClient()
				.queryRecentChanges(Limit.getDefault(), startAt, endAt, grccontinue)
		);
	}

	public Response<QueryResponse> doExecuteQueryPagesByTitle(
		final PipeSeparated titles
	) throws IOException {
		return callWithLog(getClient().queryPagesByTitle(titles));
	}

	@Test
	void given_firstCall_when_queryCategoryMembersByTimestamp_then_responseOk()
		throws IOException {
		// given
		final String gmcontinue = null;
		// when
		final Response<QueryResponse> cards =
			doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
		// then
		assertSoftly(softly -> {
			softly.assertThat(cards.code()).isEqualTo(200);
			softly.assertThat(cards.body()).isNotNull();
			softly
				.assertThat(cards.body().getContinue())
				.isNotNull()
				.extracting(Continue::gcmcontinue)
				.isNotNull();
		});
	}

	@Test
	void given_consecutive3calls_when_queryCategoryMembersByTimestamp_then_responseOk()
		throws IOException {
		// given/when
		final Response<QueryResponse> firstResponse =
			doExecuteTestQueryCategoryMembersByTimestamp(null);
		String gmcontinue = firstResponse.body().getContinue().gcmcontinue();
		final Response<QueryResponse> secondCall =
			doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
		gmcontinue = secondCall.body().getContinue().gcmcontinue();
		final Response<QueryResponse> thirdCall =
			doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
		// then
		assertSoftly(softly -> {
			softly.assertThat(secondCall.code()).isEqualTo(200);
			softly.assertThat(secondCall.body()).isNotNull();
			softly.assertThat(secondCall).isNotEqualTo(firstResponse.body());
			softly.assertThat(thirdCall.code()).isEqualTo(200);
			softly.assertThat(thirdCall.body()).isNotNull();
			softly.assertThat(thirdCall).isNotEqualTo(secondCall.body());
		});
	}

	@Test
	void given_firstCall_when_queryPagesWithTemplate_then_responseOk()
		throws IOException {
		// given
		final String geicontinue = null;
		// when
		final Response<QueryResponse> sets = doExecuteTestQueryPagesWithTemplate(
			geicontinue
		);
		// then
		assertSoftly(softly -> {
			softly.assertThat(sets.code()).isEqualTo(200);
			softly.assertThat(sets.body()).isNotNull();
			softly
				.assertThat(sets.body().getContinue())
				.isNotNull()
				.extracting(Continue::geicontinue)
				.isNotNull();
		});
	}

	@Test
	void given_consecutive3calls_when_queryPagesWithTemplate_then_responseOk()
		throws IOException {
		// given/when
		final Response<QueryResponse> firstResponse =
			doExecuteTestQueryPagesWithTemplate(null);
		String geicontinue = firstResponse.body().getContinue().geicontinue();
		final Response<QueryResponse> secondCall =
			doExecuteTestQueryPagesWithTemplate(geicontinue);
		geicontinue = secondCall.body().getContinue().geicontinue();
		final Response<QueryResponse> thirdCall =
			doExecuteTestQueryPagesWithTemplate(geicontinue);
		// then
		assertSoftly(softly -> {
			softly.assertThat(secondCall.code()).isEqualTo(200);
			softly.assertThat(secondCall.body()).isNotNull();
			softly.assertThat(secondCall).isNotEqualTo(firstResponse.body());
			softly.assertThat(thirdCall.code()).isEqualTo(200);
			softly.assertThat(thirdCall.body()).isNotNull();
			softly.assertThat(thirdCall).isNotEqualTo(secondCall.body());
		});
	}

	@Test
	void given_firstCall_when_queryRecentChanges_then_responseOk()
		throws IOException {
		// given
		final String grccontinue = null;
		// when
		final Response<QueryResponse> recentChanges =
			doExecuteTestQueryRecentChanges(grccontinue);
		// then
		assertSoftly(softly -> {
			softly.assertThat(recentChanges.code()).isEqualTo(200);
			softly.assertThat(recentChanges.body()).isNotNull();
		});
	}

	@Test
	void given_consecutive3calls_when_queryRecentChanges_then_responseOk()
		throws IOException {
		// given
		String grccontinue = null;
		// when
		final Response<QueryResponse> firstResponse =
			doExecuteTestQueryRecentChanges(grccontinue);
		grccontinue = firstResponse.body().getContinue().grccontinue();
		final Response<QueryResponse> secondCall = doExecuteTestQueryRecentChanges(
			grccontinue
		);
		grccontinue = secondCall.body().getContinue().grccontinue();
		final Response<QueryResponse> thirdCall = doExecuteTestQueryRecentChanges(
			grccontinue
		);
		// then
		assertSoftly(softly -> {
			softly.assertThat(secondCall.code()).isEqualTo(200);
			softly.assertThat(secondCall.body()).isNotNull();
			softly.assertThat(secondCall).isNotEqualTo(firstResponse.body());
			softly.assertThat(thirdCall.code()).isEqualTo(200);
			softly.assertThat(thirdCall.body()).isNotNull();
			softly.assertThat(thirdCall).isNotEqualTo(secondCall.body());
		});
	}

	@Test
	void given_callWithStartAtDateTime_when_queryRecentChanges_then_responseOk()
		throws IOException {
		// given
		final ZonedDateTime startAt = ZonedDateTime.of(
			2024,
			4,
			1,
			0,
			0,
			0,
			0,
			ZoneOffset.UTC
		);
		final String grccontinue = null;
		// when
		final Response<QueryResponse> recentChanges =
			doExecuteTestQueryRecentChanges(Timestamp.of(startAt), null, grccontinue);
		// then
		assertSoftly(softly -> {
			softly.assertThat(recentChanges.code()).isEqualTo(200);
			softly.assertThat(recentChanges.body()).isNotNull();
		});
	}

	@Test
	void given_callWithTitles_when_queryPagesByTitle_then_responseOk()
		throws IOException {
		// given
		final PipeSeparated titles = PipeSeparated.of("LOB", "ETCO");
		// when
		final Response<QueryResponse> sets = doExecuteQueryPagesByTitle(titles);
		// then
		assertSoftly(softly -> {
			softly.assertThat(sets.code()).isEqualTo(200);
			softly.assertThat(sets.body()).isNotNull();
		});
	}

	@Test
	void given_callWithNotExistentTitle_when_queryPagesByTitle_then_responseOk()
		throws IOException {
		// given
		final PipeSeparated titles = PipeSeparated.of("Does not exists");
		// when
		final Response<QueryResponse> sets = doExecuteQueryPagesByTitle(titles);
		// then
		assertSoftly(softly -> {
			softly.assertThat(sets.code()).isEqualTo(200);
			softly.assertThat(sets.body()).isNotNull();
		});
	}

	@Test
	void given_callWithoutTitles_when_queryPagesByTitle_then_responseOk()
		throws IOException {
		// given
		final PipeSeparated titles = null;
		// when
		final Response<QueryResponse> sets = doExecuteQueryPagesByTitle(titles);
		// then
		assertSoftly(softly -> {
			softly.assertThat(sets.code()).isEqualTo(200);
			softly.assertThat(sets.body()).isNotNull();
			softly.assertThat(sets.body().batchcomplete()).isTrue();
		});
	}
}
