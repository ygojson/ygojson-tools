package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.definition.localization.Region;

class KoLanguageHandler extends AbstractLanguageHandler {

	private static final List<Region> possibleRegions = List.of(
		Region.KR,
		Region.K
	);

	public KoLanguageHandler() {
		super("kr_prefix", possibleRegions);
	}

	@Override
	protected void setSetCode(SetEntity entity, String value) {
		entity.ko.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(SetEntity entity, String value) {
		entity.ko.setCodeAlt = value;
	}
}
