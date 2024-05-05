package io.github.ygojson.application.yugipedia.mapper;

import java.util.List;
import java.util.Map;

import org.mapstruct.*;

import io.github.ygojson.application.yugipedia.parser.model.CustomProperties;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.LinkArrow;

/**
 * Mapper for the YGOJSON {@link Card} from {@link YugipediaProperty} map.
 */
// TODO: use CDI for injection as we will use it with quarkus
@Mapper
public abstract class YugipediaCardMapper extends YugipediaPropertyBaseMapper {

	@Mapping(target = "id", ignore = true) // IDs are not added here
	@Mapping(target = "name")
	@Mapping(target = "flavorText", ignore = true) // this is a common parsing from effectText
	@Mapping(target = "effectText", source = "lore")
	@Mapping(target = "pendulumEffect", source = "pendulum_effect")
	@Mapping(target = "materials") // TODO: maybe not ignore, as it is part of the CardText mapping?
	@Mapping(
		target = "identifiers.yugipediaPageId",
		source = CustomProperties.PAGE_ID
	)
	@Mapping(target = "identifiers.konamiId", source = "database_id")
	@Mapping(target = "identifiers.password", source = "password")
	@Mapping(target = "cardType", source = "card_type", defaultValue = "MONSTER")
	@Mapping(target = "attribute", qualifiedByName = "toLowerCase")
	@Mapping(target = "property", qualifiedByName = "toLowerCase")
	@Mapping(
		target = "monsterTypes",
		source = "types",
		qualifiedByName = MONSTER_TYPES_MAPPER
	)
	@Mapping(
		target = "atk",
		source = "atk",
		qualifiedByName = "maybeUndefinedIntegerProperty"
	)
	@Mapping(target = "atkUndefined", ignore = true) // computed with after-mapping
	@Mapping(
		target = "def",
		source = "def",
		qualifiedByName = "maybeUndefinedIntegerProperty"
	)
	@Mapping(target = "defUndefined", ignore = true) // computed with after-mapping
	@Mapping(target = "linkArrows", source = "link_arrows")
	@Mapping(target = "linkRating", ignore = true) // computed with after-mapping
	@Mapping(target = "level")
	@Mapping(target = "pendulumScale", source = "pendulum_scale")
	@Mapping(target = "xyzRank", source = "rank")
	@Mapping(target = "localizedData.de.name", source = "de_name")
	@Mapping(target = "localizedData.de.effectText", source = "de_lore")
	@Mapping(
		target = "localizedData.de.pendulumEffect",
		source = "de_pendulum_effect"
	)
	@Mapping(target = "localizedData.es.name", source = "es_name")
	@Mapping(target = "localizedData.es.effectText", source = "es_lore")
	@Mapping(
		target = "localizedData.es.pendulumEffect",
		source = "es_pendulum_effect"
	)
	@Mapping(target = "localizedData.fr.name", source = "fr_name")
	@Mapping(target = "localizedData.fr.effectText", source = "fr_lore")
	@Mapping(
		target = "localizedData.fr.pendulumEffect",
		source = "fr_pendulum_effect"
	)
	@Mapping(target = "localizedData.it.name", source = "it_name")
	@Mapping(target = "localizedData.it.effectText", source = "it_lore")
	@Mapping(
		target = "localizedData.it.pendulumEffect",
		source = "it_pendulum_effect"
	)
	@Mapping(target = "localizedData.ja.name", source = "ja_name")
	@Mapping(target = "localizedData.ja.effectText", source = "ja_lore")
	@Mapping(
		target = "localizedData.ja.pendulumEffect",
		source = "ja_pendulum_effect"
	)
	@Mapping(target = "localizedData.ko.name", source = "ko_name")
	@Mapping(target = "localizedData.ko.effectText", source = "ko_lore")
	@Mapping(
		target = "localizedData.ko.pendulumEffect",
		source = "ko_pendulum_effect"
	)
	@Mapping(target = "localizedData.pt.name", source = "pt_name")
	@Mapping(target = "localizedData.pt.effectText", source = "pt_lore")
	@Mapping(
		target = "localizedData.pt.pendulumEffect",
		source = "pt_pendulum_effect"
	)
	@Mapping(target = "localizedData.zhHans.name", source = "sc_name")
	@Mapping(target = "localizedData.zhHans.effectText", source = "sc_lore")
	@Mapping(
		target = "localizedData.zhHans.pendulumEffect",
		source = "sc_pendulum_effect"
	)
	@Mapping(target = "localizedData.zhHant.name", source = "tc_name")
	@Mapping(target = "localizedData.zhHant.effectText", source = "tc_lore")
	@Mapping(
		target = "localizedData.zhHant.pendulumEffect",
		source = "tc_pendulum_effect"
	)
	public abstract Card toCard(final Map<String, YugipediaProperty> property);

	protected abstract List<LinkArrow> toLinkArrows(final List<String> likArrows);

	@ValueMapping(source = "Top-Left", target = "TOP_LEFT")
	@ValueMapping(source = "Top-Center", target = "TOP_CENTER")
	@ValueMapping(source = "Top-Right", target = "TOP_RIGHT")
	@ValueMapping(source = "Middle-Left", target = "MIDDLE_LEFT")
	@ValueMapping(source = "Middle-Right", target = "MIDDLE_RIGHT")
	@ValueMapping(source = "Bottom-Left", target = "BOTTOM_LEFT")
	@ValueMapping(source = "Bottom-Center", target = "BOTTOM_CENTER")
	@ValueMapping(source = "Bottom-Right", target = "BOTTOM_RIGHT")
	@ValueMapping(
		source = MappingConstants.ANY_REMAINING,
		target = MappingConstants.THROW_EXCEPTION
	)
	protected abstract LinkArrow toLinkArrow(final String likArrow);

	@Named("toLowerCase")
	protected String toLowerCase(final String value) {
		return value.toLowerCase();
	}
}
