package io.github.ygojson.tools.data.impl;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.index.IndexType;
import org.dizitart.no2.repository.ObjectRepository;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.data.SetRepository;

public class SetRepositoryImpl extends AbstractNitriteRepository<Set> implements SetRepository {

	public SetRepositoryImpl(Nitrite nitriteDb) {
		super(nitriteDb);
	}

	@Override
	protected ObjectRepository<Set> getObjectRepository(Nitrite nitriteDb) {
		return nitriteDb.getRepository(Set.class);
	}

	@Override
	protected void createIndexes(final ObjectRepository<Set> objectRepository) {
		// unique indexes
		objectRepository.createIndex("id", "name");
		// full text indexes
		objectRepository.createIndex("name", IndexType.FULL_TEXT);

	}
}
