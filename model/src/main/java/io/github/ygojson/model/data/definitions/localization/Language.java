package io.github.ygojson.model.data.definitions.localization;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.ygojson.model.data.properties.LanguageProperties;

/**
 * Enum values for the supported languages on YGOJSON.
 * <br>
 * These values follow the ISO 639-1 standard format.
 */
public enum Language {
	@JsonProperty(value = LanguageProperties.EN)
	EN,
	@JsonProperty(value = LanguageProperties.DE)
	DE,
	@JsonProperty(value = LanguageProperties.ES)
	ES,
	@JsonProperty(value = LanguageProperties.FR)
	FR,
	@JsonProperty(value = LanguageProperties.IT)
	IT,
	@JsonProperty(value = LanguageProperties.JA)
	JA,
	@JsonProperty(value = LanguageProperties.KO)
	KO,
	@JsonProperty(value = LanguageProperties.PT)
	PT,
	@JsonProperty(value = LanguageProperties.ZH)
	ZH,
}
