package io.github.ygojson.model.utils.data.definition.localization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.model.utils.serialization.JsonUtils;
import io.github.ygojson.model.utils.test.RegionTestModel;

public class RegionSerializationUnitTest {

	@Test
	void given_modelWithNoneRegion_when_serialize_then_printRegionIsEmpty()
		throws JsonProcessingException {
		// given
		final RegionTestModel RegionTestModel = new RegionTestModel(Region.NONE);
		// when
		final String serialized = JsonUtils
			.getObjectMapper()
			.writeValueAsString(RegionTestModel);
		// then
		assertThat(serialized).contains("\"regionCode\":\"\"");
	}

	@Test
	void given_stringWithEmptyRegion_when_deserialize_then_printRegionIsNone()
		throws JsonProcessingException {
		// given
		final String json = "{\"regionCode\":\"\"}";
		// when
		final RegionTestModel RegionTestModel = JsonUtils
			.getObjectMapper()
			.readValue(json, RegionTestModel.class);
		// then
		final RegionTestModel expected = new RegionTestModel(Region.NONE);
		assertThat(RegionTestModel).isEqualTo(expected);
	}

	@Test
	void given_stringWithNoRegion_when_deserialize_then_printRegionIsNull()
		throws JsonProcessingException {
		// given
		final String json = "{}";
		// when
		final RegionTestModel RegionTestModel = JsonUtils
			.getObjectMapper()
			.readValue(json, RegionTestModel.class);
		// then
		final RegionTestModel expected = new RegionTestModel(null);
		assertThat(RegionTestModel).isEqualTo(expected);
	}

	@ParameterizedTest
	@EnumSource(value = Region.class)
	void given_enumValueSerialized_when_deserialize_then_sameValue(
		final Region region
	) throws JsonProcessingException {
		// given
		final String json = JsonUtils.getObjectMapper().writeValueAsString(region);
		// when
		final Region deserialized = JsonUtils
			.getObjectMapper()
			.readValue(json, Region.class);
		// then
		assertThat(deserialized).isEqualTo(region);
	}
}
