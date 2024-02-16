package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

@Mapper
public abstract class WikitextTemplateMapper {

	/**
	 * Pattern for the CardTable2 content
	 */
	private static final Pattern CARDTABLE2_CONTENT_PATTERN = Pattern.compile(
		"(?s).*\\{\\{CardTable2(.*)\n}}"
	);

	/**
	 * Group on the {@link #CARDTABLE2_CONTENT_PATTERN} where the content is.
	 */
	private static final int CARDTABLE2_CONTENT_GROUP = 1;

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
	 * Split pattern fore each field on the Templates.
	 */
	private static final Pattern TEMPLATE_FIELD_SPLIT_PATTERN = Pattern.compile(
		"\n\\| "
	);

	/**
	 * Split pattern fore each key=value pair on the Templates.
	 */
	private static final Pattern TEMPLATE_KEY_VALUE_SPLIT_PATTERN =
		Pattern.compile(" += ");

	/**
	 * Maps a wikitext String with a possibly existing CardTable2
	 * markup into a Map with the found properties.
	 *
	 * @param wikitext wikitext to parse
	 *
	 * @return parsed properties; {@code null} if CardTable2 markup is not present.
	 */
	public Map<String, String> mapCardTable2Template(final String wikitext) {
		return wikitextToMap(
			CARDTABLE2_CONTENT_PATTERN,
			CARDTABLE2_CONTENT_GROUP,
			wikitext
		);
	}

	/**
	 * Maps a wikitext String with a possibly existing InfoBoxSet
	 * markup into a Map with the found properties.
	 *
	 * @param wikitext wikitext to parse
	 *
	 * @return parsed properties; {@code null} if InfoBoxSet markup is not present.
	 */
	public Map<String, String> mapInfoboxSetTemplate(final String wikitext) {
		return wikitextToMap(
			INFOBOX_SET_CONTENT_PATTERN,
			INFOBOX_SET_CONTENT_GROUP,
			wikitext
		);
	}

	protected Map<String, String> wikitextToMap(
		final Pattern templatePattern,
		final int contentGroup,
		final String wikitext
	) {
		if (wikitext == null) {
			return null;
		}
		final Matcher matcher = templatePattern.matcher(wikitext);
		if (!matcher.find()) {
			// no card table is present on the wikitext
			return null;
		}
		final String contentString = matcher.group(contentGroup);
		return TEMPLATE_FIELD_SPLIT_PATTERN
			.splitAsStream(contentString)
			.map(val -> TEMPLATE_KEY_VALUE_SPLIT_PATTERN.split(val, 2))
			.filter(val -> val.length == 2) // ignore lines not having a key-value pair
			.collect(Collectors.toMap(val -> val[0], val -> val[1].trim())); // trim as sometimes it has extra new-lines
	}
}
