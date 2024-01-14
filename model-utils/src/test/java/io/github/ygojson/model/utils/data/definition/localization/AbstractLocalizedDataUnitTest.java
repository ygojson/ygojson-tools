package io.github.ygojson.model.utils.data.definition.localization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.model.utils.test.ModelTestData;

public abstract class AbstractLocalizedDataUnitTest<T> {

	private static List<String> EXPECTED_LANGUAGE_KEYS;

	private static Settings MIN_SIZE_SETTING;

	@BeforeAll
	static void beforeAll() {
		EXPECTED_LANGUAGE_KEYS = ModelTestData.getLocalizedLanguageKeys();
		final int minSize = EXPECTED_LANGUAGE_KEYS.size() * 2; // ensure enough languages
		MIN_SIZE_SETTING =
			Settings
				.create()
				.set(Keys.COLLECTION_MIN_SIZE, minSize)
				.set(Keys.MAP_MIN_SIZE, minSize)
				.lock();
	}

	protected abstract Class<T> getLocalizedDataClass();

	@Test
	void given_cardLocalizedDataWithAllLanguages_when_serialized_then_allLanguagesExists()
		throws JsonProcessingException {
		// given
		final T localizedData = Instancio
			.of(getLocalizedDataClass())
			.withSettings(MIN_SIZE_SETTING)
			.create();
		// when
		final String serialized = JsonUtils
			.getObjectMapper()
			.writeValueAsString(localizedData);
		// then
		assertThat(serialized).contains(EXPECTED_LANGUAGE_KEYS);
	}

	@Test
	void given_cardLocalizedDataWithAllLanguages_when_serialized_then_mainLanguageDoesNotExists()
		throws JsonProcessingException {
		// given
		final T localizedData = Instancio
			.of(getLocalizedDataClass())
			.withSettings(MIN_SIZE_SETTING)
			.create();
		// when
		final String serialized = JsonUtils
			.getObjectMapper()
			.writeValueAsString(localizedData);
		// then
		final String notExpected = ModelTestData.getLocalizedLanguageKey(
			Language.EN
		);
		assertThat(serialized).doesNotContain(notExpected);
	}
}
