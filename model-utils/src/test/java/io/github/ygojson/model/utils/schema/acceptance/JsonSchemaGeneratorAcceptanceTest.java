package io.github.ygojson.model.utils.schema.acceptance;

import java.io.IOException;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.github.ygojson.model.utils.schema.DataModelSchema;
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
	@EnumSource(value = DataModelSchema.class)
	void testWithDefsDataSchemas(final DataModelSchema dataModel) throws IOException {
		Approvals.verify(
			generator.generateWithDefs(dataModel, dataModel.getModelClassName()).toPrettyString(),
			Approvals.NAMES
				.withParameters(dataModel.getName())
				.forFile()
				.withExtension(".json")
		);
	}
}
