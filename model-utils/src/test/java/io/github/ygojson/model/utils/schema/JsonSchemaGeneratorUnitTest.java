package io.github.ygojson.model.utils.schema;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.model.utils.test.RegionTestModel;

class JsonSchemaGeneratorUnitTest {

	private static JsonSchemaGenerator TEST_GENERATOR;

	@BeforeAll
	static void beforeAll() {
		TEST_GENERATOR = JsonSchemaGenerator.createInstance();
	}

	@Test
	void given_methodAnnotateds_when_generateInline_then_schemaContainsDescriptions() {
		// given
		final Class<?> type = MethodAnnotatedClass.class;
		// when
		final ObjectNode schema = TEST_GENERATOR.generateWithDefs(type, "test");
		// then
		SoftAssertions.assertSoftly(softly -> {
			softly
				.assertThat(getDescription(schema))
				.isEqualTo("My class description");
			softly
				.assertThat(getDescription(schema.get("properties").get("test")))
				.isEqualTo("My property description");
		});
	}
	@Test
	@Disabled("should check a model that contains the region enum as we don't support any longer any class")
	void give_regionTestModel_when_generateSchemaInline_then_schemaContainsEnums() {
		// given
		final Class<?> type = RegionTestModel.class;
		// when
		final ObjectNode schema = TEST_GENERATOR.generateWithDefs(type, "test");
		// then
		final List<String> schemaEnumValues = new ArrayList<>();
		schema
			.get("properties")
			.get("regionCode")
			.get("enum")
			.iterator()
			.forEachRemaining(node -> schemaEnumValues.add(node.asText()));
		final String[] expectedEnumValues = Arrays
			.stream(Region.values())
			.map(Region::value)
			.toArray(String[]::new);

		assertThat(schemaEnumValues).containsExactlyInAnyOrder(expectedEnumValues);
	}

	private String getDescription(final JsonNode node) {
		return node.get("description").textValue();
	}

	@JsonClassDescription("My class description")
	private static class MethodAnnotatedClass {

		private String test;

		@JsonPropertyDescription("My property description")
		@JsonProperty("test")
		public String getTest() {
			return test;
		}
	}
}
