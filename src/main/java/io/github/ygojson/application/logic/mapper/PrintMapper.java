package io.github.ygojson.application.logic.mapper;

import org.mapstruct.*;

import io.github.ygojson.application.core.db.print.PrintEntity;
import io.github.ygojson.model.data.Print;

/**
 * Mapper between the {@link Print} model and the {@link PrintEntity}.
 */
@Mapper(
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = MappingConstants.ComponentModel.JAKARTA_CDI
)
public abstract class PrintMapper {

	@Mapping(target = "id", source = "ygojsonId")
	@Mapping(target = "cardId", source = "card.ygojsonId")
	@Mapping(target = "setId", source = "set.ygojsonId")
	public abstract Print toModel(PrintEntity entity);

	@InheritInverseConfiguration
	@Mapping(target = "id", ignore = true)
	public abstract PrintEntity toEntity(Print model);
}
