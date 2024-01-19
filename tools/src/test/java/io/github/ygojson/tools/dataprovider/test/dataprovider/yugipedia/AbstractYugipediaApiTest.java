package io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia;

import java.io.IOException;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Continue;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Category;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.PipeSeparated;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.SortDirection;

public abstract class AbstractYugipediaApiTest {

	protected abstract YugipediaApi getApi();

	private Response<QueryResponse> doExecuteTestQueryCategoryMembersByTimestamp(
		final String gmcontinue
	) throws IOException {
		return getApi()
			.queryCategoryMembersByTimestamp(
				Category.CARDS,
				Limit.getDefault(),
				SortDirection.NEWER,
				gmcontinue
			)
			.execute();
	}

	@Test
	void testQueryCategoryMembersByTimestamp() throws IOException {
		final Response<QueryResponse> cards =
			doExecuteTestQueryCategoryMembersByTimestamp(null);
		SoftAssertions.assertSoftly(softly -> {
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
	void testQueryCategoryMembersByTimestampWithGmContinue() throws IOException {
		final Response<QueryResponse> firstResponse =
			doExecuteTestQueryCategoryMembersByTimestamp(null);
		String gmcontinue = firstResponse.body().getContinue().gcmcontinue();
		final Response<QueryResponse> secondCall =
			doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
		gmcontinue = secondCall.body().getContinue().gcmcontinue();
		final Response<QueryResponse> thirdCall =
			doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(secondCall.code()).isEqualTo(200);
			softly.assertThat(secondCall.body()).isNotNull();
			softly.assertThat(secondCall).isNotEqualTo(firstResponse.body());
			softly.assertThat(thirdCall.code()).isEqualTo(200);
			softly.assertThat(thirdCall.body()).isNotNull();
			softly.assertThat(thirdCall).isNotEqualTo(secondCall.body());
		});
	}

	@Test
	void testQueryRecentChanges() throws IOException {
		final Response<QueryResponse> recentChanges = getApi()
			.queryRecentChanges(Limit.getDefault(), null, null)
			.execute();
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(recentChanges.code()).isEqualTo(200);
			softly.assertThat(recentChanges.body()).isNotNull();
		});
	}

	@Test
	void testQueryPagesByTitle() throws IOException {
		final Response<QueryResponse> sets = getApi()
			.queryPagesByTitle(PipeSeparated.of("LOB", "ETCO"))
			.execute();
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(sets.code()).isEqualTo(200);
			softly.assertThat(sets.body()).isNotNull();
		});
	}
}
