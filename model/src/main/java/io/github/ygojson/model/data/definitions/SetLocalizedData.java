package io.github.ygojson.model.data.definitions;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import io.github.ygojson.model.data.Set;

@JsonClassDescription(
	"""
		Describes translations and locale-specific data for a set.

		All translated properties match the name of the original property
		and any missing property indicates no translation for it."""
)
@JsonPropertyOrder({ Set.PREFIX_PROPERTY, Set.NAME_PROPERTY })
@Getter
@SuperBuilder(toBuilder = true)
@JsonPOJOBuilder
public class SetLocalizedData {

	/**
	 * Localized data for {@link Set#getPrefix()}.
	 */
	@JsonPropertyDescription("Prefix of the set in this locale (localized).")
	@JsonProperty(value = Set.PREFIX_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-zA-Z0-9]+-[a-zA-Z0-9]+")
	private String prefix;

	/**
	 * Translation for {@link Set#getPrefix()}.
	 */
	@JsonPropertyDescription("Name of the set in this locale (translated).")
	@JsonProperty(value = Set.NAME_PROPERTY)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String name;
}
