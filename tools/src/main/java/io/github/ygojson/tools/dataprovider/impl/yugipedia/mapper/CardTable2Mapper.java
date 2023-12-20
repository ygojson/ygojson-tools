package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;

@Mapper(uses = MarkupStringMapper.class)
public abstract class CardTable2Mapper {

	public static final CardTable2Mapper INSTANCE = Mappers.getMapper(
		CardTable2Mapper.class
	);

	/**
	 * Pattern for the CardTable2 content
	 */
	private static final Pattern CARDTABLE2_CONTENT_PATTERN = Pattern.compile(
		"(?s).*\\{\\{CardTable2(.*)\n}}",
		Pattern.DOTALL
	);

	/**
	 * Group on the {@link #CARDTABLE2_CONTENT_PATTERN} where the content is.
	 */
	private static final int CARDTABLE2_CONTENT_GROUP = 1;

	/**
	 * Split pattern fore each fiel on the CardTable2 content.
	 */
	private static final Pattern CARDTABLE2_FIELD_SPLIT_PATTERN = Pattern.compile(
		"\n\\| "
	);

	/**
	 * Split pattern fore each key=value pair on the CardTable2 content.
	 */
	private static final Pattern CARDTABLE2_KEY_VALUE_SPLIT_PATTERN =
		Pattern.compile(" += ");

	public CardTable2 wikitextToCardTable2(final String wikitext) {
		return toCardTable2(wikitextToMap(wikitext));
	}

	private Map<String, String> wikitextToMap(final String wikitext) {
		if (wikitext == null) {
			return null;
		}
		final Matcher matcher = CARDTABLE2_CONTENT_PATTERN.matcher(wikitext);
		if (!matcher.matches()) {
			// no card table is present on the wikitext
			return null;
		}
		final String cardTableContent = matcher.group(CARDTABLE2_CONTENT_GROUP);
		return CARDTABLE2_FIELD_SPLIT_PATTERN
			.splitAsStream(cardTableContent)
			.skip(1) // the first contains only an empty string
			.map(val -> CARDTABLE2_KEY_VALUE_SPLIT_PATTERN.split(val, 2))
			.collect(Collectors.toMap(val -> val[0], val -> val[1]));
	}

	@Mapping(target = "anti_supports", source = "anti-supports")
	@Mapping(
		target = "anti_supports_archetypes",
		source = "anti-supports_archetypes"
	)
	@Mapping(target = "m_s_t", source = "m/s/t")
	@Mapping(target = "card_type", defaultValue = "Monster")
	@Mapping(target = "ja_name", qualifiedByName = "japanese")
	@Mapping(target = "ja_pendulum_effect", qualifiedByName = "japanese")
	@Mapping(target = "ja_lore", qualifiedByName = "japanese")
	protected abstract CardTable2 toCardTable2(final Map<String, String> asMap);
}
