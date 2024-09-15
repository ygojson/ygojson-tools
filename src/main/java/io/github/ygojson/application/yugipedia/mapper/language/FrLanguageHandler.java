package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.datastore.db.set.RawSet;
import io.github.ygojson.model.data.definition.localization.Region;

class FrLanguageHandler extends AbstractLanguageHandler {

	private static final List<Region> possibleRegions = List.of(
		Region.FR,
		Region.F,
		Region.C
	);

	public FrLanguageHandler() {
		super(List.of("fr_prefix", "fc_prefix"), possibleRegions);
	}

	@Override
	protected void setSetCode(RawSet entity, String value) {
		entity.fr.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(RawSet entity, String value) {
		entity.fr.setCodeAlt = value;
	}
}
