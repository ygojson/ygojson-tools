package io.github.ygojson.acceptancetest.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.instancio.settings.BeanValidationTarget;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.acceptancetest.JsonAcceptance;
import io.github.ygojson.model.utils.serialization.JsonUtils;

@Tag("acceptance-test")
public class SerializationAcceptanceTest {

	private static JsonAcceptance ACCEPTANCE;
	private static Settings INSTANCIO_SETTINGS;

	@BeforeAll
	static void beforeAll() {
		// use the JSONUtils mapper to ensure that it is properly configured
		ACCEPTANCE = new JsonAcceptance(JsonUtils.getObjectMapper());
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
		// when/then
		final String testCase = "serialization/" + clazz.getSimpleName();
		ACCEPTANCE.verify(testCase, dataModel);
	}
}
