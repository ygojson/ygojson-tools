package io.github.ygojson.application.yugipedia.parser;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Abstract class to help parsing a template with properties.
 */
abstract class TemplateParser {

	// Split pattern fore each field on the Templates.
	private static final Pattern TEMPLATE_FIELD_SPLIT_PATTERN = Pattern.compile(
		"\n\\| "
	);

	// Split pattern fore each key=value pair on the Templates.
	private static final Pattern TEMPLATE_KEY_VALUE_SPLIT_PATTERN =
		Pattern.compile(" += ");

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final PropertyParser propertyParser;
	private final Pattern templatePattern;
	private final int contentGroup;

	protected TemplateParser(
		final PropertyParser parser,
		final Pattern templatePattern,
		final int contentGroup
	) {
		this.propertyParser = parser;
		this.templatePattern = templatePattern;
		this.contentGroup = contentGroup;
	}

	/**
	 * Ignore properties before they are parsed.
	 *
	 * @param property the property to check.
	 *
	 * @return {@code true} if the property should be ignored; {@code false} otherwise.
	 */
	protected abstract boolean ignoreProperty(final String property);

	/**
	 * Return the type of the property.
	 * <br>
	 * If the property is unknown, a {@link PropertyParser.Type#TEXT} MUST be returned.
	 *
	 * @param property the property to check.
	 *
	 * @return the type of the property.
	 */
	protected abstract PropertyParser.Type getPropertyType(final String property);

	/**
	 * Parse the template as a map of properties.
	 *
	 * @param wikitext the wikitext to parse.
	 *
	 * @return the parsed properties.
	 */
	public Map<String, YugipediaProperty> parse(final String wikitext) {
		final Matcher matcher = templatePattern.matcher(wikitext);
		if (!matcher.find()) {
			// no card table is present on the wikitext
			return null;
		}
		final String contentString = matcher.group(contentGroup);

		return TEMPLATE_FIELD_SPLIT_PATTERN
			.splitAsStream(contentString)
			.map(val -> TEMPLATE_KEY_VALUE_SPLIT_PATTERN.split(val, 2))
			.filter(this::keepArg) // ignore lines not having a key-value pair and explicitly ignored
			.collect(
				Collectors.toMap(
					val -> val[0],
					val -> propertyParser.parse(getPropertyType(val[0]), val[1])
				)
			);
	}

	// check if the argument should be kept the template argument
	private boolean keepArg(final String[] templateArg) {
		// ignore empty lines
		if (templateArg.length == 0 || templateArg[0].isBlank()) {
			return false;
		}
		// TODO: maybe we should keep template flags!
		if (templateArg.length == 1) {
			log.debug("Ignored template argument without value: {}", templateArg[0]);
			return false;
		}
		if (ignoreProperty(templateArg[0])) {
			log.debug("Ignored template argument: {}", templateArg[0]);
			return false;
		}
		// keep if not filtered
		return true;
	}
}
