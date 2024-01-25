package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class TimestampUnitTest {

	@Test
	void given_nowTimestamp_when_toString_then_correct() {
		// given
		final Timestamp timestamp = Timestamp.ofNow();
		// when
		final String actual = timestamp.toString();
		// then
		final String expected = "now";
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void given_timeZonedTimestamp_when_toString_then_correct() {
		// given
		final Timestamp timestamp = Timestamp.of(
			ZonedDateTime.of(2022, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
		);
		// when
		final String actual = timestamp.toString();
		// then
		final String expected = "2022-01-01T00:00:00Z";
		assertThat(actual).isEqualTo(expected);
	}
}
