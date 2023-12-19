package io.github.ygojson.tools.documentation.domain;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.ygojson.model.utils.schema.JsonSchemaGenerator;

class SchemaDoc {

	private static final String SCHEMA_SUFFIX = ".schema.json";
	private final ModelDocumentation modelDoc;
	private final String schemaPrefix;
	private JsonSchemaGenerator generator;

	private SchemaDoc(final ModelDocumentation modelDoc, final String schemaPrefix) {
		this.modelDoc = modelDoc;
		this.schemaPrefix = schemaPrefix;
	}

	public static final SchemaDoc forModel(
		final ModelDocumentation modelDoc,
		final String schemaPrefix
	) {
		return new SchemaDoc(
			modelDoc,
			schemaPrefix.endsWith("/") ? schemaPrefix : schemaPrefix + "/"
		);
	}

	public String getFullSchemaId() {
		return schemaPrefix + getSchemaFilename();
	}

	/**
	 * Generates the model schema.
	 *
	 * @return the model schema.
	 */
	public ObjectNode generateSchema() {
		return getGenerator()
			.generateWithDefs(modelDoc.getModelClass(), getFullSchemaId());
	}

	public String getSchemaFilename() {
		return modelDoc.getModelName() + SCHEMA_SUFFIX;
	}

	private JsonSchemaGenerator getGenerator() {
		if (generator == null) {
			generator = JsonSchemaGenerator.createInstance();
		}
		return generator;
	}

}
