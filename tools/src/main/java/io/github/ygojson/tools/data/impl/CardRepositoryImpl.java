package io.github.ygojson.tools.data.impl;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.repository.ObjectRepository;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.tools.data.CardRepository;

public class CardRepositoryImpl extends AbstractNitriteRepository<Card> implements CardRepository {
	public CardRepositoryImpl(final Nitrite nitriteDb) {
		super(nitriteDb);
	}

	@Override
	protected ObjectRepository<Card> getObjectRepository(final Nitrite nitriteDb) {
		return nitriteDb.getRepository(Card.class);
	}

	@Override
	protected void createIndexes(final ObjectRepository<Card> objectRepository) {
		/*
		// unique identifierss
		objectRepository.createIndex(
			"id",
			"indentifiers.konamiId",
			"identifiers.password",
			"identifiers.passwordAlt",
			"identifiers.yugipediaPageId",
			"name"
		);
		// full-text identifiers
		objectRepository.createIndex("name", IndexType.FULL_TEXT);
		 */
	}
}
