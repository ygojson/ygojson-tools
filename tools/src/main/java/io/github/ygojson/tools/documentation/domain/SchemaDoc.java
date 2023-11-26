package io.github.ygojson.tools.documentation.domain;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class SchemaDoc {

	private static final String SCHEMA_SUFFIX = ".schema.json";
	private final ModelDocumentation modelDoc;
	private final String schemaPrefix;

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
		return createSchemaGenerator().generateSchema(modelDoc.getModelClass());
	}

	public String getSchemaFilename() {
		return modelDoc.getModelName() + SCHEMA_SUFFIX;
	}

	private SchemaGenerator createSchemaGenerator() {
		final SchemaGeneratorConfigBuilder builder =
			new SchemaGeneratorConfigBuilder(
				SchemaVersion.DRAFT_2019_09,
				OptionPreset.PLAIN_JSON
			);
		builder.withObjectMapper(modelDoc.getObjectMapper());
		// configure modules and default options
		builder
			.with(
				new JacksonModule(
					JacksonOption.RESPECT_JSONPROPERTY_ORDER,
					JacksonOption.RESPECT_JSONPROPERTY_REQUIRED,
					JacksonOption.FLATTENED_ENUMS_FROM_JSONPROPERTY
				)
			)
			.with(
				new JakartaValidationModule(
					JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS
				)
			)
			.with(Option.MAP_VALUES_AS_ADDITIONAL_PROPERTIES)
			.with(Option.DEFINITIONS_FOR_ALL_OBJECTS);
		// tweak the map defs
		builder
			.forFields()
			.withCustomDefinitionProvider(SchemaDoc::skipMapDefintions);
		// specific for this schema variables
		builder.forTypesInGeneral().withIdResolver(this::idResolver);
		// build the generator
		return new SchemaGenerator(builder.build());
	}

	private String idResolver(final TypeScope scope) {
		return scope.getType().getErasedType() == modelDoc.getModelClass()
			? getFullSchemaId()
			: null;
	}

	private static CustomPropertyDefinition skipMapDefintions(
		final FieldScope scope,
		final SchemaGenerationContext context
	) {
		if (scope.getType().isInstanceOf(Map.class)) {
			final ObjectNode node = context.getGeneratorConfig().createObjectNode();
			return new CustomPropertyDefinition(node);
		}
		return null;
	}
}
