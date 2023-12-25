package io.github.ygojson.model.utils.schema;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JsonSchemaGeneratorUnitTest {

	private static JsonSchemaGenerator TEST_GENERATOR;

	@BeforeAll
	static void beforeAll() {
		TEST_GENERATOR = JsonSchemaGenerator.createInstance();
	}

	@Test
	void given_methodAnnotated_when_generateInline_then_schemaContainsDescriptions() {
		// given
		final Class<?> type = MethodAnnotatedClass.class;
		// when
		final ObjectNode schema = TEST_GENERATOR.generateInline(type, "test");
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
	void given_childAnnotation_when_generateInline_then_schemaDoesNotContainAllOf() {
		// given
		final Class<?> type = ParentClass.class;
		// when
		final ObjectNode schema = TEST_GENERATOR.generateInline(type, "test");
		// then
		assertThat(schema.toPrettyString()).doesNotContain("allOf");
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

	@JsonClassDescription("Parent class description")
	private static class ParentClass {

		private ChildClass child;

		@JsonPropertyDescription("Child method description")
		@JsonProperty("child")
		public ChildClass getChild() {
			return child;
		}
	}

	@JsonClassDescription("Child class description")
	private static class ChildClass {

		private String test;

		@JsonPropertyDescription("Test description")
		@JsonProperty("test")
		public String getTest() {
			return test;
		}
	}
}
