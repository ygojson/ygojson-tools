package io.github.ygojson.tools.dataprovider.impl.repository.nitrite;

import io.github.ygojson.tools.dataprovider.domain.repository.set.SetEntity;
import org.dizitart.no2.index.IndexType;
import org.dizitart.no2.repository.EntityDecorator;
import org.dizitart.no2.repository.EntityId;
import org.dizitart.no2.repository.EntityIndex;

import java.util.List;

class SetEntityDecorator implements EntityDecorator<SetEntity> {

	@Override
	public Class<SetEntity> getEntityType() {
		return SetEntity.class;
	}

	@Override
	public EntityId getIdField() {
		return new EntityId("_uuid");
	}

	@Override
	public List<EntityIndex> getIndexFields() {
		return List.of(
			// FIXME: cannot be unique because null values are not allowed
			// FIXME: a custom index implementation can be provided (see https://github.com/orgs/nitrite/discussions/48)
			new EntityIndex(IndexType.NON_UNIQUE, "set.id"),
			new EntityIndex(IndexType.NON_UNIQUE, "set.name"),
			new EntityIndex(IndexType.NON_UNIQUE, "set.nameAlt"),
			new EntityIndex(IndexType.NON_UNIQUE, "set.setCode"),
			new EntityIndex(IndexType.NON_UNIQUE, "set.setCodeAlt")
		);
	}
}
