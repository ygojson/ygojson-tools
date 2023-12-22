package io.github.ygojson.tools.dataprovider.impl.yugipedia.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class IntegerOrUndefinedUnitTest {

	@Test
	void given_nullValue_when_instanceCreation_thenThrows() {
		// given
		final String value = null;
		// when
		ThrowableAssert.ThrowingCallable throwingCallable = () ->
			new IntegerOrUndefined(value);
		// then
		assertThatThrownBy(throwingCallable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_undefinedString_when_isUndefined_thenReturnTrue() {
		// given
		IntegerOrUndefined integerOrUndefined = new IntegerOrUndefined("?");
		// when
		final boolean isUndefined = integerOrUndefined.isUndefined();
		// then
		assertThat(isUndefined).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = { "1", "2", "3", "100" })
	void given_valueString_when_isUndefined_thenReturnFalse(final String value) {
		// given
		IntegerOrUndefined integerOrUndefined = new IntegerOrUndefined(value);
		// when
		final boolean isUndefined = integerOrUndefined.isUndefined();
		// then
		assertThat(isUndefined).isFalse();
	}

	@Test
	void given_nonParsableString_when_asInteger_thenThrows() {
		// given
		IntegerOrUndefined integerOrUndefined = new IntegerOrUndefined("NaN");
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			integerOrUndefined.asInteger();
		// then
		assertThatThrownBy(throwingCallable)
			.isInstanceOf(NumberFormatException.class);
	}
}
