package io.github.ygojson.model.data;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import io.github.ygojson.model.data.definitions.SetText;
import io.github.ygojson.model.data.definitions.localization.SetLocalizedData;
import io.github.ygojson.model.data.properties.CommonProperties;
import io.github.ygojson.model.data.properties.LanguageProperties;
import io.github.ygojson.model.data.properties.SetProperties;

/**
 * Model describing the properties of a Set (atomic).
 */
@JsonClassDescription(
	"""
		Describes the information for a set.

		This model contains the properties of the set."""
)
@JsonPropertyOrder(
	{
		CommonProperties.ID,
		SetProperties.PREFIX,
		SetProperties.NAME,
		SetProperties.TYPE,
		SetProperties.SERIES,
		LanguageProperties.LOCALIZED_DATA,
	}
)
public class Set {

	// container for set text related properties
	private SetText setTex;
	private UUID id;
	private String type;
	private String series;
	private SetLocalizedData localizedData;

	/**
	 * Return the set-text as a whole.
	 * <br>
	 * Note: this method is not serialized as JSON,
	 * as each individual getter/setter has its own field.
	 *
	 * @return card-text as a model
	 */
	@JsonIgnore // using json ignore but add the methods for the annotations+
	public SetText getSetText() {
		return setTex;
	}

	/**
	 * UUID (v5) for the set information (not the prints) generated by YGOJSON.
	 */
	@JsonPropertyDescription(
		"UUID (v5) for the Set (atomic) generated by YGOJSON."
	)
	@NotNull @JsonProperty(value = CommonProperties.ID, required = true)
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public UUID getId() {
		return id;
	}

	/**
	 * Prefix of the set in English (if available).
	 */
	@JsonPropertyDescription("Prefix of the set in English (if available).")
	@JsonProperty(value = SetProperties.PREFIX)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-zA-Z0-9]+-[a-zA-Z0-9]+")
	public String getPrefix() {
		return setTex.getPrefix();
	}

	/**
	 * Name of the set in English (if available).
	 */
	@JsonPropertyDescription("Name of the set in English (if available).")
	@JsonProperty(value = SetProperties.NAME)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public String getName() {
		return setTex.getName();
	}

	/**
	 * Type of the set (lower-case).
	 */
	@JsonPropertyDescription("Type of the set (lower-case).")
	@JsonProperty(value = SetProperties.TYPE)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-z-0-9]+")
	public String getType() {
		return type;
	}

	/**
	 * Series of the set (lower-case).
	 */
	@JsonPropertyDescription("Series of the set (lower-case).")
	@JsonProperty(value = SetProperties.SERIES)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Pattern(regexp = "[a-z-0-9]+")
	public String getSeries() {
		return series;
	}

	/**
	 * Localized data for the set.
	 */
	@JsonPropertyDescription(
		"""
			Describes translations and locale-specific data for a set.

			All translated properties match the name of the original property
			and any missing property indicates no translation for it."""
	)
	@JsonProperty(value = LanguageProperties.LOCALIZED_DATA)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public SetLocalizedData getLocalizedData() {
		return localizedData;
	}

	@JsonIgnore // required for the jsonschema generator to be sure that it is not serialized
	public void setSetTex(SetText setTex) {
		this.setTex = setTex;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public void setLocalizedData(SetLocalizedData localizedData) {
		this.localizedData = localizedData;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Set set)) return false;
		return (
			Objects.equals(setTex, set.setTex) &&
			Objects.equals(id, set.id) &&
			Objects.equals(type, set.type) &&
			Objects.equals(series, set.series) &&
			Objects.equals(localizedData, set.localizedData)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(setTex, id, type, series, localizedData);
	}
}
