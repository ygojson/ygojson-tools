package io.github.ygojson.model.data.definition;

import java.util.Objects;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Pattern;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.property.SetProperties;

/**
 * Definition for the set text that can be localized.
 * <br>
 * This model doesn't include the enums or values that are
 * shared between sets (i.e., series).
 */
@JsonClassDescription(
	"""
		Describes set text that can be localized.

		Only set information that is not an enum is included."""
)
@JsonPropertyOrder({ SetProperties.PREFIX, SetProperties.NAME })
public class SetText {

	private String prefix;
	private String name;

	/**
	 * Localized data for {@link Set#getPrefix()}.
	 */
	@JsonPropertyDescription("Prefix of the set in this locale (localized).")
	@JsonProperty(value = SetProperties.PREFIX)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-zA-Z0-9]+-[a-zA-Z0-9]+")
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Translation for {@link Set#getPrefix()}.
	 */
	@JsonPropertyDescription("Name of the set in this locale (translated).")
	@JsonProperty(value = SetProperties.NAME)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public String getName() {
		return name;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SetText that)) return false;
		return (
			Objects.equals(prefix, that.prefix) && Objects.equals(name, that.name)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prefix, name);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", SetText.class.getSimpleName() + "[", "]")
			.add("prefix='" + prefix + "'")
			.add("name='" + name + "'")
			.toString();
	}
}
