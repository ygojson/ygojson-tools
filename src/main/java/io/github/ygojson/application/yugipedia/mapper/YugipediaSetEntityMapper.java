package io.github.ygojson.application.yugipedia.mapper;

import java.util.Map;

import org.mapstruct.*;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.application.yugipedia.parser.model.CustomProperties;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Mapper for the YGOJSON {@link SetEntity} from {@link YugipediaProperty} map.
 */
@Mapper(
	uses = {
		YugipediaPropertyBaseMapper.class, YugipediaLocalizedDataMapper.class,
	},
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = MappingConstants.ComponentModel.JAKARTA_CDI
)
public abstract class YugipediaSetEntityMapper {

	// ignored
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ygojsonId", ignore = true)
	@Mapping(target = "yugipediaPageId", source = CustomProperties.PAGE_ID)
	// common
	@Mapping(target = "printNumberPrefix", source = "postfix")
	@Mapping(
		target = "type",
		source = "type",
		qualifiedByName = YugipediaPropertyBaseMapper.TO_LOWER_CASE
	)
	@Mapping(
		target = "series",
		source = "series",
		qualifiedByName = YugipediaPropertyBaseMapper.TO_LOWER_CASE
	)
	// main name and altName
	@Mapping(target = "en.name", source = "en_name")
	@Mapping(target = "en.konamiId", source = "en_database_id")
	@Mapping(target = "enNameAlt", source = "en_name_2")
	// localized names
	@Mapping(target = "de.name", source = "de_name")
	@Mapping(target = "de.konamiId", source = "de_database_id")
	@Mapping(target = "es.name", source = "es_name")
	@Mapping(target = "es.konamiId", source = "es_database_id")
	@Mapping(target = "fr.name", source = "fr_name")
	@Mapping(target = "fr.konamiId", source = "fr_database_id")
	@Mapping(target = "it.name", source = "it_name")
	@Mapping(target = "it.konamiId", source = "it_database_id")
	@Mapping(target = "ja.name", source = "ja_name")
	@Mapping(target = "ja.konamiId", source = "ja_database_id")
	@Mapping(target = "ko.name", source = "ko_name")
	@Mapping(target = "ko.konamiId", source = "ko_database_id")
	@Mapping(target = "pt.name", source = "pt_name")
	@Mapping(target = "pt.konamiId", source = "pt_database_id")
	@Mapping(target = "zhHans.name", source = "sc_name")
	@Mapping(target = "zhHans.konamiId", source = "sc_database_id")
	@Mapping(target = "zhHant.name", source = "tc_name")
	@Mapping(target = "zhHant.konamiId", source = "tc_database_id")
	// localized setCode / setCodesAlt are set with the YugipediaLocalizedDataMapper on after-mapping
	public abstract SetEntity toEntity(
		final Map<String, YugipediaProperty> property
	);
}
