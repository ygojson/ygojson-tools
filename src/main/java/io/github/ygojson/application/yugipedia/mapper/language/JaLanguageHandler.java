package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.datastore.db.set.RawSet;
import io.github.ygojson.model.data.definition.localization.Region;

class JaLanguageHandler extends AbstractLanguageHandler {

	private static final List<Region> possibleRegions = List.of(
		Region.JP,
		Region.JA
	);

	public JaLanguageHandler() {
		super(List.of("jp_prefix", "ja_prefix"), possibleRegions);
	}

	@Override
	protected void setSetCode(RawSet entity, String value) {
		entity.ja.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(RawSet entity, String value) {
		entity.ja.setCodeAlt = value;
	}
}
