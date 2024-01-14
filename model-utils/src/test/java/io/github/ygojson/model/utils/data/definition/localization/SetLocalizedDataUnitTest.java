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
import io.github.ygojson.model.data.definition.localization.SetLocalizedData;
import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.model.utils.test.ModelTestData;

public class SetLocalizedDataUnitTest {

	private static List<String> EXPECTED_LANGUAGES;

	private static Settings MIN_SIZE_SETTING;

	@BeforeAll
	static void beforeAll() {
		EXPECTED_LANGUAGES = ModelTestData.getLocalizedLanguageKeys();
		MIN_SIZE_SETTING =
			Settings
				.create()
				.set(Keys.COLLECTION_MIN_SIZE, EXPECTED_LANGUAGES.size())
				.set(Keys.MAP_MIN_SIZE, EXPECTED_LANGUAGES.size())
				.lock();
	}

	@Test
	void given_setLocalizedDataWithAllLanguages_when_serialized_then_allLanguagesExists()
		throws JsonProcessingException {
		// given
		final SetLocalizedData setLocalizedData = Instancio
			.of(SetLocalizedData.class)
			.withSettings(MIN_SIZE_SETTING)
			.create();
		// when
		final String serialized = JsonUtils
			.getObjectMapper()
			.writeValueAsString(setLocalizedData);
		// then
		assertThat(serialized).contains(EXPECTED_LANGUAGES);
	}

	@Test
	void given_setLocalizedDataWithAllLanguages_when_serialized_then_mainLanguageDoesNotExists()
		throws JsonProcessingException {
		// given
		final SetLocalizedData setLocalizedData = Instancio
			.of(SetLocalizedData.class)
			.withSettings(MIN_SIZE_SETTING)
			.create();
		// when
		final String serialized = JsonUtils
			.getObjectMapper()
			.writeValueAsString(setLocalizedData);
		// then
		final String notExpected = ModelTestData.getLocalizedLanguageKey(
			Language.EN
		);
		assertThat(serialized).doesNotContain(notExpected);
	}
}
