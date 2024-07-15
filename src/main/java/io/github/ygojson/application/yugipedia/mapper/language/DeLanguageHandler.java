package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.core.db.card.CardLocalizedValues;
import io.github.ygojson.application.core.db.set.SetEntity;
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
	protected void setSetCode(SetEntity entity, String value) {
		entity.de.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(SetEntity entity, String value) {
		entity.de.setCodeAlt = value;
	}

	@Override
	protected CardLocalizedValues getLocalizedValuesToUpdate(CardEntity entity) {
		return entity.de;
	}
}
