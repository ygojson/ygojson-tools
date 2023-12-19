package io.github.ygojson.model.utils.schema;

import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;

class YgoJsonOptionPreset {

	public static final OptionPreset YGOJSON_INLINE = new OptionPreset(
		// same as OptionPreset.PLAIN_JSON
		Option.SCHEMA_VERSION_INDICATOR,
		Option.ADDITIONAL_FIXED_TYPES,
		Option.STANDARD_FORMATS,
		Option.FLATTENED_ENUMS,
		Option.FLATTENED_OPTIONALS,
		Option.FLATTENED_SUPPLIERS,
		Option.VALUES_FROM_CONSTANT_FIELDS,
		Option.PUBLIC_NONSTATIC_FIELDS,
		Option.NONPUBLIC_NONSTATIC_FIELDS_WITH_GETTERS,
		Option.NONPUBLIC_NONSTATIC_FIELDS_WITHOUT_GETTERS,
		Option.ALLOF_CLEANUP_AT_THE_END,
		// ygojson specific
		// - methods for manually unwrapped container data
		Option.NONSTATIC_NONVOID_NONGETTER_METHODS,
		// - previous methods named as properties instead of method naming
		Option.FIELDS_DERIVED_FROM_ARGUMENTFREE_METHODS,
		// preset-specific
		// - inline schemas
		Option.INLINE_ALL_SCHEMAS
	);

	public static final OptionPreset YGOJSON_WITH_DEFS = new OptionPreset(
		// same as OptionPreset.PLAIN_JSON
		Option.SCHEMA_VERSION_INDICATOR,
		Option.ADDITIONAL_FIXED_TYPES,
		Option.STANDARD_FORMATS,
		Option.FLATTENED_ENUMS,
		Option.FLATTENED_OPTIONALS,
		Option.FLATTENED_SUPPLIERS,
		Option.VALUES_FROM_CONSTANT_FIELDS,
		Option.PUBLIC_NONSTATIC_FIELDS,
		Option.NONPUBLIC_NONSTATIC_FIELDS_WITH_GETTERS,
		Option.NONPUBLIC_NONSTATIC_FIELDS_WITHOUT_GETTERS,
		Option.ALLOF_CLEANUP_AT_THE_END,
		// ygojson specific
		// - methods for manually unwrapped container data
		Option.NONSTATIC_NONVOID_NONGETTER_METHODS,
		// - previous methods named as properties instead of method naming
		Option.FIELDS_DERIVED_FROM_ARGUMENTFREE_METHODS,
		// preset-specific
		// - definition for all objects
		Option.DEFINITIONS_FOR_ALL_OBJECTS,
		// - maps use additional properties to avoid definitions
		Option.MAP_VALUES_AS_ADDITIONAL_PROPERTIES
	);
}
