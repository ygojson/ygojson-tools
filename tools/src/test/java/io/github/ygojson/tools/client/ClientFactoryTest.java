package io.github.ygojson.tools.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.tools.client.yugipedia.YugipediaConfig;
import io.github.ygojson.tools.common.ApplicationInfo;

class ClientFactoryTest {

	private static ClientFactory CLIENT_FACTORY;

	@BeforeAll
	static void beforeAll() {
		CLIENT_FACTORY =
			new ClientFactory(
				new ObjectMapper(),
				new ApplicationInfo("test", "test", "test")
			);
	}

	public static Stream<Arguments> availableClients() {
		return Stream.of(Arguments.of(new YugipediaConfig()));
	}

	@MethodSource("availableClients")
	@ParameterizedTest
	void given_availableClients_when_createClient_then_apiClassIsProvided(
		final ClientConfig<?> config
	) {
		// given - factory
		// when
		final Object client = CLIENT_FACTORY.getClient(config);
		// then
		assertThat(client).isInstanceOf(config.apiClass());
	}
}
