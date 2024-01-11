package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext;

import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.MarkupString;

/**
 * Maps a wikitext String to a {@link InfoboxSet} model,
 * using also {@link MarkupString}
 * or derived models for some fields.
 */
@Mapper(uses = { MarkupStringMapper.class })
public abstract class InfoboxSetMapper {

	private final WikitextTemplateMapper wikitextTemplateMapper =
		Mappers.getMapper(WikitextTemplateMapper.class);

	/**
	 * Maps a wikitext String with a possibly existing InfoBox Set
	 * markup into a {@link InfoboxSet}.
	 *
	 * @param wikitext wikitext to parse
	 *
	 * @return parsed model; {@code null} if InfoBox Set markup is not present.
	 */
	public InfoboxSet mapWikitextToInfoboxSet(final String wikitext) {
		return mapToInfoboxSet(
			wikitextTemplateMapper.mapInfoboxSetTemplate(wikitext)
		);
	}

	@Mapping(target = "ja_name", qualifiedByName = "rubyCharacters")
	@Mapping(target = "ko_name", qualifiedByName = "rubyCharacters")
	@Mapping(target = "sp_lat_release_date", source = "sp-lat_release_date")
	@Mapping(
		target = "fr_de_it_sp_release_date",
		source = "fr/de/it/sp_release_date"
	)
	@Mapping(target = "two_pack_set", source = "2-pack_set")
	protected abstract InfoboxSet mapToInfoboxSet(final Map<String, String> map);
}
