package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.MarkupStringMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;

/**
 * @deprecated should use the {@link io.github.ygojson.application.yugipedia.mapper.YugipediaSetMapper} instead.
 */
@Mapper(
	uses = {
		GeneralMapper.class, MarkupStringMapper.class, YugipediaSetInfoMapper.class,
	},
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
@Deprecated
public abstract class YugipediaSetMapper {

	/**
	 * Maps to the YGOJSON Set model the Yugipedia relevant information.
	 *
	 * @param infoboxSet the InfoboxSet model
	 * @param name name to be used if none is present (the page-title)
	 *
	 * @return the set
	 */
	public Set mapToSet(final InfoboxSet infoboxSet, final String name) {
		final Set set = mapToSet(infoboxSet);
		if (set.getName() == null) {
			set.setName(name);
		}
		return set;
	}

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "name", ignore = true) // SetInfo properties are added on the YugipediaSetInfoMapper
	@Mapping(target = "nameAlt", ignore = true)
	@Mapping(target = "setCode", ignore = true)
	@Mapping(target = "setCodeAlt", ignore = true)
	@Mapping(target = "printNumberPrefix", source = "postfix")
	@Mapping(target = "type", source = "type", qualifiedByName = "toLowerCase")
	@Mapping(
		target = "series",
		source = "series",
		qualifiedByName = "toLowerCase"
	)
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
