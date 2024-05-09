package io.github.ygojson.application.yugipedia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.testutil.server.MockedServer;
import io.github.ygojson.application.yugipedia.client.YugipediaClientMother;
import io.github.ygojson.application.yugipedia.client.YugipediaMockServerFactory;
import io.github.ygojson.application.yugipedia.client.params.Limit;

class YugipediaProviderMockTest {

	private static final Logger LOG = LoggerFactory.getLogger(
		YugipediaProviderMockTest.class
	);

	private static MockedServer MOCK_SERVER;
	private static YugipediaProvider PROVIDER;

	@BeforeAll
	static void beforeAll() {
		MOCK_SERVER = YugipediaMockServerFactory.create(LOG);
		PROVIDER =
			new YugipediaProvider(
				YugipediaClientMother.mocked(MOCK_SERVER),
				Limit.getDefault()
			);
	}

	@AfterAll
	static void afterAll() throws IOException {
		MOCK_SERVER.close();
	}

	@ParameterizedTest
	@ValueSource(ints = { 10, 15, 20 })
	void given_mockDataProviderFetchSets_when_streamCollectedWithLimitLessThanMockedData_then_noFailureAnd10Sets(
		final int limit
	) {
		// given
		final var fetchingStream = PROVIDER.fetchSets();
		// when
		final var collectedSets = fetchingStream.limit(limit).toList();
		// then
		assertThat(collectedSets).hasSize(limit);
	}

	@Test
	void given_mockDataProviderFetchSets_when_cannotFetchMoreData_then_throwYugipediaException() {
		// given
		final var fetchingStream = PROVIDER.fetchSets();
		// when
		final ThrowableAssert.ThrowingCallable callable = fetchingStream::toList;
		// then
		assertThatThrownBy(callable).isInstanceOf(YugipediaException.class);
	}
}
