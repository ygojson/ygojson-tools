package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.definition.localization.Region;

class EnLanguageHandler extends AbstractLanguageHandler {

	private static final List<String> PREFIXES = List.of(
		"prefix",
		"en_prefix",
		"na_prefix",
		"eu_prefix",
		"oc_prefix",
		"ae_prefix"
	);

	private static final List<Region> possibleRegions = List.of(
		Region.EN,
		Region.E,
		Region.AE,
		Region.A
	);

	public EnLanguageHandler() {
		super(PREFIXES, possibleRegions);
	}

	@Override
	protected void setSetCode(SetEntity entity, String value) {
		entity.en.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(SetEntity entity, String value) {
		entity.en.setCodeAlt = value;
	}
}
