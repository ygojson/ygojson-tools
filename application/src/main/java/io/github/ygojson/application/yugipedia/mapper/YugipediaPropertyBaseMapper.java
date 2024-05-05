package io.github.ygojson.application.yugipedia.mapper;

import java.util.List;

import org.mapstruct.Named;

import io.github.ygojson.application.yugipedia.YugipediaException;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

class YugipediaPropertyBaseMapper {

	protected static final String TO_LOWER_CASE = "toLowerCase";

	@Named(TO_LOWER_CASE)
	protected String toLowerCase(final String value) {
		if (value == null) {
			return null;
		}
		return value.toLowerCase();
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
