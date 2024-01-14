package io.github.ygojson.model.utils.schema;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.VersionInfo;

/**
 * Enumeration of our data model schemas.
 */
public enum DataModelSchema {
	VERSION_INFO("version-info.schema.yaml", VersionInfo.class),
	CARD("card.schema.yaml", Card.class),
	PRINT("print.schema.yaml", Print.class),
	SET("set.schema.yaml", Set.class);

	final String filename;
	final Class<?> modelClass;

	DataModelSchema(final String filename, final Class<?> modelClass) {
		this.filename = filename;
		this.modelClass = modelClass;
	}

	public String getName() {
		return modelClass.getSimpleName();
	}

	public String getModelClassName() {
		return modelClass.getName();
	}
}
