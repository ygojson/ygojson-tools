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
		YugipediaProperty.SetsProp,
		YugipediaProperty.NumberProp {
	@JsonValue
	Object jsonValue();

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
	 * Number property.
	 *
	 * @param value
	 */
	record NumberProp(@JsonIgnore Number value) implements YugipediaProperty {
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
