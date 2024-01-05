package io.github.ygojson.tools.dataprovider.test;

import static org.instancio.Select.field;

import org.instancio.Instancio;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.MarkupString;

/**
 * Mother for {@link CardTable2} test data.
 */
public class CardTable2Mother {

	private CardTable2Mother() {
		// cannot be instantiated - mother class
	}

	/**
	 * Generates a random {@link CardTable2} with the given data fixed.
	 *
	 * @param name name of the card
	 * @param pendulumEffect pendulum effect
	 * @param lore lore
	 *
	 * @return a random {@link CardTable2} with fixed data.
	 */
	public static CardTable2 withMainCardText(
		final String name,
		final String pendulumEffect,
		final String lore
	) {
		return Instancio
			.of(CardTable2.class)
			.set(field("name"), name)
			.set(field("pendulum_effect"), pendulumEffect)
			.set(field("lore"), MarkupString.of(lore))
			.create();
	}
}
