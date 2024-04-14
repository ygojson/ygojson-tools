package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.DataProviderException;
import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.YugipediaApiMother;
import io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia.YugipediaMockServer;

class YugipediaDataProviderTest {

	private static YugipediaMockServer MOCK_SERVER;
	private static YugipediaDataProvider MOCK_DATA_PROVIDER;

	@BeforeAll
	static void beforeAll() {
		MOCK_SERVER = new YugipediaMockServer();
		MOCK_DATA_PROVIDER =
			new YugipediaDataProvider(
				YugipediaApiMother.mockTestClient(MOCK_SERVER.getUrl()),
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
		final Stream<Set> fetchingStream = MOCK_DATA_PROVIDER.fetchSets();
		// when
		final List<Set> collectedSets = fetchingStream.limit(limit).toList();
		// then
		assertThat(collectedSets).hasSize(limit);
	}

	@Test
	void given_mockDataProviderFetchSets_when_cannotFetchMoreData_then_throwDataProviderException() {
		// given
		final Stream<Set> fetchingStream = MOCK_DATA_PROVIDER.fetchSets();
		// when
		final ThrowableAssert.ThrowingCallable callable = fetchingStream::toList;
		// then
		assertThatThrownBy(callable).isInstanceOf(DataProviderException.class);
	}
}
