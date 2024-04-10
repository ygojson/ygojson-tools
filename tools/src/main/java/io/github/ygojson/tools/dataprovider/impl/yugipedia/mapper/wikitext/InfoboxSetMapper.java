package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser.ParsedWikitext;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser.YugipediaWikitextParser;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.MarkupString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

/**
 * Maps a wikitext String to a {@link InfoboxSet} model,
 * using also {@link MarkupString}
 * or derived models for some fields.
 */
@Mapper(uses = { MarkupStringMapper.class })
public abstract class InfoboxSetMapper {

	private final YugipediaWikitextParser parser = new YugipediaWikitextParser();

	/**
	 * Maps a wikitext String with a possibly existing InfoBox Set
	 * markup into a {@link InfoboxSet}.
	 *
	 * @param wikitext wikitext to parse
	 *
	 * @return parsed model; {@code null} if InfoBox Set markup is not present.
	 */
	public InfoboxSet mapWikitextToInfoboxSet(final String title, final String wikitext) {
		// TODO: we should use the title here to override the name if not present?
		final ParsedWikitext parsedWikitext = parser.parse(title, wikitext);
		return mapToInfoboxSet(parsedWikitext.getTemplateMap2("Infobox set"));
	}

	//@Mapping(target = "ja_name", qualifiedByName = "rubyCharacters")
	//@Mapping(target = "ko_name", qualifiedByName = "rubyCharacters")
	@Mapping(target = "sp_lat_release_date", source = "sp-lat_release_date")
	@Mapping(
		target = "fr_de_it_sp_release_date",
		source = "fr/de/it/sp_release_date"
	)
	@Mapping(target = "two_pack_set", source = "2-pack_set")
	protected abstract InfoboxSet mapToInfoboxSet(final Map<String, Object> map);


	protected String objectToString(final Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		if (obj instanceof List<?> l) {
			// TODO: how to handle recursive list
			return String.join(",", l.stream().map(this::objectToString).toList());
		}
		return obj.toString();
	}

	protected List<String> objectToStringList(final Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof List<?> l) {
			return l.stream().map(this::objectToString).toList();
		}
		return List.of(objectToString(obj));
	}
}
