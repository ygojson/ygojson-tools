package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.IntegerOrUndefinedMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser.ParsedWikitext;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser.YugipediaWikitextParser;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.MarkupString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

/**
 * Maps a wikitext String to a {@link CardTable2} model,
 * using also {@link MarkupString}
 * or derived models for some fields.
 */
@Mapper(uses = { MarkupStringMapper.class, IntegerOrUndefinedMapper.class })
public abstract class CardTable2Mapper {

	// TODO: should be injected and/or created as a mapper?
	private final YugipediaWikitextParser parser = new YugipediaWikitextParser();

	/**
	 * Maps a wikitext String with a possibly existing CardTable2
	 * markup into a {@link CardTable2}.
	 *
	 * @param wikitext wikitext to parse
	 *
	 * @return parsed model; {@code null} if CardTable2 markup is not present.
	 */
	public CardTable2 mapWikitextToCardTable2(final String title, final String wikitext) {
		// TODO: we should use the title here to override the name if not present?
		final ParsedWikitext parsedWikitext = parser.parse(title, wikitext);
		return mapToCardTable2(parsedWikitext.getTemplateMap("CardTable2"));
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
