package io.github.ygojson.model.data;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import io.github.ygojson.model.data.definition.CardText;
import io.github.ygojson.model.data.definition.CardType;
import io.github.ygojson.model.data.definition.Identifiers;
import io.github.ygojson.model.data.definition.LinkArrow;
import io.github.ygojson.model.data.definition.localization.CardLocalizedData;
import io.github.ygojson.model.data.property.CardProperties;
import io.github.ygojson.model.data.property.IdProperties;
import io.github.ygojson.model.data.property.LanguageProperties;

/**
 * Model describing the properties of a Card (atomic).
 */
@JsonClassDescription(
	"""
		Describes the information for a card.

		This model contains the properties of the card that will not change from print to print."""
)
@JsonPropertyOrder(
	{
		IdProperties.SELF_ID,
		CardProperties.IDENTIFIERS,
		CardProperties.NAME,
		CardProperties.CARD_TYPE,
		CardProperties.PROPERTY,
		CardProperties.EFFECT_TEXT,
		CardProperties.MONSTER_TYPES,
		CardProperties.FLAVOR_TEXT,
		CardProperties.ATTRIBUTE,
		CardProperties.ATK,
		CardProperties.ATK_UNDEFINED,
		CardProperties.DEF,
		CardProperties.DEF_UNDEFINED,
		CardProperties.LEVEL,
		CardProperties.MATERIALS,
		CardProperties.PENDULUM_EFFECT,
		CardProperties.PENDULUM_SCALE,
		CardProperties.LINK_RATING,
		CardProperties.LINK_ARROWS,
		CardProperties.XYZ_RANK,
		LanguageProperties.LOCALIZED_DATA,
	}
)
public class Card {

	// container for card text related properties
	private CardText cardText;

	private UUID id;
	private Identifiers identifiers;
	private CardType cardType;
	private String property;
	private List<String> monsterTypes;
	private String attribute;
	private Integer atk;
	private Boolean atkUndefined;

	private Integer def;

	private Boolean defUndefined;

	private Integer level;

	private Integer linkRating;

	private List<LinkArrow> linkArrows;

	private Integer pendulumScale;

	private Integer xyzRank;

	private CardLocalizedData localizedData;

	/**
	 * Return the card-text as a whole.
	 * <br>
	 * Note: this method is not serialized as JSON,
	 * as each individual getter/setter has its own field.
	 *
	 * @return card-text as a model; might be {@code null}
	 */
	@JsonIgnore // using json ignore but add the methods for the annotations+
	public CardText getCardText() {
		return cardText;
	}

	private Optional<CardText> getOptionalCardText() {
		return Optional.ofNullable(cardText);
	}

	/**
	 * UUID (v5) for the Card (atomic) generated by YGOJSON.
	 */
	@JsonPropertyDescription(
		"UID (v5) for the Card (atomic) generated by YGOJSON."
	)
	@JsonProperty(value = IdProperties.SELF_ID, required = true)
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public UUID getId() {
		return id;
	}

	/**
	 * Identifiers associated to the card.
	 * </br>
	 * See the actual model for more information.
	 */
	@JsonPropertyDescription("Identifiers associated to the card.")
	@JsonProperty(value = CardProperties.IDENTIFIERS)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Identifiers getIdentifiers() {
		return identifiers;
	}

	/**
	 * Name of the card.
	 */
	@JsonPropertyDescription("Name of the card")
	@JsonProperty(value = CardProperties.NAME)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getName() {
		return getOptionalCardText().map(CardText::getName).orElse(null);
	}

	/**
	 * Main card type.
	 */
	@JsonPropertyDescription("Main card type.")
	@JsonProperty(value = CardProperties.CARD_TYPE)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public CardType getCardType() {
		return cardType;
	}

	/**
	 * Special property for the spell or trap (lower-case).
	 * </br>
	 * These are the properties represented by the spell/trap icon.
	 */
	@JsonPropertyDescription(
		"""
			Special property for the spell or trap (lower-case).

			These are the properties represented by the spell/trap icon."""
	)
	@Pattern(regexp = "[a-z]+")
	@JsonProperty(value = CardProperties.PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getProperty() {
		return property;
	}

	/**
	 * Effect text for the card (empty for Normal Monsters).
	 * </br>
	 * Note that the list of materials or pendulum effects are not included
	 * as they are on their own property.
	 */
	@JsonPropertyDescription(
		"""
			Effect text for the card (empty for Normal Monsters).

			Note that the list of materials and pendulum effects are not included as they are part of their own property.
			"""
	)
	@JsonProperty(value = CardProperties.EFFECT_TEXT)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getEffectText() {
		return getOptionalCardText().map(CardText::getEffectText).orElse(null);
	}

	/**
	 * Monster type(s) as an array of lower-case values.
	 */
	@JsonPropertyDescription(
		"Array of lower-case values for the monster-type line."
	)
	@JsonProperty(value = CardProperties.MONSTER_TYPES)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public List<@Pattern(regexp = "[a-z]+") String> getMonsterTypes() {
		return monsterTypes;
	}

	/**
	 * Flavor text on a normal monster.
	 * </br>
	 * If the monster is an effect monster, this would be empty.
	 */
	@JsonPropertyDescription(
		"""
			Flavor text on a normal monster.

			If the monster is an effect monster, this would be empty."""
	)
	@JsonProperty(value = CardProperties.FLAVOR_TEXT)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getFlavorText() {
		return getOptionalCardText().map(CardText::getFlavorText).orElse(null);
	}

	/**
	 * Attribute of monster.
	 */
	@JsonPropertyDescription(
		"""
			Attribute of the monster (lower case).

			If the monster is an effect monster, this would be empty."""
	)
	@Pattern(regexp = "[a-z]+")
	@JsonProperty(value = CardProperties.ATTRIBUTE)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public String getAttribute() {
		return attribute;
	}

	/**
	 * Actual ATK value.
	 * </br>
	 * If {@code 0}, it can also be undefined. In that case,
	 * {@link #isAtkUndefined()} would be equal to {@code true}.
	 */
	@JsonPropertyDescription(
		"""
			Actual ATK value.

			If <tt>0</tt>, it can also be undefined.
			In that case, the <tt>atkUndefined</tt> property would be equal to <tt>true</tt>."""
	)
	@PositiveOrZero
	@JsonProperty(value = CardProperties.ATK)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Integer getAtk() {
		return atk;
	}

	/**
	 * Flag to indicate an undefined ({@code ?}) ATK value if {@code true}.
	 */
	@JsonPropertyDescription(
		"Flag indicating an undefined (<tt>?</tt>) ATK value."
	)
	@JsonProperty(value = CardProperties.ATK_UNDEFINED, defaultValue = "false")
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	public Boolean isAtkUndefined() {
		return atkUndefined;
	}

	/**
	 * Actual DEF value.
	 * </br>
	 * If {@code 0}, it can also be undefined. In that case,
	 * {@link #isDefUndefined()} would be equal to {@code true}.
	 */
	@JsonPropertyDescription(
		"""
			Actual DEF value.

			If <tt>0</tt>, it can also be undefined.
			In that case, the <tt>defUndefined</tt> property would be equal to <tt>true</tt>."""
	)
	@PositiveOrZero
	@JsonProperty(value = CardProperties.DEF)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Integer getDef() {
		return def;
	}

	/**
	 * Flag to indicate an undefined ({@code ?}) DEF value if {@code true}.
	 */
	@JsonPropertyDescription(
		"Flag indicating an undefined (<tt>?</tt>) DEF value."
	)
	@JsonProperty(value = CardProperties.DEF_UNDEFINED, defaultValue = "false")
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	public Boolean isDefUndefined() {
		return defUndefined;
	}

	/**
	 * Level of the monster.
	 */
	@JsonPropertyDescription("Level of the monster.")
	@JsonProperty(value = CardProperties.LEVEL)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@PositiveOrZero
	public Integer getLevel() {
		return level;
	}

	/**
	 * Materials for extra-deck monsters as defined on the card-text.
	 */
	@JsonPropertyDescription(
		"Materials for extra-deck monsters as defined on the card-text."
	)
	@JsonProperty(value = CardProperties.MATERIALS)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getMaterials() {
		return getOptionalCardText().map(CardText::getMaterials).orElse(null);
	}

	/**
	 * Link rating of the link monster.
	 */
	@JsonPropertyDescription("Link rating of the link monster.")
	@JsonProperty(value = CardProperties.LINK_RATING)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@PositiveOrZero
	public Integer getLinkRating() {
		return linkRating;
	}

	/**
	 * Link arrows of the link monster.
	 */
	@JsonPropertyDescription("Link arrows of the link monster.")
	@JsonProperty(value = CardProperties.LINK_ARROWS)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Size(max = 8)
	public List<LinkArrow> getLinkArrows() {
		return linkArrows;
	}

	/**
	 * Effect of the Pendulum monster.
	 */
	@JsonPropertyDescription("Effect of the Pendulum monster.")
	@JsonProperty(value = CardProperties.PENDULUM_EFFECT)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getPendulumEffect() {
		return getOptionalCardText().map(CardText::getPendulumEffect).orElse(null);
	}

	/**
	 * Scale of the Pendulum monster.
	 */
	@JsonPropertyDescription("Scale of the Pendulum monster.")
	@JsonProperty(value = CardProperties.PENDULUM_SCALE)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@PositiveOrZero
	public Integer getPendulumScale() {
		return pendulumScale;
	}

	/**
	 * Rank of the XYZ monster.
	 */
	@JsonPropertyDescription("Rank of the XYZ monster.")
	@JsonProperty(value = CardProperties.XYZ_RANK)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@PositiveOrZero
	public Integer getXyzRank() {
		return xyzRank;
	}

	/**
	 * Localized data for the card.
	 */

	@JsonPropertyDescription(
		"""
		Describes translations and locale-specific data for a card.

		All translated properties match the name of the original property
		and any missing property indicates no translation for it."""
	)
	@JsonProperty(value = LanguageProperties.LOCALIZED_DATA)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public CardLocalizedData getLocalizedData() {
		return localizedData;
	}

	public void setCardText(CardText cardText) {
		this.cardText = cardText;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setIdentifiers(Identifiers identifiers) {
		this.identifiers = identifiers;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setMonsterTypes(List<String> monsterTypes) {
		this.monsterTypes = monsterTypes;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setAtk(Integer atk) {
		this.atk = atk;
	}

	public void setAtkUndefined(Boolean atkUndefined) {
		this.atkUndefined = atkUndefined;
	}

	public void setDef(Integer def) {
		this.def = def;
	}

	public void setDefUndefined(Boolean defUndefined) {
		this.defUndefined = defUndefined;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setLinkRating(Integer linkRating) {
		this.linkRating = linkRating;
	}

	public void setLinkArrows(List<LinkArrow> linkArrows) {
		this.linkArrows = linkArrows;
	}

	public void setPendulumScale(Integer pendulumScale) {
		this.pendulumScale = pendulumScale;
	}

	public void setXyzRank(Integer xyzRank) {
		this.xyzRank = xyzRank;
	}

	public void setLocalizedData(CardLocalizedData localizedData) {
		this.localizedData = localizedData;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Card card)) return false;
		return (
			atkUndefined == card.atkUndefined &&
			defUndefined == card.defUndefined &&
			Objects.equals(cardText, card.cardText) &&
			Objects.equals(id, card.id) &&
			Objects.equals(identifiers, card.identifiers) &&
			cardType == card.cardType &&
			Objects.equals(property, card.property) &&
			Objects.equals(monsterTypes, card.monsterTypes) &&
			Objects.equals(attribute, card.attribute) &&
			Objects.equals(atk, card.atk) &&
			Objects.equals(def, card.def) &&
			Objects.equals(level, card.level) &&
			Objects.equals(linkRating, card.linkRating) &&
			Objects.equals(linkArrows, card.linkArrows) &&
			Objects.equals(pendulumScale, card.pendulumScale) &&
			Objects.equals(xyzRank, card.xyzRank) &&
			Objects.equals(localizedData, card.localizedData)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			cardText,
			id,
			identifiers,
			cardType,
			property,
			monsterTypes,
			attribute,
			atk,
			atkUndefined,
			def,
			defUndefined,
			level,
			linkRating,
			linkArrows,
			pendulumScale,
			xyzRank,
			localizedData
		);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Card.class.getSimpleName() + "[", "]")
			.add("cardText=" + cardText)
			.add("id=" + id)
			.add("identifiers=" + identifiers)
			.add("cardType=" + cardType)
			.add("property='" + property + "'")
			.add("monsterTypes=" + monsterTypes)
			.add("attribute='" + attribute + "'")
			.add("atk=" + atk)
			.add("atkUndefined=" + atkUndefined)
			.add("def=" + def)
			.add("defUndefined=" + defUndefined)
			.add("level=" + level)
			.add("linkRating=" + linkRating)
			.add("linkArrows=" + linkArrows)
			.add("pendulumScale=" + pendulumScale)
			.add("xyzRank=" + xyzRank)
			.add("localizedData=" + localizedData)
			.toString();
	}
}
