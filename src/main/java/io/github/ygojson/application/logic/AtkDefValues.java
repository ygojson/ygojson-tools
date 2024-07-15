package io.github.ygojson.application.logic;

/**
 * Represents the ATK/DEF values.
 *
 * @param value the value (can be {@code null} if not known).
 * @param undefined the undefined flag (can only be {@code true} or {@code null}).
 */
public record AtkDefValues(Integer value, Boolean undefined) {
	private static final Integer UNDEFINED_ACTUAL_VALUE = 0;

	public static AtkDefValues ofUndefined() {
		return new AtkDefValues(UNDEFINED_ACTUAL_VALUE, true);
	}

	public static AtkDefValues ofValue(final Integer value) {
		return new AtkDefValues(value, null);
	}
}
