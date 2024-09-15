package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

import io.github.ygojson.application.core.datastore.db.set.RawSet;
import io.github.ygojson.model.data.definition.localization.Region;

class ZhHansLanguageHandler extends AbstractLanguageHandler {

	public ZhHansLanguageHandler() {
		super("sc_prefix", List.of(Region.SC));
	}

	@Override
	protected void setSetCode(RawSet entity, String value) {
		entity.zhHans.setCode = value;
	}

	@Override
	protected void setSetCodeAlt(RawSet entity, String value) {
		entity.zhHans.setCodeAlt = value;
	}
}
