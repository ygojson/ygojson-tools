package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.MarkupStringMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;

@Mapper(
	uses = {
		GeneralMapper.class, MarkupStringMapper.class, YugipediaSetInfoMapper.class,
	},
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public abstract class YugipediaSetMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "type", source = "type", qualifiedByName = "toLowerCase")
	@Mapping(
		target = "series",
		source = "series",
		qualifiedByName = "toLowerCase"
	)
	@Mapping(target = "setInfo", source = ".", qualifiedByName = "mainSetInfo")
	@Mapping(
		target = "localizedData.de",
		source = ".",
		qualifiedByName = "deSetInfo"
	)
	@Mapping(
		target = "localizedData.es",
		source = ".",
		qualifiedByName = "esSetInfo"
	)
	@Mapping(
		target = "localizedData.fr",
		source = ".",
		qualifiedByName = "frSetInfo"
	)
	@Mapping(
		target = "localizedData.it",
		source = ".",
		qualifiedByName = "itSetInfo"
	)
	@Mapping(
		target = "localizedData.ja",
		source = ".",
		qualifiedByName = "jaSetInfo"
	)
	@Mapping(
		target = "localizedData.ko",
		source = ".",
		qualifiedByName = "koSetInfo"
	)
	@Mapping(
		target = "localizedData.pt",
		source = ".",
		qualifiedByName = "ptSetInfo"
	)
	@Mapping(
		target = "localizedData.zhHans",
		source = ".",
		qualifiedByName = "zhHansSetInfo"
	)
	@Mapping(
		target = "localizedData.zhHant",
		source = ".",
		qualifiedByName = "zhHantSetInfo"
	)
	public abstract Set mapToSet(final InfoboxSet infoboxSet);
}
