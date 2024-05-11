package io.github.ygojson.model.utils.data.utils.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.VersionInfo;
import io.github.ygojson.model.utils.serialization.JsonUtils;

public class VersionInfoSerializationUnitTest {

	@Test
	void given_versionInfoEmpty_when_serialize_then_noVersionIsPresent()
		throws JsonProcessingException {
		// given
		final VersionInfo versionInfo = new VersionInfo();
		// when
		final String value = JsonUtils
			.getObjectMapper()
			.writeValueAsString(versionInfo);
		// then
		assertThat(value).isEqualTo("{}");
	}

	@Test
	void given_versionInfoWithEmptyVersion_when_serialize_then_noVersionIsPresent()
		throws JsonProcessingException {
		// given
		final VersionInfo versionInfo = new VersionInfo();
		versionInfo.setVersion("");
		// when
		final String value = JsonUtils
			.getObjectMapper()
			.writeValueAsString(versionInfo);
		// then
		assertThat(value).contains("{}");
	}
}
