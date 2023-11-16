package io.github.ygojson.model.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

/**
 * Model representing the version information of the YGOJSON data.
 */
@JsonClassDescription(
        "Model representing the version information of the YGOJSON data."
)
@JsonPropertyOrder({
        VersionInfo.VERSION_PROPERTY,
        VersionInfo.DATE_PROPERTY
})
@Getter
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@JsonPOJOBuilder
public class VersionInfo {

    public static final String VERSION_PROPERTY = "version";
    public static final String DATE_PROPERTY = "date";

    /**
     * Semantic Versioning of the YGOJSON data.
     */
    @JsonPropertyDescription(
            """
            Semantic Versioning of the YGOJSON data and files.
            
            Major version represents breaking changes on the data model and minor versions non-breaking changes.
            Patch versions are non-breaking data updates."""
    )
    @NotNull
    @JsonProperty(value = VERSION_PROPERTY, required = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @Pattern(regexp = "[0-9]\\.[0-9]\\.[0-9]")
    private String version;

    /**
     * Date and time in ISO-8601 (UTC) of the YGOJSON data build.
     */
    @JsonPropertyDescription(
            """
            Date and time of the YGOJSON data build.
            
            Note that as all dates in YGOJSON, the format is ISO-8601 (UTC)."""
    )
    @NotNull
    @JsonProperty(value = DATE_PROPERTY, required = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private ZonedDateTime date;

}
