package io.github.ygojson.model.data;

import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

/**
 * Model representing the version information of the YGOJSON data.
 */
@JsonClassDescription(
	"Model representing the version information of the YGOJSON data."
)
@JsonPropertyOrder({ VersionInfo.VERSION_PROPERTY, VersionInfo.DATE_PROPERTY })
@Getter
public class VersionInfo {

	public static final String VERSION_PROPERTY = "version";
	public static final String DATE_PROPERTY = "date";

	private String version;
	private ZonedDateTime date;

	/**
	 * Semantic Versioning of the YGOJSON data.
	 */
	@JsonPropertyDescription(
		"""
			Semantic Versioning of the YGOJSON data and files.

			Major version represents breaking changes on the data model and minor versions non-breaking changes.
			Patch versions are non-breaking data updates."""
	)
	@JsonProperty(value = VERSION_PROPERTY, required = true)
	@JsonInclude(JsonInclude.Include.ALWAYS)
	@Pattern(regexp = "[0-9]\\.[0-9]\\.[0-9]")
	public String getVersion() {
		return version;
	}

	/**
	 * Date and time in ISO-8601 (UTC) of the YGOJSON data build.
	 */
	@JsonPropertyDescription(
		"""
			Date and time of the YGOJSON data build.

			Note that as all dates in YGOJSON, the format is ISO-8601 (UTC)."""
	)
	@JsonProperty(value = DATE_PROPERTY, required = true)
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public ZonedDateTime getDate() {
		return date;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setDate(ZonedDateTime date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VersionInfo that)) return false;
		return (
			Objects.equals(version, that.version) && Objects.equals(date, that.date)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, date);
	}
}
