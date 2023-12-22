package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import org.mapstruct.Mapper;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.IntegerOrUndefined;

@Mapper
public abstract class IntegerOrUndefinedMapper {

	public abstract IntegerOrUndefined map(final String value);

	public Integer mapToInt(final IntegerOrUndefined value) {
		if (value == null) {
			return null;
		}
		return value.asInteger();
	}

	/**
	 * Maps the value to its undefined state.
	 *
	 * @param value the actual value or {@code null}.
	 *
	 * @return the undefined state; if {@code null} returns {@code false}.
	 */
	public boolean mapToUndefined(final IntegerOrUndefined value) {
		if (value == null) {
			return false;
		}
		return value.isUndefined();
	}
}
