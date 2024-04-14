package io.github.ygojson.tools.dataprovider.utils.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.application.ApplicationInfo;
import io.github.ygojson.application.util.http.ClientConfig;
import io.github.ygojson.application.util.http.ClientFactory;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaConfig;

class ClientFactoryTest {

	private static ClientFactory SERVICE_FACTORY;

	@BeforeAll
	static void beforeAll() {
		SERVICE_FACTORY =
			new ClientFactory(
				new ObjectMapper(),
				new ApplicationInfo("test", "test", "test")
			);
	}

	public static Stream<Arguments> availableServices() {
		return Stream.of(Arguments.of(new YugipediaConfig()));
	}

	@MethodSource("availableServices")
	@ParameterizedTest
	void given_availableServices_when_getService_then_serviceClassIsProvided(
		final ClientConfig<?> config
	) {
		// given - factory
		// when
		final Object client = SERVICE_FACTORY.getClient(config);
		// then
		assertThat(client).isInstanceOf(config.clientClass());
	}
}
