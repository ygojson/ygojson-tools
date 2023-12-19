package io.github.ygojson.model.utils.schema;

import java.util.Map;
import java.util.function.Function;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;

import io.github.ygojson.model.utils.JsonUtils;

public final class JsonSchemaGenerator {

	private final boolean addDescription;

	// cannot be instantiated directly
	private JsonSchemaGenerator(final boolean addDescription) {
		this.addDescription = addDescription;
	}

	/**
	 * Creates an instance of the json-schema generator.
	 *
	 * @return instance of the generator with descriptions.
	 */
	public static JsonSchemaGenerator createInstance() {
		return createInstance(true);
	}

	/**
	 * Creates an instance of the json-schema with or without descriptions.
	 *
	 * @param addDescription if {@code true}, the descriptions will be added;
	 *                          otherwise, the descriptions will not be added
	 * @return instance of the generator
	 */
	public static JsonSchemaGenerator createInstance(
		final boolean addDescription
	) {
		return new JsonSchemaGenerator(addDescription);
	}

	public ObjectNode generateInline(final Class<?> type, final String modelId) {
		return getGenerator(
			YgoJsonOptionPreset.YGOJSON_INLINE,
			scope -> idResolver(type, modelId, scope)
		)
			.generateSchema(type);
	}

	public ObjectNode generateWithDefs(
		final Class<?> type,
		final String modelId
	) {
		return getGenerator(
			YgoJsonOptionPreset.YGOJSON_WITH_DEFS,
			scope -> idResolver(type, modelId, scope)
		)
			.generateSchema(type);
	}

	// TODO: maybe cache the generator instance to re-use
	private SchemaGenerator getGenerator(
		final OptionPreset preset,
		final Function<TypeScope, String> idResolver
	) {
		final SchemaGeneratorConfigBuilder builder =
			new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, preset);
		// jackson & jakarta
		builder
			.withObjectMapper(JsonUtils.getObjectMapper())
			.with(new CustomJacksonModule(addDescription))
			.with(
				new JakartaValidationModule(
					JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS
				)
			);
		// types and fields customization
		builder.forTypesInGeneral().withIdResolver(idResolver::apply);
		// fix map definitions
		if (preset.isOptionEnabledByDefault(Option.DEFINITIONS_FOR_ALL_OBJECTS)) {
			builder
				.forFields()
				.withCustomDefinitionProvider(JsonSchemaGenerator::skipMapDefintions);
		}
		return new SchemaGenerator(builder.build());
	}

	private static String idResolver(
		final Class<?> modelType,
		final String modelName,
		final TypeScope scope
	) {
		return scope.getType().getErasedType() == modelType ? modelName : null;
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
