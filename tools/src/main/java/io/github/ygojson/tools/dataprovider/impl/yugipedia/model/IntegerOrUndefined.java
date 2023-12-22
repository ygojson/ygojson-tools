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

	/**
	 * Default constructor.
	 *
	 * @param value non-null integer or undefined value string.
	 */
	public IntegerOrUndefined(final String value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null");
		}
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
	 * @throws NumberFormatException if the value is not an integer nor undefined.
	 */
	public int asInteger() throws NumberFormatException {
		if (isUndefined()) {
			return 0;
		}
		return Integer.parseInt(value);
	}

	@Override
	public String toString() {
		return value;
	}
}
