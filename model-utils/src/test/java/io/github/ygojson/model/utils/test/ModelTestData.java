package io.github.ygojson.model.utils.test;

import java.util.stream.Stream;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.VersionInfo;

public class ModelTestData {

	static Stream<Class<?>> getMainModels() {
		return Stream.of(
			Card.class,
			Print.class,
			Set.class,
			VersionInfo.class // utility model also considered main model
		);
	}
}
