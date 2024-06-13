package io.github.ygojson.application.yugipedia.mapper;

import java.util.Map;

import org.mapstruct.*;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Mapper for the YGOJSON {@link SetEntity} from {@link YugipediaProperty} map.
 */
@Mapper(
	uses = {
		YugipediaPropertyBaseMapper.class, YugipediaLocalizedDataMapper.class,
	},
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = MappingConstants.ComponentModel.JAKARTA_CDI
)
public abstract class YugipediaSetEntityMapper {

	// ignored
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ygojsonId", ignore = true)
	// common
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
	// main name and altName
	@Mapping(target = "en.name", source = "en_name")
	@Mapping(target = "enNameAlt", source = "en_name_2")
	// localized names
	@Mapping(target = "de.name", source = "de_name")
	@Mapping(target = "es.name", source = "es_name")
	@Mapping(target = "fr.name", source = "fr_name")
	@Mapping(target = "it.name", source = "it_name")
	@Mapping(target = "ja.name", source = "ja_name")
	@Mapping(target = "ko.name", source = "ko_name")
	@Mapping(target = "pt.name", source = "pt_name")
	@Mapping(target = "zhHans.name", source = "sc_name")
	@Mapping(target = "zhHant.name", source = "tc_name")
	// localized setCode / setCodesAlt are set with the LangSpec on after-mapping
	public abstract SetEntity toEntity(
		final Map<String, YugipediaProperty> property
	);

	// TODO: remove this after mapping
	@AfterMapping
	protected void setLocalizedData(
		@MappingTarget final SetEntity entity,
		final Map<String, YugipediaProperty> properties
	) {
		//Arrays
		//	.stream(LangSpec.values())
		//	.forEach(spec -> spec.mapFromProperties(entity, properties));
	}
	/*
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
			List.of(Region.EN, Region.E, Region.AE, Region.A),
			(entity, value) -> entity.enSetCode = value,
			(entity, value) -> entity.enSetCodeAlt = value
		),
		DE(
			Language.DE,
			"de_prefix",
			List.of(Region.DE, Region.G),
			(entity, value) -> entity.deSetCode = value,
			(entity, value) -> entity.deSetCodeAlt = value
		),
		ES(
			Language.ES,
			"sp_prefix",
			List.of(Region.SP, Region.S),
			(entity, value) -> entity.esSetCode = value,
			(entity, value) -> entity.esSetCodeAlt = value
		),
		FR(
			Language.FR,
			List.of("fr_prefix", "fc_prefix"),
			List.of(Region.FR, Region.F, Region.C),
			(entity, value) -> entity.frSetCode = value,
			(entity, value) -> entity.frSetCodeAlt = value
		),
		IT(
			Language.IT,
			"it_prefix",
			List.of(Region.IT, Region.I),
			(entity, value) -> entity.itSetCode = value,
			(entity, value) -> entity.itSetCodeAlt = value
		),
		JA(
			Language.JA,
			List.of("jp_prefix", "ja_prefix"),
			List.of(Region.JA),
			(entity, value) -> entity.jaSetCode = value,
			(entity, value) -> entity.jaSetCode = value
		),
		KO(
			Language.KO,
			"kr_prefix",
			List.of(Region.KR, Region.K),
			(entity, value) -> entity.koSetCode = value,
			(entity, value) -> entity.koSetCodeAlt = value
		),
		PT(
			Language.PT,
			"pt_prefix",
			List.of(Region.PT, Region.P),
			(entity, value) -> entity.ptSetCode = value,
			(entity, value) -> entity.ptSetCodeAlt = value
		),
		ZH_HANS(
			Language.ZH_HANS,
			"sc_prefix",
			List.of(Region.SC),
			(entity, value) -> entity.zhHansSetCode = value,
			(entity, value) -> entity.zhHansSetCodeAlt = value
		),
		ZH_HANT(
			Language.ZH_HANT,
			"tc_prefix",
			List.of(Region.TC),
			(entity, value) -> entity.zhHantSetCode = value,
			(entity, value) -> entity.zhHantSetCodeAlt = value
		);

		private final Language language;
		private final List<String> prefixProperties;
		private final List<Region> possibleRegion;
		private final BiConsumer<SetEntity, String> setCodeSetter;
		private final BiConsumer<SetEntity, String> setCodeAltSetter;

		LangSpec(
			final Language language,
			final String prefixProperties,
			final List<Region> possibleRegions,
			final BiConsumer<SetEntity, String> setCodeSetter,
			final BiConsumer<SetEntity, String> setCodeAltSetter
		) {
			this(
				language,
				List.of(prefixProperties),
				possibleRegions,
				setCodeSetter,
				setCodeAltSetter
			);
		}

		LangSpec(
			final Language language,
			final List<String> prefixProperties,
			final List<Region> possibleRegions,
			final BiConsumer<SetEntity, String> setCodeSetter,
			final BiConsumer<SetEntity, String> setCodeAltSetter
		) {
			this.language = language;
			this.prefixProperties = prefixProperties;
			this.possibleRegion = possibleRegions;
			this.setCodeSetter = setCodeSetter;
			this.setCodeAltSetter = setCodeAltSetter;
		}

		public void mapFromProperties(
			final SetEntity entity,
			final Map<String, YugipediaProperty> properties
		) {
			final List<String> setCodes = toSetCodes(properties);
			setSetCodes(entity, setCodes);
		}

		private Stream<String> getPrefixProperties(
			final Map<String, YugipediaProperty> properties
		) {
			return prefixProperties
				.stream()
				.map(prefix -> (YugipediaProperty.TextProp) properties.get(prefix))
				.filter(Objects::nonNull)
				.map(YugipediaProperty.TextProp::value);
		}

		private List<String> toSetCodes(
			final Map<String, YugipediaProperty> properties
		) {
			return getPrefixProperties(properties)
				.map(prefix -> CardNumberParser.parseCardNumber(prefix, possibleRegion))
				.filter(Objects::nonNull)
				.map(CardNumber::setCode)
				.distinct()
				.toList();
		}

		// helper method to set several set codes at the same time
		// TODO: this should go somewhere else (mapper)?
		// TODO: this might be useful out of a mapper,
		// TODO: but yugipedia-specific list-base mapping might not be the case
		private void setSetCodes(
			final SetEntity entity,
			final List<String> values
		) {
			if (values == null) {
				throw new IllegalArgumentException("Cannot set null set-codes");
			}
			switch (values.size()) {
				case 0:
					setCodeSetter.accept(entity, null);
					setCodeAltSetter.accept(entity, null);
					break;
				case 1:
					setCodeSetter.accept(entity, values.getFirst());
					break;
				case 2:
					setCodeSetter.accept(entity, values.getFirst());
					setCodeAltSetter.accept(entity, values.getLast());
					break;
				default:
					throw new MappingException(
						"Cannot set " + values.size() + " set-codes"
					);
			}
		}
	}
	*/
}
