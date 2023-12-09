package io.github.ygojson.tools.yugipedia.api;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import io.github.ygojson.tools.yugipedia.api.params.Category;
import io.github.ygojson.tools.yugipedia.api.params.PipeSeparated;
import io.github.ygojson.tools.yugipedia.api.params.SortDirection;
import io.github.ygojson.tools.yugipedia.api.response.Continue;
import io.github.ygojson.tools.yugipedia.api.response.QueryResponse;

@Slf4j
class YugipediaApiIT {

	private YugipediaApi api;

	@BeforeEach
	void beforeEach() {
		api = YugipediaApiMother.productionClient();
	}

	private Response<QueryResponse> doExecuteTestQueryCategoryMembersByTimestamp(final String gmcontinue) throws IOException {
		return api.queryCategoryMembersByTimestamp(
			Category.CARDS,
			50,
			SortDirection.NEWER,
			gmcontinue
		).execute();
	}

	@Test
	void testQueryCategoryMembersByTimestamp() throws IOException {
		final Response<QueryResponse> cards = doExecuteTestQueryCategoryMembersByTimestamp(null);
		log.info("Response:\n:{}", cards.body());
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(cards.code()).isEqualTo(200);
			softly.assertThat(cards.body()).isNotNull();
			softly.assertThat(cards.body().getContinueInfo())
				.isNotNull()
				.extracting(Continue::getGcmcontinue)
				.isNotNull();
		});
	}

	@Test
	void testQueryCategoryMembersByTimestampWithGmContinue() throws IOException {
		final Response<QueryResponse> firstResponse = doExecuteTestQueryCategoryMembersByTimestamp(null);
		final String gmcontinue = firstResponse.body().getContinueInfo().getGcmcontinue();
		final Response<QueryResponse> secondCall = doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
		log.info("Response:\n:{}", secondCall.body());
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(secondCall.code()).isEqualTo(200);
			softly.assertThat(secondCall.body()).isNotNull();
			softly.assertThat(secondCall).isNotEqualTo(firstResponse.body());
		});
	}

	@Test
	void testQueryRecentChanges() throws IOException {
		final Response<QueryResponse> recentChanges = api
			.queryRecentChanges(50, null, null)
			.execute();
		log.info("Response:\n:{}", recentChanges.body());
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(recentChanges.code()).isEqualTo(200);
			softly.assertThat(recentChanges.body()).isNotNull();
		});
	}

	@Test
	void testQueryPagesByTitle() throws IOException {
		final Response<QueryResponse> sets = api
			.queryPagesByTitle(new PipeSeparated("LOB", "ETCO"))
			.execute();
		log.info("Response:\n:{}", sets.body());
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(sets.code()).isEqualTo(200);
			softly.assertThat(sets.body()).isNotNull();
		});
	}
}
