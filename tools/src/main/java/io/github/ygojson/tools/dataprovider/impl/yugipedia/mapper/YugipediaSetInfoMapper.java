package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.SetInfo;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.utils.data.utils.LocalizationUtils;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.MarkupStringMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardNumber;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.YugipediaLanguageRegion;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;

@Mapper
public abstract class YugipediaSetInfoMapper {

	private static final Function<InfoboxSet, String> NO_ALT_NAME = infoboxSet ->
		null;

	protected MarkupStringMapper markupStringMapper = Mappers.getMapper(
		MarkupStringMapper.class
	);

	protected CardNumberMapper cardNumberMapper = Mappers.getMapper(
		CardNumberMapper.class
	);

	@AfterMapping
	protected void updateMainSetInfo(
		final InfoboxSet infoboxSet,
		@MappingTarget final Set set
	) {
		final SetInfo setInfo = mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::en_name,
			InfoboxSet::en_name_2,
			YugipediaLanguageRegion.EN,
			YugipediaLanguageRegion.EU,
			YugipediaLanguageRegion.NA,
			YugipediaLanguageRegion.AU,
			YugipediaLanguageRegion.AE
		);
		LocalizationUtils.setAsMainLanguage(set, setInfo);
		// cleanup duplicated non-main info
		LocalizationUtils
			.getNonMainLanguages()
			.forEach(language -> {
				cleanupDuplicated(set, setInfo, language);
			});
	}

	private void cleanupDuplicated(
		Set set,
		SetInfo mainSetInfo,
		Language language
	) {
		// skip if main-set info is null
		if (mainSetInfo == null) {
			return;
		}
		final SetInfo localized = LocalizationUtils.getLocalizedData(language, set);
		if (localized != null) {
			// by now, only remove the setCode if it is the same as the set
			final String localizedSetCode = localized.getSetCode();
			if (
				localizedSetCode != null &&
				localizedSetCode.equals(mainSetInfo.getSetCode())
			) {
				localized.setSetCode(null);
			}
		}
	}

	@Named("esSetInfo")
	protected SetInfo mapEsSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::es_name,
			NO_ALT_NAME,
			YugipediaLanguageRegion.ES
		);
	}

	@Named("deSetInfo")
	protected SetInfo mapDeSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::de_name,
			NO_ALT_NAME,
			YugipediaLanguageRegion.DE
		);
	}

	@Named("frSetInfo")
	protected SetInfo mapFrSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::fr_name,
			NO_ALT_NAME,
			YugipediaLanguageRegion.FR,
			YugipediaLanguageRegion.FC
		);
	}

	@Named("itSetInfo")
	protected SetInfo mapItSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::it_name,
			NO_ALT_NAME,
			YugipediaLanguageRegion.IT
		);
	}

	@Named("jaSetInfo")
	protected SetInfo mapJaSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			model -> markupStringMapper.map(model.ja_name()),
			NO_ALT_NAME,
			YugipediaLanguageRegion.JP,
			YugipediaLanguageRegion.JA
		);
	}

	@Named("koSetInfo")
	protected SetInfo mapKoSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			model -> markupStringMapper.map(model.ko_name()),
			NO_ALT_NAME,
			YugipediaLanguageRegion.KO
		);
	}

	@Named("ptSetInfo")
	protected SetInfo mapPtSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::pt_name,
			NO_ALT_NAME,
			YugipediaLanguageRegion.PT
		);
	}

	@Named("zhHansSetInfo")
	protected SetInfo mapZhHansSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::sc_name,
			NO_ALT_NAME,
			YugipediaLanguageRegion.ZH_HANS
		);
	}

	@Named("zhHantSetInfo")
	protected SetInfo mapZhHantSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::tc_name,
			NO_ALT_NAME,
			YugipediaLanguageRegion.ZH_HANT
		);
	}

	private List<CardNumber> mapToCardNumberPrefixes(
		final InfoboxSet infoboxSet,
		final YugipediaLanguageRegion... langRegion
	) {
		if (langRegion.length == 1) {
			final YugipediaLanguageRegion region = langRegion[0];
			return mapToCardNumber(region.getInfoboxSetPrefixes(infoboxSet), region)
				.toList();
		}

		return Arrays
			.stream(langRegion)
			.flatMap(region ->
				mapToCardNumber(region.getInfoboxSetPrefixes(infoboxSet), region)
			)
			.toList();
	}

	private Stream<CardNumber> mapToCardNumber(
		final List<String> prefixes,
		final YugipediaLanguageRegion region
	) {
		if (prefixes == null) {
			return Stream.empty();
		}
		return prefixes
			.stream()
			.map(prefix -> cardNumberMapper.mapSetPrefixToCardNumber(prefix, region));
	}

	protected SetInfo mapToSetInfo(
		final InfoboxSet infoboxSet,
		final Function<InfoboxSet, String> nameGetter,
		final Function<InfoboxSet, String> nameAltGetter,
		final YugipediaLanguageRegion... langRegion
	) {
		final String name = nameGetter.apply(infoboxSet);
		final String nameAlt = nameAltGetter.apply(infoboxSet);
		final List<CardNumber> cardNumberPrefixes = mapToCardNumberPrefixes(
			infoboxSet,
			langRegion
		);
		if (name == null && cardNumberPrefixes.isEmpty()) {
			return null;
		}
		final SetInfo info = new SetInfo();
		info.setName(name);
		info.setNameAlt(nameAlt);
		updateSetCodes(cardNumberPrefixes, info);
		return info;
	}

	private List<String> mapToSetCodes(
		final List<CardNumber> cardNumberPrefixes
	) {
		if (cardNumberPrefixes == null) {
			return List.of();
		}
		return cardNumberPrefixes
			.stream()
			.map(CardNumber::setCode)
			.distinct()
			.toList();
	}

	private void updateSetCodes(
		final List<CardNumber> cardNumberPrefixes,
		final SetInfo setInfo
	) {
		final List<String> setCodes = mapToSetCodes(cardNumberPrefixes);
		switch (setCodes.size()) {
			case 0 -> {}
			case 1 -> setInfo.setSetCode(setCodes.getFirst());
			case 2 -> {
				setInfo.setSetCode(setCodes.getFirst());
				setInfo.setSetCodeAlt(setCodes.get(1));
			}
			default -> throw new IllegalStateException(
				"More than 2 set codes for set: " + setInfo
			);
		}
	}
}
