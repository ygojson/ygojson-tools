package io.github.ygojson.model.utils;

import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilsTest {

	@Test
	void testSerializeUTCZonedDateTimeInYgoJsonFormat() throws Exception {
		// given
		final ZonedDateTime zonedDateTime = ZonedDateTime.of(
			2020,
			6,
			27,
			6, 0, 0, 0,
			ZoneOffset.UTC
		);
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(zonedDateTime);
		// then
		assertThat(value).isEqualTo("\"2020-06-27T06:00:00Z\"");
	}

}
