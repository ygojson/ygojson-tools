package io.github.ygojson.model.utils.acceptancetest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.instancio.settings.BeanValidationTarget;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.testutil.JsonAcceptance;

public class SerializationAcceptanceTest {

	private static JsonAcceptance JSON_ACCEPTANCE;
	private static Settings INSTANCIO_SETTINGS;

	@BeforeAll
	static void beforeAll() {
		JSON_ACCEPTANCE =
			JsonAcceptance.fromTestClass(SerializationAcceptanceTest.class);
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
		final String modelName = clazz.getSimpleName();
		// verify
		final String testCase =
			"SerializationAcceptanceTest.testDataModelSerialization." + modelName;
		JSON_ACCEPTANCE.verify(testCase, dataModel);
	}
}
