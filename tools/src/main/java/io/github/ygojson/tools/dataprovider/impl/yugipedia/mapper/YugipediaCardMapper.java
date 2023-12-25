package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardType;
import io.github.ygojson.model.data.definition.LinkArrow;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;

/**
 * Mapper for {@link CardTable2} related classes.
 */
@Mapper(
	uses = {
		GeneralMapper.class, IntegerOrUndefinedMapper.class, YugipediaCardTextMapper.class,
	},
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public abstract class YugipediaCardMapper {

	/**
	 * Monster type separator.
	 */
	private static final String MONSTER_TYPE_SEPARATOR = "/";

	/**
	 * Maps to the YGOJSON Card model the yugipedia relevant information.
	 *
	 * @param cardTable2 the CardTable2 model
	 * @param name name to be used if none is present (the page-tilte)
	 * @param yugipediaPageId the page ID
	 *
	 * @return the card
	 */
	public Card mapToCard(
		final CardTable2 cardTable2,
		final String name,
		final Long yugipediaPageId
	) {
		final Card card = mapToCard(cardTable2);
		updateCard(card, name, yugipediaPageId);
		return card;
	}

	/**
	 * Maps to the YGOJSON Card model only the components of
	 * the already parsed CardTable2 model.
	 *
	 * @param cardTable2 the CardTable2 model
	 * @return the card
	 */
	@Mapping(target = "id", ignore = true) // IDs are not added here
	@Mapping(target = "identifiers.konamiId", source = "database_id")
	@Mapping(target = "identifiers.password", source = "password")
	@Mapping(target = "cardText", source = ".", qualifiedByName = "mainCardText")
	@Mapping(target = "cardType", source = "card_type")
	@Mapping(target = "attribute", qualifiedByName = "toLowerCase")
	@Mapping(target = "property", qualifiedByName = "toLowerCase")
	@Mapping(
		target = "monsterTypes",
		source = "types",
		qualifiedByName = "monsterTypes"
	)
	@Mapping(target = "atkValue", source = "atk")
	@Mapping(target = "atkUndefined", source = "atk")
	@Mapping(target = "defValue", source = "def")
	@Mapping(target = "defUndefined", source = "def")
	@Mapping(target = "linkArrows", source = "link_arrows")
	@Mapping(target = "linkRating", source = "link_arrows")
	@Mapping(target = "level")
	@Mapping(target = "pendulumScale", source = "pendulum_scale")
	@Mapping(target = "xyzRank", source = "rank")
	@Mapping(
		target = "localizedData.de",
		source = ".",
		qualifiedByName = "deCardText"
	)
	@Mapping(
		target = "localizedData.es",
		source = ".",
		qualifiedByName = "esCardText"
	)
	@Mapping(
		target = "localizedData.fr",
		source = ".",
		qualifiedByName = "frCardText"
	)
	@Mapping(
		target = "localizedData.it",
		source = ".",
		qualifiedByName = "itCardText"
	)
	@Mapping(
		target = "localizedData.ja",
		source = ".",
		qualifiedByName = "jaCardText"
	)
	@Mapping(
		target = "localizedData.ko",
		source = ".",
		qualifiedByName = "koCardText"
	)
	@Mapping(
		target = "localizedData.pt",
		source = ".",
		qualifiedByName = "ptCardText"
	)
	@Mapping(
		target = "localizedData.zhHans",
		source = ".",
		qualifiedByName = "zhHansCardText"
	)
	@Mapping(
		target = "localizedData.zhHant",
		source = ".",
		qualifiedByName = "zhHantCardText"
	)
	public abstract Card mapToCard(final CardTable2 cardTable2);

	/**
	 * Updates the card with the name if not present already
	 * and the yugipedia page ID.
	 *
	 * @param card the card to update
	 * @param name the name to update if not present
	 * @param yugipediaPageId the yugipedia page ID
	 */
	private void updateCard(
		final Card card,
		final String name,
		final Long yugipediaPageId
	) {
		if (card.getName() == null) {
			card.getCardText().setName(name);
		}
		card.getIdentifiers().setYugipediaPageId(yugipediaPageId);
	}

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
	protected abstract LinkArrow toLinkArrow(final String cardTable2LinkArrow);

	/**
	 * Custom map method for monster types.
	 *
	 * @param typesString the monster types as a string
	 * @return the list of monster types (lower-case).
	 */
	@Named("monsterTypes")
	protected List<String> toMonsterTypes(String typesString) {
		if (typesString != null) {
			return Arrays
				.stream(typesString.split(MONSTER_TYPE_SEPARATOR))
				.map(value -> value.trim().toLowerCase())
				.collect(Collectors.toList());
		}
		return null;
	}

	/**
	 * Maps the card-type string to the CardType enum.
	 * <br>
	 * As the cardType coming from CardTable2 is capitalized,
	 * convert to UPPER case to match the actual enum from YGOJSON.
	 *
	 * @param cardType string for card-type from CardTable2
	 *
	 * @return enum value
	 */
	protected CardType toCardTypeEnum(final String cardType) {
		return CardType.valueOf(cardType.toUpperCase());
	}
}
