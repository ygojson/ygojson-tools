package io.github.ygojson.application.yugipedia.mapper;

import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.application.yugipedia.mapper.language.*;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Localized data mapper for yugipedia.
 */
@ApplicationScoped
public class YugipediaLocalizedDataMapper {

	private final List<LanguageHandler> handlers =
		LanguageHandlerFactory.createAll();

	@AfterMapping
	protected void addLocalizedData(
		@MappingTarget final SetEntity entity,
		final Map<String, YugipediaProperty> properties
	) {
		handlers.forEach(mapper ->
			mapper.addLanguagePropertiesToSetEntity(entity, properties)
		);
	}
}
