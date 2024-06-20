package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.definition.localization.Region;

class PtLanguageHandler extends AbstractLanguageHandler {

	private static final List<Region> possibleRegions = List.of(
		Region.PT,
		Region.P
	);

	public PtLanguageHandler() {
		super("pt_prefix", possibleRegions);
	}

	@Override
	protected void setSetCode(SetEntity entity, String value) {
		entity.pt.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(SetEntity entity, String value) {
		entity.pt.setCodeAlt = value;
	}
}
