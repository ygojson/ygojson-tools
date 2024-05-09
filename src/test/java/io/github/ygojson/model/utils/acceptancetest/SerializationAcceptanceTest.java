package io.github.ygojson.model.utils.acceptancetest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.approvaltests.Approvals;
import org.instancio.Instancio;
import org.instancio.settings.BeanValidationTarget;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.model.utils.serialization.JsonUtils;

public class SerializationAcceptanceTest {

	private static ObjectMapper OBJECT_MAPPER;
	private static Settings INSTANCIO_SETTINGS;

	@BeforeAll
	static void beforeAll() {
		OBJECT_MAPPER = JsonUtils.getObjectMapper();
		INSTANCIO_SETTINGS =
			Settings
				.defaults()
				.set(Keys.BEAN_VALIDATION_ENABLED, true)
				.set(Keys.BEAN_VALIDATION_TARGET, BeanValidationTarget.GETTER);
	}

	private Object randomInstance(final Class<?> type, final long seed) {
		return Instancio
			.of(type)
			.withSettings(INSTANCIO_SETTINGS)
			.withSeed(seed)
			.create();
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.model.utils.test.ModelTestData#getMainModels"
	)
	void testDataModelSerialization(final Class<?> clazz)
		throws JsonProcessingException {
		// given
		final Object dataModel = randomInstance(clazz, 1);
		// when
		final String serialized = OBJECT_MAPPER
			.writerWithDefaultPrettyPrinter()
			.writeValueAsString(dataModel);
		// then
		Approvals.verify(
			serialized,
			Approvals.NAMES
				.withParameters(clazz.getSimpleName())
				.forFile()
				.withExtension(".json")
		);
	}
}
