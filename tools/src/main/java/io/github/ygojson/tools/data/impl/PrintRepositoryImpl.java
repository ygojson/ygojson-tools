package io.github.ygojson.tools.data.impl;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.repository.ObjectRepository;

import io.github.ygojson.model.data.Print;
import io.github.ygojson.tools.data.PrintRepository;

public class PrintRepositoryImpl extends AbstractNitriteRepository<Print> implements PrintRepository {

	public PrintRepositoryImpl(Nitrite nitriteDb) {
		super(nitriteDb);
	}

	@Override
	protected ObjectRepository<Print> getObjectRepository(Nitrite nitriteDb) {
		return nitriteDb.getRepository(Print.class);
	}

	@Override
	protected void createIndexes(final ObjectRepository<Print> repository) {
		// unique indexes
		repository.createIndex("id", "printCode");
	}
}
