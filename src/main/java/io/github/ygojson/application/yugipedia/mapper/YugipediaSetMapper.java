package io.github.ygojson.application.yugipedia.mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import io.github.ygojson.application.logic.mapper.CardNumber;
import io.github.ygojson.application.logic.mapper.CardNumberParser;
import io.github.ygojson.application.logic.mapper.MappingException;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.SetInfo;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.model.utils.LocalizationUtils;

/**
 * Mapper for the YGOJSON {@link Set} from {@link YugipediaProperty} map.
 */
// TODO: use CDI for injection as we will use it with quarkus
@Mapper(uses = YugipediaPropertyBaseMapper.class)
public abstract class YugipediaSetMapper {

	private static final SetInfo EMPTY = new SetInfo();

	protected enum LangSpec {
		EN(
			Language.EN,
			List.of(
				"prefix",
				"en_prefix",
				"na_prefix",
				"eu_prefix",
				"oc_prefix",
				"ae_prefix"
			),
			List.of(Region.EN, Region.E, Region.AE, Region.A)
		),
		DE(Language.DE, "de_prefix", List.of(Region.DE, Region.G)),
		ES(Language.ES, "sp_prefix", List.of(Region.SP, Region.S)),
		FR(
			Language.FR,
			List.of("fr_prefix", "fc_prefix"),
			List.of(Region.FR, Region.F, Region.C)
		),
		IT(Language.IT, "it_prefix", List.of(Region.IT, Region.I)),
		JA(Language.JA, List.of("jp_prefix", "ja_prefix"), List.of(Region.JA)),
		KO(Language.KO, "kr_prefix", List.of(Region.KR, Region.K)),
		PT(Language.PT, "pt_prefix", List.of(Region.PT, Region.P)),
		ZH_HANS(Language.ZH_HANS, "sc_prefix", List.of(Region.SC)),
		ZH_HANT(Language.ZH_HANT, "tc_prefix", List.of(Region.TC));

		private final Language language;
		private final List<String> prefixProperties;
		private final List<Region> possibleRegion;

		LangSpec(
			final Language language,
			final List<String> prefixProperties,
			final List<Region> possibleRegions
		) {
			this.language = language;
			this.prefixProperties = prefixProperties;
			this.possibleRegion = possibleRegions;
		}

		LangSpec(
			final Language language,
			final String prefixProperties,
			final List<Region> possibleRegions
		) {
			this(language, List.of(prefixProperties), possibleRegions);
		}

		public Stream<String> getPrefixProperties(
			final Map<String, YugipediaProperty> properties
		) {
			return prefixProperties
				.stream()
				.map(prefix -> (YugipediaProperty.TextProp) properties.get(prefix))
				.filter(Objects::nonNull)
				.map(YugipediaProperty.TextProp::value);
		}
	}

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "name", source = "en_name")
	@Mapping(target = "nameAlt", source = "en_name_2")
	@Mapping(target = "printNumberPrefix", source = "postfix")
	@Mapping(
		target = "type",
		source = "type",
		qualifiedByName = YugipediaPropertyBaseMapper.TO_LOWER_CASE
	)
	@Mapping(
		target = "series",
		source = "series",
		qualifiedByName = YugipediaPropertyBaseMapper.TO_LOWER_CASE
	)
	@Mapping(target = "localizedData.es.name", source = "es_name")
	@Mapping(target = "localizedData.fr.name", source = "fr_name")
	@Mapping(target = "localizedData.it.name", source = "it_name")
	@Mapping(target = "localizedData.de.name", source = "de_name")
	@Mapping(target = "localizedData.ja.name", source = "ja_name")
	@Mapping(target = "localizedData.ko.name", source = "ko_name")
	@Mapping(target = "localizedData.pt.name", source = "pt_name")
	@Mapping(target = "localizedData.zhHans.name", source = "sc_name")
	@Mapping(target = "localizedData.zhHant.name", source = "tc_name")
	public abstract Set toSet(final Map<String, YugipediaProperty> property);

	@AfterMapping
	protected void addMainCardSetCodes(
		@MappingTarget final Set set,
		final Map<String, YugipediaProperty> properties
	) {
		final SetInfo info = getSetInfoToUpdate(LangSpec.EN, set, properties);
		if (info != null) {
			set.setSetCode(info.getSetCode());
			set.setSetCodeAlt(info.getSetCodeAlt());
		}
	}

	@AfterMapping
	protected void updateLocalizedData(
		@MappingTarget final Set set,
		final Map<String, YugipediaProperty> properties
	) {
		for (final LangSpec langSpec : LangSpec.values()) {
			if (langSpec == LangSpec.EN) {
				continue;
			}
			final SetInfo info = getSetInfoToUpdate(langSpec, set, properties);
			if (
				info != null &&
				info.getSetCode() != null &&
				info.getSetCode().equals(set.getSetCode())
			) {
				info.setSetCode(null);
			}
			LocalizationUtils.setLocalizedData(set, info, langSpec.language);
		}
	}

	private SetInfo getSetInfoToUpdate(
		final LangSpec langSpec,
		final Set set,
		final Map<String, YugipediaProperty> properties
	) {
		SetInfo info = LocalizationUtils.getLocalizedData(langSpec.language, set);
		final List<String> prefixes = toSetCodes(langSpec, properties);

		switch (prefixes.size()) {
			case 0:
				break;
			case 1:
				info.setSetCode(prefixes.getFirst());
				break;
			case 2:
				info.setSetCode(prefixes.get(0));
				info.setSetCodeAlt(prefixes.get(1));
				break;
			default:
				throw new MappingException(
					String.format(
						"More than 2 set codes for set %s in language %s",
						set,
						langSpec.language
					)
				);
		}
		if (EMPTY.equals(info)) {
			info = null;
		}
		return info;
	}

	private List<String> toSetCodes(
		final LangSpec lang,
		final Map<String, YugipediaProperty> properties
	) {
		return lang
			.getPrefixProperties(properties)
			.map(prefix ->
				CardNumberParser.parseCardNumber(prefix, lang.possibleRegion)
			)
			.filter(Objects::nonNull)
			.map(CardNumber::setCode)
			.distinct()
			.toList();
	}
}
