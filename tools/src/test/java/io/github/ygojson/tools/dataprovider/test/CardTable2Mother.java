package io.github.ygojson.tools.dataprovider.test;

import static org.instancio.Select.field;

import java.util.List;

import org.instancio.Instancio;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.MarkupString;

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

	public static CardTable2 withoutSets() {
		return Instancio
			.of(CardTable2.class)
			.ignore(field(CardTable2::en_sets))
			.ignore(field(CardTable2::na_sets))
			.ignore(field(CardTable2::eu_sets))
			.ignore(field(CardTable2::au_sets))
			.ignore(field(CardTable2::ae_sets))
			.ignore(field(CardTable2::de_sets))
			.ignore(field(CardTable2::sp_sets))
			.ignore(field(CardTable2::fr_sets))
			.ignore(field(CardTable2::fc_sets))
			.ignore(field(CardTable2::it_sets))
			.ignore(field(CardTable2::pt_sets))
			.ignore(field(CardTable2::ja_sets))
			.ignore(field(CardTable2::jp_sets))
			.ignore(field(CardTable2::kr_sets))
			.ignore(field(CardTable2::sc_sets))
			.ignore(field(CardTable2::tc_sets))
			.create();
	}

	/**
	 * Generates a random {@link CardTable2} with only english sets (all other sets are empty).
	 *
	 * @param enSets string representing the {@link CardTable2#en_sets()} field.
	 *
	 * @return a random {@link CardTable2} with fixed data.
	 */
	public static CardTable2 withOnlyEnSets(final String enSets) {
		return Instancio
			.of(CardTable2.class)
			.set(field(CardTable2::en_sets), List.of(MarkupString.of(enSets)))
			.ignore(field(CardTable2::na_sets))
			.ignore(field(CardTable2::eu_sets))
			.ignore(field(CardTable2::au_sets))
			.ignore(field(CardTable2::ae_sets))
			.ignore(field(CardTable2::de_sets))
			.ignore(field(CardTable2::sp_sets))
			.ignore(field(CardTable2::fr_sets))
			.ignore(field(CardTable2::fc_sets))
			.ignore(field(CardTable2::it_sets))
			.ignore(field(CardTable2::pt_sets))
			.ignore(field(CardTable2::ja_sets))
			.ignore(field(CardTable2::jp_sets))
			.ignore(field(CardTable2::kr_sets))
			.ignore(field(CardTable2::sc_sets))
			.ignore(field(CardTable2::tc_sets))
			.create();
	}

	/**
	 * Generates a random {@link CardTable2} with only spanish sets (all other sets are empty).
	 *
	 * @param spSets string representing the {@link CardTable2#sp_sets()} field.
	 *
	 * @return a random {@link CardTable2} with fixed data.
	 */
	public static CardTable2 withOnlySpSets(final String spSets) {
		return Instancio
			.of(CardTable2.class)
			.ignore(field(CardTable2::en_sets))
			.ignore(field(CardTable2::na_sets))
			.ignore(field(CardTable2::eu_sets))
			.ignore(field(CardTable2::au_sets))
			.ignore(field(CardTable2::ae_sets))
			.ignore(field(CardTable2::de_sets))
			.set(field(CardTable2::sp_sets), List.of(MarkupString.of(spSets)))
			.ignore(field(CardTable2::fr_sets))
			.ignore(field(CardTable2::fc_sets))
			.ignore(field(CardTable2::it_sets))
			.ignore(field(CardTable2::pt_sets))
			.ignore(field(CardTable2::ja_sets))
			.ignore(field(CardTable2::jp_sets))
			.ignore(field(CardTable2::kr_sets))
			.ignore(field(CardTable2::sc_sets))
			.ignore(field(CardTable2::tc_sets))
			.create();
	}

	/**
	 * Generates a random {@link CardTable2} with only japanese sets (all other sets are empty).
	 *
	 * @param jaSets string representing the {@link CardTable2#ja_sets()} field.
	 *
	 * @return a random {@link CardTable2} with fixed data.
	 */
	public static CardTable2 withOnlyJaSets(final String jaSets) {
		return Instancio
			.of(CardTable2.class)
			.ignore(field(CardTable2::en_sets))
			.ignore(field(CardTable2::na_sets))
			.ignore(field(CardTable2::eu_sets))
			.ignore(field(CardTable2::au_sets))
			.ignore(field(CardTable2::ae_sets))
			.ignore(field(CardTable2::de_sets))
			.ignore(field(CardTable2::sp_sets))
			.ignore(field(CardTable2::fr_sets))
			.ignore(field(CardTable2::fc_sets))
			.ignore(field(CardTable2::it_sets))
			.ignore(field(CardTable2::pt_sets))
			.set(field(CardTable2::ja_sets), List.of(MarkupString.of(jaSets)))
			.ignore(field(CardTable2::jp_sets))
			.ignore(field(CardTable2::kr_sets))
			.ignore(field(CardTable2::sc_sets))
			.ignore(field(CardTable2::tc_sets))
			.create();
	}
}
