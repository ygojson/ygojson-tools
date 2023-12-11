package io.github.ygojson.model.data;

import java.util.List;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Model describing the properties of a Card with all associated prints.
 */
@JsonClassDescription(
	"""
		Describes the information for a Card with associated prints.

		This model contains the same properties as the Card Model with the inclusion of the print list."""
)
@Getter
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@JsonPOJOBuilder
public class CardPrints {

	public static final String PRINTS_PROPERTY = "prints";

	@JsonUnwrapped
	public Card card;

	@JsonPropertyDescription("List of prints for the card")
	@JsonProperty(value = PRINTS_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public List<Print> prints;
}
