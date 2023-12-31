package io.github.ygojson.model.data.definition;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum values for the arrows on a link monster.
 */
@JsonClassDescription("Enum values for the arrows on link monster.")
public enum LinkArrow {
	@JsonProperty("top-left")
	TOP_LEFT,
	@JsonProperty("top-center")
	TOP_CENTER,
	@JsonProperty("top-right")
	TOP_RIGHT,
	@JsonProperty("middle-left")
	MIDDLE_LEFT,
	@JsonProperty("middle-right")
	MIDDLE_RIGHT,
	@JsonProperty("bottom-left")
	BOTTOM_LEFT,
	@JsonProperty("bottom-center")
	BOTTOM_CENTER,
	@JsonProperty("bottom-right")
	BOTTOM_RIGHT,
}
