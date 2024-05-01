package io.github.ygojson.application.yugipedia.parser.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Interface representing a Yugipedia property after parsing.
 */
public sealed interface YugipediaProperty
	permits
		YugipediaProperty.TextProp,
		YugipediaProperty.ListProp,
		YugipediaProperty.SetsProp {
	@JsonValue
	Object jsonValue();

	/**
	 * Create a text property.
	 *
	 * @param value value from the property.
	 *
	 * @return the property.
	 */
	static YugipediaProperty text(String value) {
		return new TextProp(value);
	}

	/**
	 * Create a list property.
	 *
	 * @param value value from the property.
	 *
	 * @return the property.
	 */
	static YugipediaProperty list(List<String> value) {
		return new ListProp(value);
	}

	/**
	 * Create a set property.
	 *
	 * @param value value from the property.
	 *
	 * @return the property.s
	 */
	static YugipediaProperty sets(List<SetRow> value) {
		return new SetsProp(value);
	}

	/**
	 * Text property.
	 *
	 * @param value
	 */
	record TextProp(@JsonIgnore String value) implements YugipediaProperty {
		@Override
		public Object jsonValue() {
			return value;
		}
	}

	/**
	 * List property.
	 *
	 * @param list
	 */
	record ListProp(@JsonIgnore List<String> list) implements YugipediaProperty {
		@Override
		public Object jsonValue() {
			return list;
		}
	}

	/**
	 * SetRow property.
	 *
	 * @param rows
	 */
	record SetsProp(@JsonIgnore List<SetRow> rows) implements YugipediaProperty {
		@Override
		public Object jsonValue() {
			return rows;
		}
	}
}
