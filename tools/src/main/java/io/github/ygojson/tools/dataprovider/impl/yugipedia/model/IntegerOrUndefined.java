package io.github.ygojson.tools.dataprovider.impl.yugipedia.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Identifies an integer that can be undefined by matching '?'.
 */
@JsonSerialize(using = ToStringSerializer.class)
public class IntegerOrUndefined {

	/**
	 * Undefined value for the integer string (i.e., atk/def).
	 */
	private static final String UNDEFINED_VALUE = "?";

	private final String value;

	public IntegerOrUndefined(final String value) {
		this.value = value;
	}

	/**
	 * Returns {@code true} if the value is undefined;
	 * {@code false} otherwise.
	 *
	 * @return if the value represents an undefned.
	 */
	public boolean isUndefined() {
		return value.equals(UNDEFINED_VALUE);
	}

	/**
	 * Return as an integer.
	 *
	 * @return integer.
	 */
	public int asInteger() {
		if (isUndefined()) {
			return 0;
		}
		return Integer.valueOf(value);
	}

	@Override
	public String toString() {
		return value;
	}
}
