package io.github.ygojson.application.yugipedia.mapper;

import static io.github.ygojson.application.yugipedia.mapper.YugipediaPropertyBaseMapper.TO_LOWER_CASE;

import java.util.List;
import java.util.Map;

import org.mapstruct.*;

import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.core.db.updater.CardEntityUpdater;
import io.github.ygojson.application.logic.mapper.MappingException;
import io.github.ygojson.application.logic.parser.AtkDefParser;
import io.github.ygojson.application.logic.parser.CardTypeParser;
import io.github.ygojson.application.logic.parser.MonsterTypesParser;
import io.github.ygojson.application.yugipedia.parser.model.CustomProperties;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Mapper for the YGOJSON {@link CardEntity} from {@link YugipediaProperty} map.
 */
@Mapper(
	uses = {
		YugipediaPropertyBaseMapper.class, YugipediaLocalizedDataMapper.class,
	},
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = MappingConstants.ComponentModel.JAKARTA_CDI
)
public abstract class YugipediaCardEntityMapper {

	protected static final String TO_ATK_DEF_MAPPER = "toAtkDefValue";
	protected static final String TO_CARD_TYPE = "toCardType";
	protected static final String TO_MONSTER_TYPES = "toMonsterTypes";
	private static final String TO_LINK_ARROWS = "toLinkArrows";

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ygojsonId", ignore = true)
	@Mapping(
		target = "identifiers.yugipediaPageId",
		source = CustomProperties.PAGE_ID
	)
	@Mapping(target = "identifiers.konamiId", source = "database_id")
	@Mapping(target = "identifiers.password", source = "password")
	@Mapping(
		target = "cardType",
		source = "card_type",
		defaultValue = "MONSTER",
		qualifiedByName = TO_CARD_TYPE
	) // yugipedia no-value for card_type is a MONSTER type
	@Mapping(target = "attribute", qualifiedByName = TO_LOWER_CASE)
	@Mapping(target = "property", qualifiedByName = TO_LOWER_CASE)
	@Mapping(
		target = "monsterTypes",
		source = "types",
		qualifiedByName = TO_MONSTER_TYPES
	)
	@Mapping(target = "atk", source = "atk", qualifiedByName = TO_ATK_DEF_MAPPER)
	@Mapping(target = "atkUndefined", ignore = true) // computed with after-mapping
	@Mapping(target = "def", source = "def", qualifiedByName = TO_ATK_DEF_MAPPER)
	@Mapping(target = "defUndefined", ignore = true) // computed with after-mapping
	@Mapping(target = "pendulumScale", source = "pendulum_scale")
	@Mapping(
		target = "linkArrows",
		source = "link_arrows",
		qualifiedByName = TO_LINK_ARROWS
	)
	@Mapping(target = "linkRating", ignore = true) // computed with after-mapping
	@Mapping(target = "xyzRank", source = "rank")
	// name
	@Mapping(target = "en.name", source = "name")
	@Mapping(target = "de.name", source = "de_name")
	@Mapping(target = "es.name", source = "es_name")
	@Mapping(target = "fr.name", source = "fr_name")
	@Mapping(target = "it.name", source = "it_name")
	@Mapping(target = "ja.name", source = "ja_name")
	@Mapping(target = "ko.name", source = "ko_name")
	@Mapping(target = "pt.name", source = "pt_name")
	@Mapping(target = "zhHans.name", source = "sc_name")
	@Mapping(target = "zhHant.name", source = "tc_name")
	// pendulum effect text
	@Mapping(target = "en.pendulumEffectText", source = "pendulum_effect")
	@Mapping(target = "de.pendulumEffectText", source = "de_pendulum_effect")
	@Mapping(target = "es.pendulumEffectText", source = "es_pendulum_effect")
	@Mapping(target = "fr.pendulumEffectText", source = "fr_pendulum_effect")
	@Mapping(target = "it.pendulumEffectText", source = "it_pendulum_effect")
	@Mapping(target = "ja.pendulumEffectText", source = "ja_pendulum_effect")
	@Mapping(target = "ko.pendulumEffectText", source = "ko_pendulum_effect")
	@Mapping(target = "pt.pendulumEffectText", source = "pt_pendulum_effect")
	@Mapping(target = "zhHans.pendulumEffectText", source = "sc_pendulum_effect")
	@Mapping(target = "zhHant.pendulumEffectText", source = "tc_pendulum_effect")
	// effect text
	@Mapping(target = "en.effectText", source = "lore")
	@Mapping(target = "de.effectText", source = "de_lore")
	@Mapping(target = "es.effectText", source = "es_lore")
	@Mapping(target = "fr.effectText", source = "fr_lore")
	@Mapping(target = "it.effectText", source = "it_lore")
	@Mapping(target = "ja.effectText", source = "ja_lore")
	@Mapping(target = "ko.effectText", source = "ko_lore")
	@Mapping(target = "pt.effectText", source = "pt_lore")
	@Mapping(target = "zhHans.effectText", source = "sc_lore")
	@Mapping(target = "zhHant.effectText", source = "tc_lore")
	// TODO: maybe ignore, as it is part of the CardText mapping? not sure if the other localizations have the value in yugipedia
	@Mapping(target = "en.materialsText", source = "materials")
	// flavorText and other localized values are mapped after mapping
	@Mapping(target = "en.flavorText", ignore = true) // this is a common parsing from effectText
	public abstract CardEntity toEntity(
		final Map<String, YugipediaProperty> property
	);

	@Named(TO_CARD_TYPE)
	protected static String toCardType(final String props) {
		return CardTypeParser.parse(props);
	}

	@Named(TO_MONSTER_TYPES)
	protected static List<String> toMonsterTypes(final String props) {
		return MonsterTypesParser.parse(props);
	}

	@Named(TO_ATK_DEF_MAPPER)
	protected static Integer toAtkDefValue(final String prop) {
		return AtkDefParser.parse(prop);
	}

	@Named(TO_LINK_ARROWS)
	protected static List<String> toLinkArrows(final List<String> propValue) {
		return propValue
			.stream()
			.map(YugipediaCardEntityMapper::convertLinkArrow)
			.toList();
	}

	private static String convertLinkArrow(final String propLinkArrow) {
		return switch (propLinkArrow) {
			case "Top-Left" -> "top-left";
			case "Top-Center" -> "top-center";
			case "Top-Right" -> "top-right";
			case "Middle-Left" -> "middle-left";
			case "Middle-Right" -> "middle-right";
			case "Bottom-Left" -> "bottom-left";
			case "Bottom-Center" -> "bottom-center";
			case "Bottom-Right" -> "bottom-right";
			default -> throw new MappingException(
				"Unexpected link-arrow: " + propLinkArrow
			);
		};
	}

	@AfterMapping
	protected static void updateCard(@MappingTarget CardEntity entity) {
		CardEntityUpdater.update(entity);
	}
}
