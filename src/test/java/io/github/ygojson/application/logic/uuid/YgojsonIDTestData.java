package io.github.ygojson.application.logic.uuid;

import java.util.UUID;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.Identifiers;

/**
 * @deprecated class to be tested will be substituted
 */
@Deprecated
class YgojsonIDTestData {

	/**
	 * Expected ID for a card with the {@link #updateCardWithTestData(Card)}
	 */
	public static final UUID TEST_CARD_ID = UUID.fromString(
		"fb048a4e-4e02-5005-be71-dbe1a54c906b"
	);
	/**
	 * Expected ID for a set with the {@link #updateSetWithTestData(Set)}
	 */
	public static final UUID TEST_SET_ID = UUID.fromString(
		"fa22361d-eb1b-5d7f-b565-55218af43b3d"
	);

	/**
	 * Expected ID for a print with the {@link #updatePrintWithTestData(Print)}
	 */
	public static final UUID TEST_PRINT_ID = UUID.fromString(
		"9b1a972e-fc21-5f90-92fb-d5b65af54dbc"
	);

	private static final Long TEST_KONAMI_ID = 12345L;
	private static final Long TEST_PASSWORD = 12345678L;

	private static final String SET_NAME = "Name of the set";
	private static final String SET_CODE = "SET";
	private static final String SET_PRINT_NUMBER_PREFIX = "SP";

	private static final String PRINT_CODE = "SET-EN001";
	private static final String PRINT_RARITY = "rare";

	public static final void updateCardWithTestData(final Card card) {
		if (card.getIdentifiers() == null) {
			card.setIdentifiers(new Identifiers());
		}
		card.getIdentifiers().setKonamiId(TEST_KONAMI_ID);
		card.getIdentifiers().setPassword(TEST_PASSWORD);
	}

	public static final void updateSetWithTestData(final Set set) {
		set.setName(SET_NAME);
		set.setSetCode(SET_CODE);
		set.setPrintNumberPrefix(SET_PRINT_NUMBER_PREFIX);
	}

	public static final void updatePrintWithTestData(final Print print) {
		print.setPrintCode(PRINT_CODE);
		print.setRarity(PRINT_RARITY);
	}

	private YgojsonIDTestData() {
		// no instantiation
	}
}
