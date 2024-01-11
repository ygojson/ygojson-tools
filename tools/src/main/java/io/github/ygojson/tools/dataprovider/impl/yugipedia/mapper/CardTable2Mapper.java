package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.WikitextTemplateMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;

/**
 * Maps a wikitext String to a {@link CardTable2} model,
 * using also {@link io.github.ygojson.tools.dataprovider.impl.yugipedia.model.MarkupString}
 * or derived models for some fields.
 */
@Mapper(uses = { MarkupStringMapper.class, IntegerOrUndefinedMapper.class })
public abstract class CardTable2Mapper {

	private final WikitextTemplateMapper wikitextTemplateMapper =
		Mappers.getMapper(WikitextTemplateMapper.class);

	/**
	 * Maps a wikitext String with a possibly existing CardTable2
	 * markup into a {@link CardTable2}.
	 *
	 * @param wikitext wikitext to parse
	 *
	 * @return parsed model; {@code null} if CardTable2 markup is not present.
	 */
	public CardTable2 mapWikitextToCardTable2(final String wikitext) {
		return mapToCardTable2(wikitextTemplateMapper.mapCardTable2Template(wikitext));
	}

	@Mapping(target = "anti_supports", source = "anti-supports")
	@Mapping(
		target = "anti_supports_archetypes",
		source = "anti-supports_archetypes"
	)
	@Mapping(target = "m_s_t", source = "m/s/t")
	// card_type model is only used for non-monster types regarding the documentation
	// thus set the default value for easier downstream processing and being explicit about it
	@Mapping(target = "card_type", defaultValue = "Monster")
	@Mapping(target = "ja_name", qualifiedByName = "rubyCharacters")
	@Mapping(target = "ja_pendulum_effect", qualifiedByName = "rubyCharacters")
	@Mapping(target = "ja_lore", qualifiedByName = "rubyCharacters")
	@Mapping(target = "ko_name", qualifiedByName = "rubyCharacters")
	@Mapping(target = "ko_pendulum_effect", qualifiedByName = "rubyCharacters")
	@Mapping(target = "ko_lore", qualifiedByName = "rubyCharacters")
	@Mapping(target = "link_arrows", qualifiedByName = "commaSeparatedStringList")
	protected abstract CardTable2 mapToCardTable2(
		final Map<String, String> asMap
	);
}
