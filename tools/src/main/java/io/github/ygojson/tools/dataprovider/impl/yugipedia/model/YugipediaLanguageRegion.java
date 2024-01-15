package io.github.ygojson.tools.dataprovider.impl.yugipedia.model;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.MarkupString;

/**
 * Enum to simplify the Yugipedia language->region mapping.
 */
public enum YugipediaLanguageRegion {
	EN(
		CardTable2::en_sets,
		prefixesGetters(InfoboxSet::prefix, InfoboxSet::en_prefix),
		Language.EN,
		Region.EN
	),
	NA(
		CardTable2::na_sets,
		prefixGetter(InfoboxSet::na_prefix),
		Language.EN,
		Region.EN
	),
	EU(
		CardTable2::eu_sets,
		prefixGetter(InfoboxSet::eu_prefix),
		Language.EN,
		Region.EN,
		Region.E
	),
	AU(
		CardTable2::au_sets,
		any -> null, // AU does not have sets
		Language.EN,
		Region.EN,
		Region.A
	),
	AE(
		CardTable2::ae_sets,
		prefixGetter(InfoboxSet::ae_prefix),
		Language.EN,
		Region.AE
	),
	DE(
		CardTable2::de_sets,
		prefixGetter(InfoboxSet::de_prefix),
		Language.DE,
		Region.DE,
		Region.G
	),
	ES(
		CardTable2::sp_sets,
		prefixGetter(InfoboxSet::sp_prefix),
		Language.ES,
		Region.SP,
		Region.S
	),
	FR(
		CardTable2::fr_sets,
		prefixGetter(InfoboxSet::fr_prefix),
		Language.FR,
		Region.FR,
		Region.F
	),
	FC(
		CardTable2::fc_sets,
		prefixGetter(InfoboxSet::fc_prefix),
		Language.FR,
		Region.FR,
		Region.C
	),
	IT(
		CardTable2::it_sets,
		prefixGetter(InfoboxSet::it_prefix),
		Language.IT,
		Region.IT,
		Region.I
	),
	JA(
		CardTable2::ja_sets,
		prefixGetter(InfoboxSet::ja_prefix),
		Language.JA,
		Region.JA
	),
	JP(
		CardTable2::jp_sets,
		prefixGetter(InfoboxSet::jp_prefix),
		Language.JA,
		Region.JP
	),
	KO(
		CardTable2::kr_sets,
		prefixGetter(InfoboxSet::kr_prefix),
		Language.KO,
		Region.KR,
		Region.K
	),
	PT(
		CardTable2::pt_sets,
		prefixGetter(InfoboxSet::pt_prefix),
		Language.PT,
		Region.PT,
		Region.P
	),
	ZH_HANS(
		CardTable2::sc_sets,
		prefixGetter(InfoboxSet::sc_prefix),
		Language.ZH_HANS,
		Region.SC
	),
	ZH_HANT(
		CardTable2::tc_sets,
		prefixGetter(InfoboxSet::tc_prefix),
		Language.ZH_HANT,
		Region.TC
	);

	private final Function<CardTable2, List<MarkupString>> printsGetter;
	private final Function<InfoboxSet, List<String>> setPrefixesGetters;
	private final Language language;
	private final Region[] acceptedRegions;

	YugipediaLanguageRegion(
		Function<CardTable2, List<MarkupString>> printsGetter,
		Function<InfoboxSet, List<String>> setPrefixesGetters,
		Language language,
		Region... possibleRegions
	) {
		this.printsGetter = printsGetter;
		this.setPrefixesGetters = setPrefixesGetters;
		this.language = language;
		this.acceptedRegions = possibleRegions;
	}

	public List<MarkupString> getCardTable2Prints(final CardTable2 cardTable2) {
		return printsGetter.apply(cardTable2);
	}

	public List<String> getInfoboxSetPrefixes(final InfoboxSet infoboxSet) {
		return setPrefixesGetters.apply(infoboxSet);
	}

	public Language getLanguage() {
		return language;
	}

	public Region[] getAcceptedRegions() {
		return acceptedRegions;
	}

	private static Function<InfoboxSet, List<String>> prefixGetter(
		final Function<InfoboxSet, String> prefixGetter
	) {
		return infoboxSet -> {
			final String prefix = prefixGetter.apply(infoboxSet);
			return prefix == null ? null : List.of(prefix);
		};
	}

	private static Function<InfoboxSet, List<String>> prefixesGetters(
		final Function<InfoboxSet, String>... prefixGetters
	) {
		return infoboxSet -> {
			final List<String> prefixes = Stream
				.of(prefixGetters)
				.map(getter -> getter.apply(infoboxSet))
				.filter(Objects::nonNull)
				.toList();
			return prefixes.isEmpty() ? null : prefixes;
		};
	}
}
