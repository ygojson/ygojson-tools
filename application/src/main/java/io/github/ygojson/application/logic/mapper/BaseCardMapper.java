package io.github.ygojson.application.logic.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.model.data.definition.CardType;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.utils.CardUtils;
import io.github.ygojson.model.utils.LocalizationUtils;

/**
 * Base mapper containing methods for base card parsing.
 */
public class BaseCardMapper {

	private static final Logger LOG = LoggerFactory.getLogger(
		BaseCardMapper.class
	);

	/**
	 * Name of the mapper method for monster-types.
	 */
	protected static final String MONSTER_TYPES_MAPPER = "monsterTypes";

	/**
	 * Representation of undefined value returned by {@link #toMaybeUndefinedLong(String)}.
	 */
	protected static final Long UNDEFINED = -1L;

	// monster type separator
	private static final String MONSTER_TYPE_SEPARATOR = "/";

	// empty card-text to compare and remove on localized-data
	private static final CardText EMPTY_CARDTEXT = new CardText();

	private static final String UNDEFINED_NUMBER_STRING = "?";

	private static final String NORMAL_MONSTER = "normal";
	private static final String EFFECT_MONSTER = "effect";
	private static final Set<String> ABILITY_TYPES = Set.of(
		"toon",
		"spirit",
		"union",
		"gemini",
		"flip"
	);

	/**
	 * Converts the value into a long (which is maybe undefined).
	 *
	 * @param value the string value.
	 *
	 * @return {@link #UNDEFINED} if the values is the undefined string;
	 * 		   {@code null} if the value is not a parsable long;
	 * 		   {@code long} otherwise.
	 */
	protected final Long toMaybeUndefinedLong(String value) {
		if (value == null) {
			return null;
		}
		if (UNDEFINED_NUMBER_STRING.equals(value)) {
			return UNDEFINED;
		}
		try {
			return Long.parseLong(value);
		} catch (final NumberFormatException e) {
			LOG.warn("Unknown number format considered null: {}", value);
			LOG.debug("Exception", e);
			return null;
		}
	}

	/**
	 * Convert the card-type string to the CardType enum.
	 * <br>
	 * This method is not {@link Named}, but a {@link org.mapstruct.Mapper} annotated
	 * class would use it to convert any {@link String} to the {@link CardType}.
	 *
	 * @param cardType string for card-type, in any case
	 *
	 * @return enum value value of te enum.
	 */
	protected CardType toCardTypeEnum(final String cardType) {
		try {
			return CardType.fromValue(cardType.toLowerCase());
		} catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException("Unknown card type: " + cardType);
		}
	}

	/**
	 * Convert the map method into a monster-type list.
	 * <br>
	 * This is a named mapstruct method ({@link #MONSTER_TYPES_MAPPER}).
	 *
	 * @param typesString the monster types as a string
	 * @return the list of monster types (lower-case).
	 */
	@Named(MONSTER_TYPES_MAPPER)
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
	 * Method to update the link-rating after mapping is performed.
	 * <br>
	 * This method is annotated with {@link AfterMapping} to be used by derived mappers.
	 *
	 * @param card the card to update.
	 */
	@AfterMapping
	protected void computeLinkRating(@MappingTarget Card card) {
		if (card.getLinkRating() == null) {
			if (card.getLinkArrows() != null && !card.getLinkArrows().isEmpty()) {
				card.setLinkRating(card.getLinkArrows().size());
			}
		}
	}

	/**
	 * Method to update the undefined ATK values after mapping is performed.
	 * <br>
	 * Note that the card ATK should be mapped to the {@link #UNDEFINED} value
	 * to identify them as such (see {@link #toMaybeUndefinedLong(String)} as the helper method for that).
	 * <br>
	 * This method is annotated with {@link AfterMapping} to be used by derived mappers.
	 *
	 * @param card the card to update.
	 */
	@AfterMapping
	protected void updateUndefinedAtk(@MappingTarget Card card) {
		if (isUndefined(card.getAtk())) {
			card.setAtk(0);
			card.setAtkUndefined(true);
		}
	}

	/**
	 * Method to update the undefined DEF values after mapping is performed.
	 * <br>
	 * Note that the card DEF should be mapped to the {@link #UNDEFINED} value
	 * to identify them as such (see {@link #toMaybeUndefinedLong(String)} as the helper method for that).
	 * <br>
	 * This method is annotated with {@link AfterMapping} to be used by derived mappers.
	 *
	 * @param card the card to update.
	 */
	@AfterMapping
	protected void updateUndefinedDef(@MappingTarget Card card) {
		if (isUndefined(card.getDef())) {
			card.setDef(0);
			card.setDefUndefined(true);
		}
	}

	/**
	 * Checks if the value is undefined.
	 *
	 * @param value the value to check.
	 *
	 * @return {@code true} if the value is undefined; {@code false} otherwise.
	 */
	protected boolean isUndefined(final Integer value) {
		// null is not considered undefined
		if (value == null) {
			return false;
		}
		return value == UNDEFINED.intValue();
	}

	/**
	 * Method to update the main card text values after mapping is performed.
	 * <br>
	 * This method is annotated with {@link AfterMapping} to be used by derived mappers.
	 *
	 * @param card the card to update.
	 */
	@AfterMapping
	protected void computeMainCardText(@MappingTarget Card card) {
		if (card.getEffectText() != null) {
			SplitCardText cardText = fullCardTextToCard(card.getEffectText(), card);
			card.setMaterials(cardText.materials());
			card.setEffectText(cardText.effect());
			card.setFlavorText(cardText.flavor());
		}
	}

	/**
	 * Method to update the {@link CardText} values on the {@link Card#getLocalizedData()}
	 * after mapping is performed.
	 * <br>
	 * This method is annotated with {@link AfterMapping} to be used by derived mappers.
	 *
	 * @param card the card to update.
	 */
	@AfterMapping
	protected void computeLocalizedCardText(@MappingTarget Card card) {
		if (card.getLocalizedData() != null) {
			LocalizationUtils
				.getNonMainLanguages()
				.forEach(language -> updateLanguageCardText(language, card));
		}
	}

	// helper record to split card text
	private record SplitCardText(
		String effect,
		String materials,
		String flavor
	) {}

	private void updateLanguageCardText(
		final Language language,
		final Card card
	) {
		final CardText cardText = LocalizationUtils.getLocalizedData(
			language,
			card
		);
		final CardText updatedCardText = updateCardText(cardText, card);
		LocalizationUtils.setLocalizedData(card, updatedCardText, language);
	}

	private CardText updateCardText(final CardText cardText, final Card card) {
		if (cardText == null || EMPTY_CARDTEXT.equals(cardText)) {
			return null;
		}
		if (cardText.getEffectText() != null) {
			final SplitCardText updatedCardText = fullCardTextToCard(
				cardText.getEffectText(),
				card
			);
			// update the fields
			cardText.setMaterials(updatedCardText.materials());
			cardText.setEffectText(updatedCardText.effect());
			cardText.setFlavorText(updatedCardText.flavor());
		}
		return cardText;
	}

	private SplitCardText fullCardTextToCard(
		final String fullCardText,
		Card card
	) {
		SplitCardText splitCardText = new SplitCardText(fullCardText, null, null);
		// process extra-deck if necessary
		if (CardUtils.isExtraDeckMonster(card)) {
			splitCardText = splitExtraDeckMonster(fullCardText);
		}
		// process flavor if necessary
		if (isFlavorText(splitCardText.effect(), card)) {
			splitCardText =
				new SplitCardText(
					null,
					splitCardText.materials(),
					splitCardText.effect()
				);
		}
		// set using the mapper, which considers null values already
		return splitCardText;
	}

	private SplitCardText splitExtraDeckMonster(final String fullCardText) {
		final String[] materialAndEffect = fullCardText.split("\n", 2);
		switch (materialAndEffect.length) {
			case 2 -> { // material and effect present
				return new SplitCardText(
					materialAndEffect[1],
					materialAndEffect[0],
					null
				);
			}
			case 1 -> { // only materials are present (normal extra deck monster)
				return new SplitCardText(null, materialAndEffect[0], null);
			}
			case 0 -> {
				return new SplitCardText(null, null, null);
			} // do nothing if the actual string is null/empty
			default -> throw new IllegalStateException(
				"should not happen: split-with-limit should return 0-2 items"
			);
		}
	}

	private boolean isFlavorText(String effect, Card card) {
		// only if the effect is present or the card is a monster card
		if (effect == null || card.getCardType() != CardType.MONSTER) {
			return false;
		}
		if (card.getMonsterTypes() == null && card.getMonsterTypes().isEmpty()) {
			return false;
		}
		// if the card already have flavor-text, then it contains flavor text
		if (card.getFlavorText() != null) {
			return true;
		}
		final Boolean normalMonster = isCodedNormalMonster(card.getMonsterTypes());
		// null in case that the card
		if (normalMonster != null) {
			return normalMonster;
		}
		// if the normal-monster status is not coded, then we need to follow other rules
		// if the type contains an ability, then it is an effect monster
		if (!containsAbility(card.getMonsterTypes())) {
			return true;
		}
		return false;
	}

	private Boolean isCodedNormalMonster(List<String> monsterTypes) {
		for (final String monsterType : monsterTypes) {
			if (NORMAL_MONSTER.equals(monsterType)) {
				return true;
			}
			if (EFFECT_MONSTER.equals(monsterType)) {
				return false;
			}
		}
		return null;
	}

	private boolean containsAbility(List<String> monsterTypes) {
		return monsterTypes.stream().anyMatch(ABILITY_TYPES::contains);
	}
}
