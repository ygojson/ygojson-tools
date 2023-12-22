package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import org.mapstruct.Mapper;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.IntegerOrUndefined;

@Mapper
public abstract class IntegerOrUndefinedMapper {

	public abstract IntegerOrUndefined map(final String value);

	public Integer mapToInt(final IntegerOrUndefined integerOrUndefined) {
		if (integerOrUndefined == null) {
			return null;
		}
		return integerOrUndefined.asInteger();
	}

	public boolean mapToUndefined(final IntegerOrUndefined integerOrUndefined) {
		if (integerOrUndefined == null) {
			return false;
		}
		return integerOrUndefined.isUndefined();
	}
}
