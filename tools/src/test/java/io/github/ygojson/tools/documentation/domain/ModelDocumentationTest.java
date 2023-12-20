package io.github.ygojson.tools.documentation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.tools.common.YgoJsonToolException;
import io.github.ygojson.tools.test.TestFilesystem;

class ModelDocumentationTest {

	private static final Logger log = LoggerFactory.getLogger(
		ModelDocumentationTest.class
	);

	private static ObjectMapper OBJECT_MAPPER;
	private static TestFilesystem TEST_FS;

	@BeforeAll
	static void beforeAll() {
		// use an in-memory FS to avoid writing to disk and speed up tests
		TEST_FS = TestFilesystem.init();
		OBJECT_MAPPER = new ObjectMapper();
	}

	@AfterAll
	static void afterAll() {
		OBJECT_MAPPER = null;
		TEST_FS.close();
	}

	@AfterEach
	void afterEach() {
		TEST_FS.cleanup();
	}

	@Test
	void given_schemaPrefixAlreadySet_when_setAgain_then_throwIllegalStateException() {
		// given
		final ModelDocumentation modelDoc = new ModelDocumentation(
			SimpleModel.class,
			OBJECT_MAPPER
		);
		modelDoc.setSchemaPrefix("my-prefix");
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			modelDoc.setSchemaPrefix("should-not-be-set");
		// then
		assertThatThrownBy(throwingCallable)
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void given_schemaAlreadyExists_when_writeJsonSchemaToWithoutForce_then_throwException() {
		// given
		final ModelDocumentation modelDoc = new ModelDocumentation(
			SimpleModel.class,
			OBJECT_MAPPER
		);
		final Path doNotOverwriteFile = TEST_FS.createFileWithContent(
			"simplemodel.schema.json",
			"DO NOT OVERWRITE"
		);
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			modelDoc.writeJsonSchemaTo(TEST_FS.getTestRoot(), false);
		// then
		SoftAssertions.assertSoftly(softly -> {
			softly
				.assertThatThrownBy(throwingCallable)
				.isInstanceOf(YgoJsonToolException.class);
			softly
				.assertThat(doNotOverwriteFile)
				.content()
				.describedAs("previous file content")
				.isEqualTo("DO NOT OVERWRITE");
		});
	}

	@Test
	void given_schemaFilenameIsExistingDirectory_when_writeJsonSchemaToWithForce_then_throwException() {
		// given
		final ModelDocumentation modelDoc = new ModelDocumentation(
			SimpleModel.class,
			OBJECT_MAPPER
		);
		TEST_FS.createDir("simplemodel.schema.json");
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			modelDoc.writeJsonSchemaTo(TEST_FS.getTestRoot(), false);
		// then
		assertThatThrownBy(throwingCallable)
			.isInstanceOf(YgoJsonToolException.class);
	}

	@Test
	void given_simpleModelDoc_when_writeJsonSchemaTo_then_idIsCorrect()
		throws IOException {
		// given
		final ModelDocumentation modelDoc = new ModelDocumentation(
			SimpleModel.class,
			OBJECT_MAPPER
		);
		modelDoc.setSchemaPrefix("simple-model-id");
		// when
		final Path schemaPath = modelDoc.writeJsonSchemaTo(
			TEST_FS.getTestRoot(),
			true
		);
		final JsonNode schemaAsJsonNode = OBJECT_MAPPER.readTree(
			Files.readString(schemaPath)
		);
		// then
		assertThat(schemaAsJsonNode.get("$id").textValue())
			.isEqualTo("simple-model-id/simplemodel.schema.json");
	}

	@Test
	void given_simpleModelDic_when_writeJsonSchemaTo_then_modelSchemaIs2019_09()
		throws IOException {
		// given
		final ModelDocumentation modelDoc = new ModelDocumentation(
			SimpleModel.class,
			OBJECT_MAPPER
		);
		// when
		final Path schemaPath = modelDoc.writeJsonSchemaTo(
			TEST_FS.getTestRoot(),
			true
		);
		final JsonNode schemaAsJsonNode = OBJECT_MAPPER.readTree(
			Files.readString(schemaPath)
		);
		// then
		assertThat(schemaAsJsonNode.get("$schema").textValue())
			.isEqualTo("https://json-schema.org/draft/2019-09/schema");
	}

	@Test
	void given_mapModel_when_processed_then_noAsDefAndRefAdditionalProperties()
		throws IOException {
		// given
		final ModelDocumentation modelDoc = new ModelDocumentation(
			MapModel.class,
			OBJECT_MAPPER
		);
		// when
		final Path schemaPath = modelDoc.writeJsonSchemaTo(
			TEST_FS.getTestRoot(),
			true
		);
		final JsonNode schemaAsJsonNode = OBJECT_MAPPER.readTree(
			Files.readString(schemaPath)
		);
		// then
		// compare as ObjectNode to avoid order changes
		final JsonNode expectedSchema = OBJECT_MAPPER.readTree(
			MapModel.EXPECTED_JSON_SCHEMA
		);
		assertThat(schemaAsJsonNode).isEqualTo(expectedSchema);
	}

	private static class SimpleModel {

		@JsonProperty("value")
		public String value;
	}

	private static class MapModel {

		@JsonPropertyOrder({ "int_value", "string_value" })
		private record MapValue(
			@JsonProperty("int_value") int i,
			@JsonProperty("string_value") String s
		) {}

		@JsonProperty("map_value")
		public Map<String, MapModel.MapValue> map;

		public static final String EXPECTED_JSON_SCHEMA =
			"""
                {
                  "$id": "/mapmodel.schema.json",
                  "$schema": "https://json-schema.org/draft/2019-09/schema",
                  "type": "object",
                  "properties": {
                    "map_value": {
                      "additionalProperties": {
                        "$ref": "#/$defs/MapValue"
                      }
                    }
                  },
                  "$defs" : {
                    "MapValue" : {
                      "type": "object",
                      "properties": {
                        "int_value": {
                          "type": "integer"
                        },
                        "string_value": {
                          "type": "string"
                        }
                      }
                    }
                  }
                }
                """;
	}
}
