package io.github.ygojson.model.utils.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.VersionInfo;
import io.github.ygojson.model.data.definition.localization.Language;

public class ModelTestData {

	public static List<String> getLocalizedLanguageKeys() {
		return Arrays
			.stream(Language.values())
			.filter(l -> l != Language.EN) // English is the main language
			.map(ModelTestData::getLocalizedLanguageKey)
			.toList();
	}

	public static String getLocalizedLanguageKey(Language lang) {
		return String.format("\"%s\":", lang.value());
	}

	static Stream<Class<?>> getMainModels() {
		return Stream.of(
			Card.class,
			Print.class,
			Set.class,
			VersionInfo.class // utility model also considered main model
		);
	}
}
