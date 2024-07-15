package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.core.db.card.CardLocalizedValues;
import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.definition.localization.Region;

class ZhHansLanguageHandler extends AbstractLanguageHandler {

	public ZhHansLanguageHandler() {
		super("sc_prefix", List.of(Region.SC));
	}

	@Override
	protected void setSetCode(SetEntity entity, String value) {
		entity.zhHans.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(SetEntity entity, String value) {
		entity.zhHans.setCodeAlt = value;
	}

	@Override
	protected CardLocalizedValues getLocalizedValuesToUpdate(CardEntity entity) {
		return entity.zhHans;
	}
}
