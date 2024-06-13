package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.definition.localization.Region;

class ZhHantLanguageHandler extends AbstractLanguageHandler {

	public ZhHantLanguageHandler() {
		super("tc_prefix", List.of(Region.TC));
	}

	@Override
	protected void setSetCode(SetEntity entity, String value) {
		entity.zhHant.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(SetEntity entity, String value) {
		entity.zhHant.setCodeAlt = value;
	}
}
