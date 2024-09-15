package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.datastore.db.set.RawSet;
import io.github.ygojson.model.data.definition.localization.Region;

class DeLanguageHandler extends AbstractLanguageHandler {

	private static final List<Region> possibleRegions = List.of(
		Region.DE,
		Region.G
	);

	public DeLanguageHandler() {
		super("de_prefix", possibleRegions);
	}

	@Override
	protected void setSetCode(RawSet entity, String value) {
		entity.de.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(RawSet entity, String value) {
		entity.de.setCodeAlt = value;
	}
}
