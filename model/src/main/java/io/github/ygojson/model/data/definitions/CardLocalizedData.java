package io.github.ygojson.model.data.definitions;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import io.github.ygojson.model.data.Card;

@JsonClassDescription(
	"""
		Describes translations and locale-specific data for a card.

		All translated properties match the name of the original property
		and any missing property indicates no translation for it."""
)
@JsonPropertyOrder(
	{
		Card.NAME_PROPERTY,
		Card.EFFECT_TEXT_PROPERTY,
		Card.FLAVOR_TEXT_PROPERTY,
		Card.MATERIALS_PROPERTY,
		Card.PENDULUM_EFFECT_PROPERTY,
	}
)
@Data
@SuperBuilder(toBuilder = true)
@JsonPOJOBuilder
public class CardLocalizedData {

	/**
	 * Translation for {@link Card#getName()}.
	 */
	@JsonPropertyDescription("Name of the card (translated)")
	@JsonProperty(value = Card.NAME_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;

	/**
	 * Translation for {@link Card#getEffectText()}.
	 */
	@JsonPropertyDescription("Effect text for the card (translated)")
	@JsonProperty(value = Card.EFFECT_TEXT_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String effectText;

	/**
	 * Translation for {@link Card#getFlavorText()}.
	 */
	@JsonPropertyDescription("Flavor text on a normal monster (translated)")
	@JsonProperty(value = Card.FLAVOR_TEXT_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String flavorText;

	/**
	 * Translation for {@link Card#getMaterials()}.
	 */
	@JsonPropertyDescription(
		"Materials for extra-deck monsters as defined on the card-text (translated)"
	)
	@JsonProperty(value = Card.MATERIALS_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String materials;

	/**
	 * Translation for {@link Card#getPendulumEffect()}.
	 */
	@JsonPropertyDescription("Effect of the Pendulum monster (translated)")
	@JsonProperty(value = Card.PENDULUM_EFFECT_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String pendulumEffect;
}
