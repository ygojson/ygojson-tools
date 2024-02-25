package io.github.ygojson.tools.dataprovider.impl.repository.nitrite;

import java.util.List;

import org.dizitart.no2.index.IndexType;
import org.dizitart.no2.repository.EntityDecorator;
import org.dizitart.no2.repository.EntityId;
import org.dizitart.no2.repository.EntityIndex;

import io.github.ygojson.model.data.Set;

class SetEntityDecorator implements EntityDecorator<SetEntityDecorator.SetWrapper> {

	private static String WRAPER_PREFIX = "set.";
	// FIXME: required because https://github.com/nitrite/nitrite-java/issues/912
	// FIXME: this should be not necessary if that is fixed
	protected record SetWrapper(Set set) {

	}

	static String getProperty(final String property) {
		return WRAPER_PREFIX + property;
	}

	@Override
	public Class<SetEntityDecorator.SetWrapper> getEntityType() {
		return SetEntityDecorator.SetWrapper.class;
	}

	@Override
	public EntityId getIdField() {
		return new EntityId(getProperty("id"));
	}

	@Override
	public List<EntityIndex> getIndexFields() {
		return List.of(
			new EntityIndex(IndexType.UNIQUE, getProperty("id")),
			new EntityIndex(IndexType.NON_UNIQUE, getProperty("name")),
			new EntityIndex(IndexType.NON_UNIQUE, getProperty("nameAlt")),
			new EntityIndex(IndexType.NON_UNIQUE, getProperty("setCode")),
			new EntityIndex(IndexType.NON_UNIQUE, getProperty("setCodeAlt"))
		);
	}
}
