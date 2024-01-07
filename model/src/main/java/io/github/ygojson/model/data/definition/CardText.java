package io.github.ygojson.model.data.definition;

import java.util.Objects;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.*;

import io.github.ygojson.model.data.property.CardProperties;

/**
 * Definition for the card text that can be localized.
 * <br>
 * This model doesn't include the enums or values that are
 * shared between cards (i.e., attributes).
 */
@JsonClassDescription(
	"""
		Describes card text that can be localized.

		Only card information that is not an enum is included."""
)
@JsonPropertyOrder(
	{
		CardProperties.NAME,
		CardProperties.EFFECT_TEXT,
		CardProperties.FLAVOR_TEXT,
		CardProperties.MATERIALS,
		CardProperties.PENDULUM_EFFECT,
	}
)
public class CardText {

	private String name;

	private String effectText;

	private String flavorText;

	private String materials;

	private String pendulumEffect;

	/**
	 * Name of the card.
	 */
	@JsonPropertyDescription("Name of the card")
	@JsonProperty(value = CardProperties.NAME)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getName() {
		return name;
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
		return effectText;
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
		return flavorText;
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
		return materials;
	}

	/**
	 * Effect of the Pendulum monster.
	 */
	@JsonPropertyDescription("Effect of the Pendulum monster.")
	@JsonProperty(value = CardProperties.PENDULUM_EFFECT)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getPendulumEffect() {
		return pendulumEffect;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEffectText(String effectText) {
		this.effectText = effectText;
	}

	public void setFlavorText(String flavorText) {
		this.flavorText = flavorText;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public void setPendulumEffect(String pendulumEffect) {
		this.pendulumEffect = pendulumEffect;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CardText cardText)) return false;
		return (
			Objects.equals(name, cardText.name) &&
			Objects.equals(effectText, cardText.effectText) &&
			Objects.equals(flavorText, cardText.flavorText) &&
			Objects.equals(materials, cardText.materials) &&
			Objects.equals(pendulumEffect, cardText.pendulumEffect)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			name,
			effectText,
			flavorText,
			materials,
			pendulumEffect
		);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", CardText.class.getSimpleName() + "[", "]")
			.add("name='" + name + "'")
			.add("effectText='" + effectText + "'")
			.add("flavorText='" + flavorText + "'")
			.add("materials='" + materials + "'")
			.add("pendulumEffect='" + pendulumEffect + "'")
			.toString();
	}
}
