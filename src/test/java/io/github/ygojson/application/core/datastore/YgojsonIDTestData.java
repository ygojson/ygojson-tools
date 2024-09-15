package io.github.ygojson.application.core.datastore;

import java.util.UUID;

import io.github.ygojson.application.core.datastore.db.card.RawCard;
import io.github.ygojson.application.core.datastore.db.card.RawCardIdentifiers;

class YgojsonIDTestData {

	/**
	 * Expected ID for a card with the {@link #updateCardWithTestData(RawCard)}
	 */
	public static final UUID TEST_CARD_ID = UUID.fromString(
		"fb048a4e-4e02-5005-be71-dbe1a54c906b"
	);

	private static final Long TEST_KONAMI_ID = 12345L;
	private static final Long TEST_PASSWORD = 12345678L;

	public static final void updateCardWithTestData(final RawCard card) {
		if (card.identifiers == null) {
			card.identifiers = new RawCardIdentifiers();
		}
		card.identifiers.konamiId = TEST_KONAMI_ID;
		card.identifiers.password = TEST_PASSWORD;
	}

	private YgojsonIDTestData() {
		// no instantiation
	}
}
