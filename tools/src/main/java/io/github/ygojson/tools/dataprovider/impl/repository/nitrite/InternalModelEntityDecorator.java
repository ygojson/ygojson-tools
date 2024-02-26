package io.github.ygojson.tools.dataprovider.impl.repository.nitrite;

import org.dizitart.no2.index.IndexType;
import org.dizitart.no2.repository.EntityDecorator;
import org.dizitart.no2.repository.EntityId;
import org.dizitart.no2.repository.EntityIndex;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class InternalModelEntityDecorator<T> implements EntityDecorator<T> {

	private final Set<String> indexedModelFields = new LinkedHashSet<>();
	private final Class<T> type;
	private final String modelField;

	InternalModelEntityDecorator(final Class<T> type, final String modelField) {
		this(type, modelField, List.of());
	}

	InternalModelEntityDecorator(final Class<T> type, final String modelField, final List<String> indexedModelFields) {
		this.type = type;
		this.modelField = modelField;
		this.indexedModelFields.addAll(indexedModelFields);
	}

	protected final String getModelField() {
		return modelField;
	}

	protected final String getModelId() {
		return modelField + ".id";
	}

	protected final String getModelProperty(final String property) {
		return modelField + "." + property;
	}

	@Override
	public final Class<T> getEntityType() {
		return type;
	}

	@Override
	public final EntityId getIdField() {
		return new EntityId("_uuid");
	}

	@Override
	public final List<EntityIndex> getIndexFields() {
		// FIXME: cannot be unique because null values are not allowed
		// FIXME: a custom index implementation can be provided (see https://github.com/orgs/nitrite/discussions/48)
		return indexedModelFields.stream()
			.map(field -> new EntityIndex(IndexType.NON_UNIQUE, getModelProperty(field)))
			.toList();
	}
}
