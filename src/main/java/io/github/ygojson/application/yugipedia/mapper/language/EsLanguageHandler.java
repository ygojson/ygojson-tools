package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.definition.localization.Region;

class EsLanguageHandler extends AbstractLanguageHandler {

	private static final List<Region> possibleRegions = List.of(
		Region.SP,
		Region.S
	);

	public EsLanguageHandler() {
		super("sp_prefix", possibleRegions);
	}

	@Override
	protected void setSetCode(SetEntity entity, String value) {
		entity.es.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(SetEntity entity, String value) {
		entity.es.setCodeAlt = value;
	}
}
