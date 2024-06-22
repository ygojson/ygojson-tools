package io.github.ygojson.application.yugipedia.processor;

import static io.github.ygojson.application.yugipedia.client.YugipediaQueryResponseMother.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.github.ygojson.application.testutil.MutinyTestUtil;
import io.github.ygojson.application.yugipedia.YugipediaException;
import io.github.ygojson.application.yugipedia.client.response.Page;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;
import io.github.ygojson.application.yugipedia.client.response.Revision;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaParserProcessorUnitTest {

	private static class TestThrowingExceptionParser implements YugipediaParser {

		@Override
		public String getName() {
			return "test-throwing-exception";
		}

		@Override
		public Map<String, YugipediaProperty> parse(
			String title,
			long pageid,
			String wikitext
		) {
			throw new YugipediaException(getName());
		}
	}

	private static class TestParser implements YugipediaParser {

		static Map<String, YugipediaProperty> parseWikitextResult(
			final String wikitext
		) {
			return Map.of("test", YugipediaProperty.text(wikitext));
		}

		@Override
		public String getName() {
			return "test-parser";
		}

		@Override
		public Map<String, YugipediaProperty> parse(
			String title,
			long pageid,
			String wikitext
		) {
			// return empty map
			return parseWikitextResult(wikitext);
		}
	}

	private static YugipediaParserProcessor createTestProcessor() {
		return new YugipediaParserProcessor(new TestParser());
	}

	@Test
	void given_queryResponseWithNullPages_when_processQuery_then_returnEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse((List<Page>) null);
		// when
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithEmptyPages_when_processQuery_then_returnEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse(List.of());
		// when
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithPageMarkedAsMissing_when_processQuery_then_returnNull() {
		// given
		final QueryResponse queryResponse = queryResponse(missingPage());
		// when
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_queryResponseWithRandom_when_processQuery_then_isEmpty() {
		// given
		final QueryResponse queryResponse = queryResponse(randomPage());
		// when
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
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
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
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
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
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
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
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
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
		// then
		assertThat(result).isEmpty();
	}

	@Test
	void given_throwingYugipediaExceptionProcessor_when_processValindWikitext_then_returnsEmpty() {
		// given
		final YugipediaParserProcessor throwingProcessor =
			new YugipediaParserProcessor(new TestThrowingExceptionParser());
		// when
		var asyncProcess = throwingProcessor.processQuery(
			queryResponse(pageWithRevision(wikitextRevision("Correct")))
		);
		var result = MutinyTestUtil.collectAll(asyncProcess);
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
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
		// then
		assertThat(result)
			.describedAs("result-stream")
			.singleElement()
			.describedAs("result-value")
			.isEqualTo(TestParser.parseWikitextResult(null));
	}

	@Test
	void given_queryResponseWithWikitextRevision_when_processWikitext_then_returnsCorrectValue() {
		// given
		final QueryResponse queryResponse = queryResponse(
			pageWithRevision(wikitextRevision("my revision content"))
		);
		// when
		var asyncProcess = createTestProcessor().processQuery(queryResponse);
		var result = MutinyTestUtil.collectAll(asyncProcess);
		// then
		assertThat(result)
			.describedAs("result-stream")
			.singleElement()
			.describedAs("result-value")
			.isEqualTo(TestParser.parseWikitextResult("my revision content"));
	}
}
