package io.github.ygojson.tools.dataprovider.test;

import java.util.UUID;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.Identifiers;
import io.github.ygojson.tools.dataprovider.domain.uuid.YgojsonIDGenerator;

public class YgojsonIDTestData {

	/**
	 * Expected ID for a card with the {@link #updateCardWithTestData(Card)}
	 */
	public static final YgojsonIDGenerator.YgojsonID TEST_CARD_ID =
		new YgojsonIDGenerator.YgojsonID(
			UUID.fromString("fb048a4e-4e02-5005-be71-dbe1a54c906b"),
			false
		);
	/**
	 * Expected ID for a set with the {@link #updateSetWithTestData(Set)}
	 */
	public static final YgojsonIDGenerator.YgojsonID TEST_SET_ID =
		new YgojsonIDGenerator.YgojsonID(
			UUID.fromString("08ebc888-cdda-55fb-b5e9-af5363f6cb25"),
			false
		);

	/**
	 * Expected ID for a print with the {@link #updatePrintWithTestData(Print)}
	 */
	public static final YgojsonIDGenerator.YgojsonID TEST_PRINT_ID =
		new YgojsonIDGenerator.YgojsonID(
			UUID.fromString("d1494603-370b-5ad0-a9b0-d55b39471a15"),
			false
		);

	private static final Long TEST_KONAMI_ID = 12345L;
	private static final Long TEST_PASSWORD = 12345678L;

	private static final String SET_NAME = "Name of the set";
	private static final String SET_CODE = "SET";

	private static final String PRINT_CODE = "SET-EN001";
	private static final UUID PRINT_CARD_ID = UUID.fromString(
		"fb048a4e-4e02-5005-be71-dbe1a54c906b"
	);
	private static final UUID PRINT_SET_ID = UUID.fromString(
		"08ebc888-cdda-55fb-b5e9-af5363f6cb25"
	);

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
	}

	public static final void updatePrintWithTestData(final Print print) {
		print.setPrintCode(PRINT_CODE);
		print.setCardId(PRINT_CARD_ID);
		print.setSetId(PRINT_SET_ID);
	}

	private YgojsonIDTestData() {
		// no instantiation
	}
}
