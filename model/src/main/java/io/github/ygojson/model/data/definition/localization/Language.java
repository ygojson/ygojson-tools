package io.github.ygojson.model.data.definition.localization;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.ygojson.model.data.property.LanguageProperties;

/**
 * Enum values for the supported languages on YGOJSON.
 * <br>
 * These values follow the IETF BCP 47 standard format.
 */
public enum Language {
	/**
	 * English.
	 * <br>
	 * Note that this is the main language of YGOJSON.
	 */
	@JsonProperty(value = LanguageProperties.EN)
	EN,
	/**
	 * German.
	 */
	@JsonProperty(value = LanguageProperties.DE)
	DE,
	/**
	 * Spanish.
	 */
	@JsonProperty(value = LanguageProperties.ES)
	ES,
	/**
	 * French.
	 */
	@JsonProperty(value = LanguageProperties.FR)
	FR,
	/**
	 * Italian.
	 */
	@JsonProperty(value = LanguageProperties.IT)
	IT,
	/**
	 * Japanese.
	 */
	@JsonProperty(value = LanguageProperties.JA)
	JA,
	/**
	 * Korean.
	 */
	@JsonProperty(value = LanguageProperties.KO)
	KO,
	/**
	 * Portuguese.
	 */
	@JsonProperty(value = LanguageProperties.PT)
	PT,
	/**
	 * Chinese (Simplified).
	 */
	@JsonProperty(value = LanguageProperties.ZH_HANS)
	ZH_HANS,
	/**
	 * Chinese (Traditional).
	 */
	@JsonProperty(value = LanguageProperties.ZH_HANT)
	ZH_HANT,

}
