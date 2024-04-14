package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor;

import static io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.YugipediaApiModelsMother.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.yugipedia.client.response.Page;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;
import io.github.ygojson.application.yugipedia.client.response.Revision;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaException;

class QueryResponseWikitextProcessorUnitTest {

	/**
	 * Test processor that returns {@code true} if it has content; otherwise {@code false}.
	 */
	private static class TestProcessor
		extends QueryResponseWikitextProcessor<Boolean> {

		private final Predicate<Page> testFilter;

		TestProcessor() {
			this(page -> false);
		}

		TestProcessor(final Predicate<Page> testFilter) {
			this.testFilter = testFilter;
		}

		@Override
		protected String getModelName() {
			return "test";
		}

		@Override
		protected boolean filterPage(final Page page) {
			return testFilter.test(page);
		}

		@Override
		protected Stream<Boolean> processWikitext(
			long pageId,
			String title,
			String wikitextContent
		) {
			return Stream.of(wikitextContent != null);
		}
	}

	private static class TestThrowingYugipediaExceptionProcessor
		extends TestProcessor {

		@Override
		protected Stream<Boolean> processWikitext(
			long pageId,
			String title,
			String wikitextContent
		) {
			throw new YugipediaException("test");
		}
	}

	@Test
	void given_queryResponseWithNullPages_when_processQuery_then_returnEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse((List<Page>) null);
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithEmptyPages_when_processQuery_then_returnEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse(List.of());
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithPageMarkedAsMissing_when_processQuery_then_returnNull() {
		// given
		final QueryResponse queryResponse = queryResponse(missingPage());
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithRandom_when_processQuery_then_isEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse(randomPage());
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithNullRevisions_when_processWikitext_then_isEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse(
			pageWithRevision((List<Revision>) null)
		);
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithEmptyRevisions_when_processWikitext_then_isEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse(
			pageWithRevision(List.of())
		);
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithNonWikitextRevision_when_processWikitext_then_isEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse(
			pageWithRevision(List.of())
		);
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithRandomRevision_when_processWikitext_then_isEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse(
			pageWithRevision(randomRevision())
		);
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_throwingYugipediaExceptionProcessor_when_processValindWikitext_then_returnsEmpty() {
		// given
		final TestThrowingYugipediaExceptionProcessor throwingProcessor =
			new TestThrowingYugipediaExceptionProcessor();
		// when
		Stream<Boolean> result = throwingProcessor.processQuery(
			queryResponse(pageWithRevision(wikitextRevision("Correct")))
		);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithNullWikitextRevision_when_processWikitext_then_returnsCorrectValue() {
		// given
		final QueryResponse queryResponse = queryResponse(
			pageWithRevision(wikitextRevision(null))
		);
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result)
			.describedAs("result-stream")
			.singleElement()
			.describedAs("result-value")
			.isEqualTo(false);
	}

	@Test
	void given_queryResponseWithWikitextRevision_when_processWikitext_then_returnsCorrectValue() {
		// given
		final QueryResponse queryResponse = queryResponse(
			pageWithRevision(wikitextRevision("my revision content"))
		);
		// when
		Stream<Boolean> result = new TestProcessor().processQuery(queryResponse);
		// then
		assertThat(result)
			.describedAs("result-stream")
			.singleElement()
			.describedAs("result-value")
			.isEqualTo(true);
	}

	@Test
	void given_queryResponseWithFilteredPage_when_processWikitext_then_returnsEmpty() {
		// given
		final Page filteredPage = pageWithRevision(
			wikitextRevision("my revision content")
		);
		final QueryResponse queryResponse = queryResponse(filteredPage);
		// when
		Stream<Boolean> result = new TestProcessor(page -> page == filteredPage)
			.processQuery(queryResponse);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithNotFilteredPage_when_processWikitext_then_returnsCorrectValueAfterCallingFilter() {
		// given
		final AtomicBoolean isCalled = new AtomicBoolean(false);
		final QueryResponse queryResponse = queryResponse(
			pageWithRevision(wikitextRevision("my revision content"))
		);
		// when
		Stream<Boolean> result = new TestProcessor(page -> isCalled.getAndSet(true))
			.processQuery(queryResponse);
		// then
		SoftAssertions.assertSoftly(softly -> {
			// first ensure the result to force the stream to resolve
			softly
				.assertThat(result)
				.describedAs("result")
				.singleElement()
				.isEqualTo(true);
			softly.assertThat(isCalled).describedAs("called-filter").isTrue();
		});
	}
}
