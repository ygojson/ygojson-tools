package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.core.db.card.CardLocalizedValues;
import io.github.ygojson.application.core.db.set.SetEntity;
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
	protected void setSetCode(SetEntity entity, String value) {
		entity.ja.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(SetEntity entity, String value) {
		entity.ja.setCodeAlt = value;
	}

	@Override
	protected CardLocalizedValues getLocalizedValuesToUpdate(CardEntity entity) {
		return entity.ja;
	}
}
