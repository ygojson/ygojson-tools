package io.github.ygojson.application.yugipedia.mapper;

import java.util.Map;

import org.mapstruct.*;

import io.github.ygojson.application.core.datastore.db.set.RawSet;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Mapper for the YGOJSON {@link RawSet} from {@link YugipediaProperty} map.
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
	@Mapping(target = "enNameAlt", source = "en_name_2")
	// localized names
	@Mapping(target = "de.name", source = "de_name")
	@Mapping(target = "es.name", source = "es_name")
	@Mapping(target = "fr.name", source = "fr_name")
	@Mapping(target = "it.name", source = "it_name")
	@Mapping(target = "ja.name", source = "ja_name")
	@Mapping(target = "ko.name", source = "ko_name")
	@Mapping(target = "pt.name", source = "pt_name")
	@Mapping(target = "zhHans.name", source = "sc_name")
	@Mapping(target = "zhHant.name", source = "tc_name")
	// localized setCode / setCodesAlt are set with the YugipediaLocalizedDataMapper on after-mapping
	public abstract RawSet toEntity(
		final Map<String, YugipediaProperty> property
	);
}
