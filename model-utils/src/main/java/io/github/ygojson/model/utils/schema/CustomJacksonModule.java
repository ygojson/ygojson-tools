package io.github.ygojson.model.utils.schema;

import com.github.victools.jsonschema.generator.MemberScope;
import com.github.victools.jsonschema.generator.TypeScope;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;

class CustomJacksonModule extends JacksonModule {

	private boolean addDescription = true;

	CustomJacksonModule(final boolean addDescription) {
		// always include these properties
		super(
			JacksonOption.RESPECT_JSONPROPERTY_ORDER,
			JacksonOption.INCLUDE_ONLY_JSONPROPERTY_ANNOTATED_METHODS,
			JacksonOption.RESPECT_JSONPROPERTY_REQUIRED,
			JacksonOption.FLATTENED_ENUMS_FROM_JSONPROPERTY,
			JacksonOption.FLATTENED_ENUMS_FROM_JSONVALUE
		);
		this.addDescription = addDescription;
	}

	@Override
	protected String resolveDescription(MemberScope<?, ?> member) {
		if (addDescription) {
			return super.resolveDescription(member);
		}
		return null;
	}

	@Override
	protected String resolveDescriptionForType(TypeScope type) {
		if (addDescription) {
			return super.resolveDescriptionForType(type);
		}
		return null;
	}
}
