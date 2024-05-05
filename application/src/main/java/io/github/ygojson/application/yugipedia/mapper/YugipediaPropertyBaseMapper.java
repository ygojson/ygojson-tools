package io.github.ygojson.application.yugipedia.mapper;

import java.util.List;

import org.mapstruct.Named;

import io.github.ygojson.application.logic.mapper.BaseCardMapper;
import io.github.ygojson.application.yugipedia.YugipediaException;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaPropertyBaseMapper extends BaseCardMapper {

	@Named("maybeUndefinedIntegerProperty")
	protected Integer toUndefinedInteger(final YugipediaProperty prop) {
		return toMaybeUndefinedLong(toString(prop)).intValue();
	}

	/**
	 * Convert a yugipedia property into a string.
	 *
	 * @param prop property
	 *
	 * @return string
	 * @throws YugipediaException if the property type is not supported as a String
	 */
	protected String toString(final YugipediaProperty prop) {
		return switch (prop) {
			case YugipediaProperty.TextProp text -> text.value();
			case YugipediaProperty.ListProp list -> String.join(", ", list.value());
			default -> throw new YugipediaException(
				"Unsupported property type: " + prop.getClass()
			);
		};
	}

	/**
	 * Convert a yugipedia property into a list of strings.
	 *
	 * @param prop property to convert
	 * @return list of strings
	 * @throws YugipediaException if the property type is not supported as a String
	 */
	protected List<String> toList(final YugipediaProperty prop) {
		return switch (prop) {
			case YugipediaProperty.TextProp text -> List.of(text.value());
			case YugipediaProperty.ListProp list -> list.value();
			default -> throw new YugipediaException(
				"Unsupported property type: " + prop.getClass()
			);
		};
	}
}
