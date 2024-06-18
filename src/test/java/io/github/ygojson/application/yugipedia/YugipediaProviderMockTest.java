package io.github.ygojson.application.yugipedia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.assertj.core.api.ThrowableAssert;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.testutil.YugipediaMockProfile;

@QuarkusTest
@TestProfile(YugipediaMockProfile.class)
class YugipediaProviderMockTest {

	@RestClient
	YugipediaClient testClient;

	private YugipediaProvider provider;

	private YugipediaProvider createOrGetProvider() {
		if (provider == null) {
			provider = new YugipediaProvider(testClient, Limit.getDefault());
		}
		return provider;
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
