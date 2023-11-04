package io.github.ygojson.model.enums;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Enum values for the main card types.
 */
@JsonClassDescription("Enum values for the main card types.")
public enum CardType {

    @JsonProperty("monster")
    MONSTER,
    @JsonProperty("spell")
    SPELL,
    @JsonProperty("trap")
    TRAP

}
