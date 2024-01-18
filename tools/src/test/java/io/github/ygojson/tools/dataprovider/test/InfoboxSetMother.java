package io.github.ygojson.tools.dataprovider.test;

import static org.instancio.Select.field;

import org.instancio.Instancio;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;

/**
 * Mother for {@link InfoboxSet} test data.
 */
public class InfoboxSetMother {

	private InfoboxSetMother() {
		// cannot be instantiated - mother class
	}

	public static InfoboxSet withoutPrefixes() {
		return Instancio
			.of(InfoboxSet.class)
			.ignore(field(InfoboxSet::prefix))
			.ignore(field(InfoboxSet::en_prefix))
			.ignore(field(InfoboxSet::na_prefix))
			.ignore(field(InfoboxSet::eu_prefix))
			.ignore(field(InfoboxSet::ae_prefix))
			.ignore(field(InfoboxSet::oc_prefix))
			.ignore(field(InfoboxSet::de_prefix))
			.ignore(field(InfoboxSet::sp_prefix))
			.ignore(field(InfoboxSet::fr_prefix))
			.ignore(field(InfoboxSet::fc_prefix))
			.ignore(field(InfoboxSet::it_prefix))
			.ignore(field(InfoboxSet::pt_prefix))
			.ignore(field(InfoboxSet::ja_prefix))
			.ignore(field(InfoboxSet::jp_prefix))
			.ignore(field(InfoboxSet::kr_prefix))
			.ignore(field(InfoboxSet::sc_prefix))
			.ignore(field(InfoboxSet::tc_prefix))
			.create();
	}
}
