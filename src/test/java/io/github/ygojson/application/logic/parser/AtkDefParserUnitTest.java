package io.github.ygojson.application.logic.parser;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AtkDefParserUnitTest {

	@Test
	void given_questionMarkValue_when_toMaybeUndefinedLong_then_returnUndefined() {
		// given
		final String value = "?";
		// when
		final Integer result = AtkDefParser.parse(value);
		final boolean isUndefined = AtkDefParser.isUndefined(result);
		// then
		assertSoftly(softly -> {
			softly.assertThat(result).isEqualTo(Integer.MIN_VALUE);
			softly.assertThat(isUndefined).isTrue();
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "null", "something", "???", "NaN" })
	void given_nonParsableValue_when_toMaybeUndefinedLong_then_returnNull(
		final String nullableValue
	) {
		// given - value
		// when
		final Integer result = AtkDefParser.parse(nullableValue);
		final boolean isUndefined = AtkDefParser.isUndefined(result);
		// then
		assertSoftly(softly -> {
			softly.assertThat(result).isNull();
			softly.assertThat(isUndefined).isFalse();
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "0", "1", "2", "100" })
	void given_parsableValue_when_toMaybeUndefinedLong_then_returnValue(
		final String parsableValue
	) {
		// given - value
		// when
		final Integer result = AtkDefParser.parse(parsableValue);
		final boolean isUndefined = AtkDefParser.isUndefined(result);
		// then
		assertSoftly(softly -> {
			softly.assertThat(result).isNotNull();
			softly.assertThat(isUndefined).isFalse();
		});
	}
}
