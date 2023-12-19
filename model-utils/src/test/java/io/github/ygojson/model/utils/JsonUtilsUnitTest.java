package io.github.ygojson.model.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JsonUtilsUnitTest {

	static Stream<Arguments> zonedDateTimes() {
		return Stream.of(
			Arguments.of(
				ZonedDateTime.of(2020, 6, 27, 6, 0, 0, 0, ZoneOffset.UTC),
				"\"2020-06-27T06:00:00Z\""
			),
			Arguments.of(
				ZonedDateTime.of(2020, 6, 27, 6, 1, 2, 3, ZoneOffset.UTC),
				"\"2020-06-27T06:01:02.000000003Z\""
			)
		);
	}

	@ParameterizedTest
	@MethodSource("zonedDateTimes")
	void given_ZonedDateTimeUTC_when_writeValueAsString_then_formattedAsISO8601(
		final ZonedDateTime zonedDateTime,
		final String expected
	) throws Exception {
		// given - zonedDateTime
		// when
		final String value = JsonUtils
			.getObjectMapper()
			.writeValueAsString(zonedDateTime);
		// then
		assertThat(value).isEqualTo(expected);
	}
}
