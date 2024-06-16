package io.github.ygojson.application.yugipedia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.testutil.server.InjectWireMock;
import io.github.ygojson.application.yugipedia.client.YugipediaClientMother;
import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.testutil.YugipediaMockProfile;

@QuarkusTest
@TestProfile(YugipediaMockProfile.class)
class YugipediaProviderMockTest {

	private static final Logger LOG = LoggerFactory.getLogger(
		YugipediaProviderMockTest.class
	);

	@InjectWireMock
	WireMockServer mockServer;

	private static YugipediaProvider PROVIDER;

	private YugipediaProvider createOrGetProvider() {
		if (PROVIDER == null) {
			PROVIDER =
				new YugipediaProvider(
					YugipediaClientMother.mocked(mockServer),
					Limit.getDefault()
				);
		}
		return PROVIDER;
	}

	@ParameterizedTest
	@ValueSource(ints = { 10, 15, 20 })
	void given_mockDataProviderFetchSets_when_streamCollectedWithLimitLessThanMockedData_then_noFailureAnd10Sets(
		final int limit
	) {
		// given
		final var fetchingStream = createOrGetProvider().fetchSets();
		// when
		final var collectedSets = fetchingStream.limit(limit).toList();
		// then
		assertThat(collectedSets).hasSize(limit);
	}

	@Test
	void given_mockDataProviderFetchSets_when_cannotFetchMoreData_then_throwYugipediaException() {
		// given
		final var fetchingStream = createOrGetProvider().fetchSets();
		// when
		final ThrowableAssert.ThrowingCallable callable = fetchingStream::toList;
		// then
		assertThatThrownBy(callable).isInstanceOf(YugipediaException.class);
	}
}
