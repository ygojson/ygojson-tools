package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import org.mapstruct.Mapper;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.IntegerOrUndefined;

@Mapper
public abstract class IntegerOrUndefinedMapper {

	public abstract IntegerOrUndefined map(final String value);

	public int mapToInt(final IntegerOrUndefined integerOrUndefined) {
		return integerOrUndefined.asInteger();
	}

	public boolean mapToUndefined(final IntegerOrUndefined integerOrUndefined) {
		return integerOrUndefined.isUndefined();
	}
}
