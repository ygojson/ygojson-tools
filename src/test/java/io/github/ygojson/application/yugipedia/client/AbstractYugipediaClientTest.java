package io.github.ygojson.application.yugipedia.client;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.yugipedia.client.params.*;
import io.github.ygojson.application.yugipedia.client.response.Continue;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;

public abstract class AbstractYugipediaClientTest {

	protected abstract YugipediaClient getClient();

	private QueryResponse doExecuteTestQueryCategoryMembersByTimestamp(
		final String grccontinue
	) {
		return getClient()
			.queryCategoryMembersByTimestamp(
				Category.CARDS,
				Limit.getDefault(),
				SortDirection.NEWER,
				grccontinue
			);
	}

	private QueryResponse doExecuteTestQueryRecentChanges(String grccontinue) {
		return doExecuteTestQueryRecentChanges(null, null, grccontinue);
	}

	private QueryResponse doExecuteTestQueryRecentChanges(
		final Timestamp startAt,
		final Timestamp endAt,
		final String grccontinue
	) {
		return getClient()
			.queryRecentChanges(Limit.getDefault(), startAt, endAt, grccontinue);
	}

	private QueryResponse doExecuteQueryPagesByTitle(final PipeSeparated titles) {
		return getClient().queryPagesByTitle(titles);
	}

	@Nested
	@DisplayName("queryCategoryMembersByTimestamp")
	class QueryCategoryMembersByTimestampTest {

		@Test
		void given_firstCall_when_queryCategoryMembersByTimestamp_then_responseOk() {
			// given
			final String gmcontinue = null;
			// when
			final QueryResponse cardsResponse =
				doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
			// then
			assertSoftly(softly -> {
				softly.assertThat(cardsResponse).isNotNull();
				softly.assertThat(cardsResponse.query().pages()).isNotEmpty();
				softly
					.assertThat(cardsResponse.getContinue())
					.isNotNull()
					.extracting(Continue::gcmcontinue)
					.isNotNull();
			});
		}

		@Test
		void given_consecutive3calls_when_queryCategoryMembersByTimestamp_then_responseOk() {
			// given/when
			final QueryResponse firstResponse =
				doExecuteTestQueryCategoryMembersByTimestamp(null);
			String gmcontinue = firstResponse.getContinue().gcmcontinue();
			final QueryResponse secondCall =
				doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
			gmcontinue = secondCall.getContinue().gcmcontinue();
			final QueryResponse thirdCall =
				doExecuteTestQueryCategoryMembersByTimestamp(gmcontinue);
			// then
			assertSoftly(softly -> {
				softly.assertThat(secondCall).isNotNull();
				softly.assertThat(secondCall.query().pages()).isNotEmpty();
				softly.assertThat(secondCall).isNotEqualTo(firstResponse);
				softly.assertThat(thirdCall).isNotNull();
				softly.assertThat(thirdCall).isNotEqualTo(secondCall);
				softly.assertThat(thirdCall.query().pages()).isNotEmpty();
			});
		}
	}

	@Nested
	@DisplayName("queryPagesWithTemplate")
	class QueryPagesWithTemplateTest {

		private QueryResponse doExecuteTestQueryPagesWithTemplate(
			final String geicontinue
		) {
			return getClient()
				.queryPagesWithTemplate(Template.SETS, Limit.getDefault(), geicontinue)
				.await()
				.indefinitely();
		}

		@Test
		void given_firstCall_when_queryPagesWithTemplate_then_responseOk() {
			// given
			final String geicontinue = null;
			// when
			final QueryResponse setsResponse = doExecuteTestQueryPagesWithTemplate(
				geicontinue
			);
			// then
			assertSoftly(softly -> {
				softly.assertThat(setsResponse).isNotNull();
				softly.assertThat(setsResponse.query().pages()).isNotEmpty();
				softly
					.assertThat(setsResponse.getContinue())
					.isNotNull()
					.extracting(Continue::geicontinue)
					.isNotNull();
			});
		}

		@Test
		void given_consecutive3calls_when_queryPagesWithTemplate_then_responseOk() {
			// given/when
			final QueryResponse firstResponse = doExecuteTestQueryPagesWithTemplate(
				null
			);
			String geicontinue = firstResponse.getContinue().geicontinue();
			final QueryResponse secondCall = doExecuteTestQueryPagesWithTemplate(
				geicontinue
			);
			geicontinue = secondCall.getContinue().geicontinue();
			final QueryResponse thirdCall = doExecuteTestQueryPagesWithTemplate(
				geicontinue
			);
			// then
			assertSoftly(softly -> {
				softly.assertThat(secondCall).isNotNull();
				softly.assertThat(secondCall.query().pages()).isNotEmpty();
				softly.assertThat(secondCall).isNotEqualTo(firstResponse);
				softly.assertThat(thirdCall).isNotNull();
				softly.assertThat(thirdCall.query().pages()).isNotEmpty();
				softly.assertThat(thirdCall).isNotEqualTo(secondCall);
			});
		}
	}

	@Nested
	@DisplayName("queryRecentChanges")
	class QueryRecentChangesTest {

		@Test
		void given_firstCall_when_queryRecentChanges_then_responseOk() {
			// given
			final String grccontinue = null;
			// when
			final QueryResponse recentChanges = doExecuteTestQueryRecentChanges(
				grccontinue
			);
			// then
			assertSoftly(softly -> {
				softly.assertThat(recentChanges).isNotNull();
				softly.assertThat(recentChanges.query().pages()).isNotEmpty();
			});
		}

		@Test
		void given_consecutive3calls_when_queryRecentChanges_then_responseOk() {
			// given
			String grccontinue = null;
			// when
			final QueryResponse firstResponse = doExecuteTestQueryRecentChanges(
				grccontinue
			);
			grccontinue = firstResponse.getContinue().grccontinue();
			final QueryResponse secondCall = doExecuteTestQueryRecentChanges(
				grccontinue
			);
			grccontinue = secondCall.getContinue().grccontinue();
			final QueryResponse thirdCall = doExecuteTestQueryRecentChanges(
				grccontinue
			);
			// then
			assertSoftly(softly -> {
				softly.assertThat(secondCall).isNotNull();
				softly.assertThat(secondCall.query().pages()).isNotEmpty();
				softly.assertThat(secondCall).isNotEqualTo(firstResponse);
				softly.assertThat(thirdCall).isNotNull();
				softly.assertThat(thirdCall.query().pages()).isNotEmpty();
				softly.assertThat(thirdCall).isNotEqualTo(secondCall);
			});
		}

		@Test
		void given_callWithStartAtDateTime_when_queryRecentChanges_then_responseOk() {
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
			final QueryResponse recentChanges = doExecuteTestQueryRecentChanges(
				Timestamp.of(startAt),
				null,
				grccontinue
			);
			// then
			assertSoftly(softly -> {
				softly.assertThat(recentChanges).isNotNull();
				softly.assertThat(recentChanges.query().pages()).isNotEmpty();
			});
		}
	}

	@Nested
	@DisplayName("queryPageByTitle")
	class QueryPageByTitleTest {

		@Test
		void given_callWithTitles_when_queryPagesByTitle_then_responseOk() {
			// given
			final PipeSeparated titles = PipeSeparated.of("LOB", "ETCO");
			// when
			final QueryResponse responseTitles = doExecuteQueryPagesByTitle(titles);
			// then
			assertSoftly(softly -> {
				softly.assertThat(responseTitles).isNotNull();
				softly.assertThat(responseTitles.query().pages()).isNotEmpty();
			});
		}

		@Test
		void given_callWithNotExistentTitle_when_queryPagesByTitle_then_responseOk() {
			// given
			final PipeSeparated titles = PipeSeparated.of("Does not exists");
			// when
			final QueryResponse responseTitles = doExecuteQueryPagesByTitle(titles);
			// then
			assertSoftly(softly -> {
				softly.assertThat(responseTitles).isNotNull();
				softly.assertThat(responseTitles.query().pages()).isNotEmpty();
			});
		}

		@Test
		void given_callWithoutTitles_when_queryPagesByTitle_then_responseOk() {
			// given
			final PipeSeparated titles = null;
			// when
			final QueryResponse responseTitles = doExecuteQueryPagesByTitle(titles);
			// then
			assertSoftly(softly -> {
				softly.assertThat(responseTitles).isNotNull();
				softly.assertThat(responseTitles.query()).isNull();
				softly.assertThat(responseTitles.batchcomplete()).isTrue();
			});
		}
	}
}
