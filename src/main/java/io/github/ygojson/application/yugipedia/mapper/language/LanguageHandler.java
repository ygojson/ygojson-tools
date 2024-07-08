package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.Map;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

public interface LanguageHandler {
	/**
	 * Adds the language properties for this language to the entity.
	 *
	 * @param entity the entity to map.
	 * @param properties the properties to use.
	 */
	void addLanguagePropertiesToSetEntity(
		final SetEntity entity,
		final Map<String, YugipediaProperty> properties
	);
}
