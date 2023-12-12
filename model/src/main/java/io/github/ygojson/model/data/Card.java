package io.github.ygojson.model.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import io.github.ygojson.model.data.definitions.CardLocalizedData;
import io.github.ygojson.model.data.definitions.CardType;
import io.github.ygojson.model.data.definitions.Identifiers;
import io.github.ygojson.model.data.definitions.LinkArrow;

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
		Card.ID_PROPERTY,
		Card.NAME_PROPERTY,
		Card.IDENTIFIERS_PROPERTY,
		Card.CARD_TYPE_PROPERTY,
		Card.PROPERTY_PROPERTY,
		Card.EFFECT_TEXT_PROPERTY,
		Card.MONSTER_TYPES_PROPERTY,
		Card.FLAVOR_TEXT_PROPERTY,
		Card.ATTRIBUTE_PROPERTY,
		Card.ATK_PROPERTY,
		Card.ATK_UNDEFINED_PROPERTY,
		Card.DEF_PROPERTY,
		Card.DEF_UNDEFINED_PROPERTY,
		Card.LEVEL_PROPERTY,
		Card.PENDULUM_EFFECT_PROPERTY,
		Card.PENDULUM_SCALE_PROPERTY,
		Card.MATERIALS_PROPERTY,
		Card.LINK_RATING_PROPERTY,
		Card.LINK_ARROWS_PROPERTY,
		Card.XYZ_RANK_PROPERTY,
		Card.LOCALIZED_DATA_PROPERTY,
	}
)
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonPOJOBuilder
public class Card {

	public static final String ID_PROPERTY = "id";
	public static final String NAME_PROPERTY = "name";
	public static final String IDENTIFIERS_PROPERTY = "identifiers";
	public static final String CARD_TYPE_PROPERTY = "cardType";
	public static final String PROPERTY_PROPERTY = "property";
	public static final String EFFECT_TEXT_PROPERTY = "effectText";
	public static final String FLAVOR_TEXT_PROPERTY = "flavorText";
	public static final String ATTRIBUTE_PROPERTY = "attribute";
	public static final String MONSTER_TYPES_PROPERTY = "monsterTypes";
	public static final String ATK_PROPERTY = "atk";
	public static final String ATK_UNDEFINED_PROPERTY = "atkUndefined";
	public static final String DEF_PROPERTY = "def";
	public static final String DEF_UNDEFINED_PROPERTY = "defUndefined";
	public static final String LEVEL_PROPERTY = "level";
	public static final String MATERIALS_PROPERTY = "materials";
	public static final String LINK_RATING_PROPERTY = "linkRating";
	public static final String LINK_ARROWS_PROPERTY = "linkArrows";
	public static final String PENDULUM_EFFECT_PROPERTY = "pendulumEffect";
	public static final String PENDULUM_SCALE_PROPERTY = "pendulumScale";
	public static final String XYZ_RANK_PROPERTY = "xyzRank";
	public static final String LOCALIZED_DATA_PROPERTY = "localizedData";

	/**
	 * UUID (v5) for the Card (atomic) generated by YGOJSON.
	 */
	@JsonPropertyDescription(
		"UID (v5) for the Card (atomic) generated by YGOJSON."
	)
	@NotNull @JsonProperty(value = ID_PROPERTY, required = true)
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private UUID id;

	/**
	 * Name of the card.
	 */
	@JsonPropertyDescription("Name of the card")
	@JsonProperty(value = NAME_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;

	/**
	 * Identifiers associated to the card.
	 * </br>
	 * See the actual model for more information.
	 */
	@JsonPropertyDescription("Identifiers associated to the card.")
	@JsonProperty(value = IDENTIFIERS_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Identifiers identifiers;

	/**
	 * Main card type.
	 */
	@JsonPropertyDescription("Main card type.")
	@JsonProperty(value = CARD_TYPE_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private CardType cardType;

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
	@JsonProperty(value = PROPERTY_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String property;

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
	@JsonProperty(value = EFFECT_TEXT_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String effectText;

	/**
	 * Monster type(s) as an array of lower-case values.
	 */
	@JsonPropertyDescription(
		"Array of lower-case values for the monster-type line."
	)
	@JsonProperty(value = MONSTER_TYPES_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<@Pattern(regexp = "[a-z]+") String> monsterTypes;

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
	@JsonProperty(value = FLAVOR_TEXT_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String flavorText;

	/**
	 * Attribute of monster.
	 */
	@JsonPropertyDescription(
		"""
			Attribute of the monster (lower case).

			If the monster is an effect monster, this would be empty."""
	)
	@Pattern(regexp = "[a-z]+")
	@JsonProperty(value = ATTRIBUTE_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String attribute;

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
	@JsonProperty(value = ATK_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Integer atkValue;

	/**
	 * Flag to indicate an undefined ({@code ?}) ATK value if {@code true}.
	 */
	@JsonPropertyDescription(
		"Flag indicating an undefined (<tt>?</tt>) ATK value."
	)
	@JsonProperty(value = ATK_UNDEFINED_PROPERTY, defaultValue = "false")
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private boolean atkUndefined;

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
	@JsonProperty(value = DEF_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Integer defValue;

	/**
	 * Flag to indicate an undefined ({@code ?}) DEF value if {@code true}.
	 */
	@JsonPropertyDescription(
		"Flag indicating an undefined (<tt>?</tt>) DEF value."
	)
	@JsonProperty(value = DEF_UNDEFINED_PROPERTY, defaultValue = "false")
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private boolean defUndefined;

	/**
	 * Level of the monster.
	 */
	@JsonPropertyDescription("Level of the monster.")
	@JsonProperty(value = LEVEL_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@PositiveOrZero
	private Integer level;

	/**
	 * Materials for extra-deck monsters as defined on the card-text.
	 */
	@JsonPropertyDescription(
		"Materials for extra-deck monsters as defined on the card-text."
	)
	@JsonProperty(value = MATERIALS_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String materials;

	/**
	 * Link rating of the link monster.
	 */
	@JsonPropertyDescription("Link rating of the link monster.")
	@JsonProperty(value = LINK_RATING_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@PositiveOrZero
	private Integer linkRating;

	/**
	 * Link arrows of the link monster.
	 */
	@JsonPropertyDescription("Link arrows of the link monster.")
	@JsonProperty(value = LINK_ARROWS_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Size(max = 8)
	private List<LinkArrow> linkArrows;

	/**
	 * Effect of the Pendulum monster.
	 */
	@JsonPropertyDescription("Effect of the Pendulum monster.")
	@JsonProperty(value = PENDULUM_EFFECT_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String pendulumEffect;

	/**
	 * Scale of the Pendulum monster.
	 */
	@JsonPropertyDescription("Scale of the Pendulum monster.")
	@JsonProperty(value = PENDULUM_SCALE_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@PositiveOrZero
	private Integer pendulumScale;

	/**
	 * Rank of the XYZ monster.
	 */
	@JsonPropertyDescription("Rank of the XYZ monster.")
	@JsonProperty(value = XYZ_RANK_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@PositiveOrZero
	private Integer xyzRank;

	/**
	 * Localized data for the card.
	 */
	@JsonPropertyDescription("Localized data for the card.")
	@JsonProperty(value = LOCALIZED_DATA_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Map<String, CardLocalizedData> localizedData;
}
