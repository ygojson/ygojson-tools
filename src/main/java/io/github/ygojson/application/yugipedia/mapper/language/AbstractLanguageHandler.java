package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.application.logic.cardnumber.CardNumber;
import io.github.ygojson.application.logic.cardnumber.CardNumberParser;
import io.github.ygojson.application.logic.mapper.MappingException;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.definition.localization.Region;

/**
 * Helper class to do the language mapping.
 */
abstract class AbstractLanguageHandler implements LanguageHandler {

	private final List<String> prefixProperties;
	private final List<Region> possibleRegions;

	/**
	 * Default constructor.
	 *
	 * @param prefixProperties the prefix properties.
	 * @param possibleRegions the possible regions for the mapping of set-codes.
	 */
	protected AbstractLanguageHandler(
		List<String> prefixProperties,
		List<Region> possibleRegions
	) {
		this.prefixProperties = prefixProperties;
		this.possibleRegions = possibleRegions;
	}

	/**
	 * Constructor for a single prefix property.
	 *
	 * @param prefixProperty the prefix property.
	 * @param possibleRegions the list of possible regions.
	 */
	protected AbstractLanguageHandler(
		final String prefixProperty,
		final List<Region> possibleRegions
	) {
		this(List.of(prefixProperty), possibleRegions);
	}

	/**
	 * Assigns the setcode value to the entity.
	 *
	 * @param entity the entity to modify.
	 * @param value the value to set.
	 */
	protected abstract void setSetCode(
		final SetEntity entity,
		final String value
	);

	/**
	 * Assigns the setcode_alt value to the entity.
	 *
	 * @param entity the entity to modify.
	 * @param value the value to set.
	 */
	protected abstract void setSetCodeAlt(
		final SetEntity entity,
		final String value
	);

	/**
	 * {@inheritDoc}
	 */
	public final void addLanguagePropertiesToSetEntity(
		final SetEntity entity,
		final Map<String, YugipediaProperty> properties
	) {
		final List<String> setCodes = toSetCodes(properties);
		switch (setCodes.size()) {
			case 0:
				setSetCode(entity, null);
				setSetCodeAlt(entity, null);
				break;
			case 1:
				setSetCode(entity, setCodes.getFirst());
				break;
			case 2:
				setSetCode(entity, setCodes.get(0));
				setSetCodeAlt(entity, setCodes.get(1));
				break;
			default:
				throw new MappingException(
					"Cannot set " + setCodes.size() + " set-codes"
				);
		}
	}

	/**
	 * Converts the properties to the list of set codes.
	 *
	 * @param properties the properties.
	 * @return list of set-codes.
	 */
	private List<String> toSetCodes(
		final Map<String, YugipediaProperty> properties
	) {
		return getPrefixProperties(properties)
			.map(prefix -> CardNumberParser.parseCardNumber(prefix, possibleRegions))
			.filter(Objects::nonNull)
			.map(CardNumber::setCode)
			.distinct()
			.toList();
	}

	/**
	 * Gets the prefix properties as a stream from the whole property map.
	 *
	 * @param properties property map.
	 * @return the prefix properties.
	 */
	private Stream<String> getPrefixProperties(
		final Map<String, YugipediaProperty> properties
	) {
		return prefixProperties
			.stream()
			.map(prefix -> (YugipediaProperty.TextProp) properties.get(prefix))
			.filter(Objects::nonNull)
			.map(YugipediaProperty.TextProp::value);
	}
}
