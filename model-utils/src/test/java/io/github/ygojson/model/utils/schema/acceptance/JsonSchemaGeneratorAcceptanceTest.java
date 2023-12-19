package io.github.ygojson.model.utils.schema.acceptance;

import java.io.IOException;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.ygojson.model.utils.schema.JsonSchemaGenerator;

class JsonSchemaGeneratorAcceptanceTest {

	private static JsonSchemaGenerator generator;

	@BeforeEach
	void beforeEach() {
		// for the acceptance test, ensure that changes in documentation
		// lines does not break it, but model changes does
		generator = JsonSchemaGenerator.createInstance(false);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.model.utils.test.ModelTestData#getMainModels"
	)
	void testInlineDataSchemas(final Class<?> type) throws IOException {
		Approvals.verify(
			generator.generateInline(type, type.getName()).toPrettyString(),
			Approvals.NAMES
				.withParameters(type.getSimpleName())
				.forFile()
				.withExtension(".json")
		);
	}

	@ParameterizedTest
	@MethodSource(
		"io.github.ygojson.model.utils.test.ModelTestData#getMainModels"
	)
	void testWithDefsDataSchemas(final Class<?> type) throws IOException {
		Approvals.verify(
			generator.generateWithDefs(type, type.getName()).toPrettyString(),
			Approvals.NAMES
				.withParameters(type.getSimpleName())
				.forFile()
				.withExtension(".json")
		);
	}
}
