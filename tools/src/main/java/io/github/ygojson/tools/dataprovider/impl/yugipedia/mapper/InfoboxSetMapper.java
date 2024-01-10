package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.InfoboxSet;

/**
 * Maps a wikitext String to a {@link InfoboxSet} model,
 * using also {@link io.github.ygojson.tools.dataprovider.impl.yugipedia.model.MarkupString}
 * or derived models for some fields.
 */
@Mapper(uses = { MarkupStringMapper.class })
public abstract class InfoboxSetMapper {

	/**
	 * Pattern for the InfoBoxSet content
	 */
	private static final Pattern INFOBOX_SET_CONTENT_PATTERN = Pattern.compile(
		"(?s).*\\{\\{Infobox set(.*?)\n}}"
	);

	/**
	 * Group on the {@link #INFOBOX_SET_CONTENT_PATTERN} where the content is.
	 */
	private static final int INFOBOX_SET_CONTENT_GROUP = 1;

	/**
	 * Split pattern fore each field on the InfoBox Set content.
	 */
	private static final Pattern INFOBOX_SET_FIELD_SPLIT_PATTERN =
		Pattern.compile("\n\\| ");

	/**
	 * Split pattern fore each key=value pair on the InfoBox Set content.
	 */
	private static final Pattern INFOBOX_SET_KEY_VALUE_SPLIT_PATTERN =
		Pattern.compile(" += ");

	/**
	 * Maps a wikitext String with a possibly existing InfoBox Set
	 * markup into a {@link InfoboxSet}.
	 *
	 * @param wikitext wikitext to parse
	 *
	 * @return parsed model; {@code null} if InfoBox Set markup is not present.
	 */
	public InfoboxSet mapWikitextToInfoboxSet(final String wikitext) {
		return mapToInfoboxSet(wikitextToMap(wikitext));
	}

	protected Map<String, String> wikitextToMap(final String wikitext) {
		if (wikitext == null) {
			return null;
		}
		final Matcher matcher = INFOBOX_SET_CONTENT_PATTERN.matcher(wikitext);
		if (!matcher.find()) {
			// no card table is present on the wikitext
			return null;
		}
		final String cardTableContent = matcher.group(INFOBOX_SET_CONTENT_GROUP);
		return INFOBOX_SET_FIELD_SPLIT_PATTERN
			.splitAsStream(cardTableContent)
			.map(val -> INFOBOX_SET_KEY_VALUE_SPLIT_PATTERN.split(val, 2))
			.filter(val -> val.length == 2) // ignore lines not having a key-value pair
			.collect(Collectors.toMap(val -> val[0], val -> val[1].trim())); // trim as sometimes it has extra new-lines
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
