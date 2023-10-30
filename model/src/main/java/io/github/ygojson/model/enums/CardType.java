package io.github.ygojson.model.enums;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;

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
