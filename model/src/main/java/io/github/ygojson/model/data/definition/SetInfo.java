package io.github.ygojson.model.data.definition;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Pattern;

import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.model.data.property.SetProperties;

/**
 * Definition for the set information that can be localized.
 * <br>
 * This model doesn't include the enums or values that are
 * shared between sets (i.e., series).
 */
@JsonClassDescription(
	"""
		Describes set text that can be localized.

		Only set information that is not an enum is included."""
)
@JsonPropertyOrder(
	{
		SetProperties.NAME,
		SetProperties.NAME_ALT,
		SetProperties.SET_CODE,
		SetProperties.REGION_CODES,
	}
)
public class SetInfo {

	private String name;
	private String nameAlt;
	private String setCode;
	private List<Region> regionCodes;

	/**
	 * Name of the set.
	 */
	@JsonPropertyDescription("Name of the set.")
	@JsonProperty(value = SetProperties.NAME)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public String getName() {
		return name;
	}

	/**
	 * Alternate name of the set.
	 */
	@JsonPropertyDescription("Alternate name of the set.")
	@JsonProperty(value = SetProperties.NAME_ALT)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public String getNameAlt() {
		return nameAlt;
	}

	/**
	 * Set code.
	 */
	@JsonPropertyDescription("Set code.")
	@JsonProperty(value = SetProperties.SET_CODE)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-z-0-9]+")
	public String getSetCode() {
		return setCode;
	}

	/**
	 * List of region codes for the set.
	 */
	@JsonPropertyDescription("List of region codes for the set.")
	@JsonProperty(value = SetProperties.REGION_CODES)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public List<Region> getRegionCodes() {
		return regionCodes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameAlt(String nameAlt) {
		this.nameAlt = nameAlt;
	}

	public void setSetCode(String setCode) {
		this.setCode = setCode;
	}

	public void setRegionCodes(List<Region> regionCodes) {
		this.regionCodes = regionCodes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SetInfo setInfo)) return false;
		return (
			Objects.equals(name, setInfo.name) &&
			Objects.equals(nameAlt, setInfo.nameAlt) &&
			Objects.equals(setCode, setInfo.setCode) &&
			Objects.equals(regionCodes, setInfo.regionCodes)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, nameAlt, setCode, regionCodes);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", SetInfo.class.getSimpleName() + "[", "]")
			.add("name='" + name + "'")
			.add("nameAlt='" + nameAlt + "'")
			.add("setCode='" + setCode + "'")
			.add("regionCodes=" + regionCodes)
			.toString();
	}
}
