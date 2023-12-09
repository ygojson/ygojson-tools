package io.github.ygojson.tools.initializer.impl;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import io.github.ygojson.tools.yugipedia.api.YugipediaApi;
import io.github.ygojson.tools.yugipedia.api.YugipediaApiMother;
import io.github.ygojson.tools.yugipedia.api.params.Category;
import io.github.ygojson.tools.yugipedia.api.params.PipeSeparated;
import io.github.ygojson.tools.yugipedia.api.params.SortDirection;
import io.github.ygojson.tools.yugipedia.api.response.QueryResponse;

@Slf4j
class YugipediaApiIT {

	private YugipediaApi api;

	@BeforeEach
	void beforeEach() {
		api = YugipediaApiMother.productionClient();
	}

	@Test
	void testQueryCategoryMembersByTimestamp() throws IOException {
		final Response<QueryResponse> cards = api
			.queryCategoryMembersByTimestamp(
				Category.CARDS,
				50,
				SortDirection.NEWER,
				null
			)
			.execute();
		log.info("Response:\n:{}", cards);
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(cards.code()).isEqualTo(200);
			softly.assertThat(cards.body()).isNotNull();
		});
	}

	@Test
	void testQueryRecentChanges() throws IOException {
		final Response<QueryResponse> recentChanges = api
			.queryRecentChanges(50, null, null)
			.execute();
		log.info("Response:\n{}", recentChanges.body());
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
		log.info("Response:\n{}", sets.body());
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(sets.code()).isEqualTo(200);
			softly.assertThat(sets.body()).isNotNull();
		});
	}
}
