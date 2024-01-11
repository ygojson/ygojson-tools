package io.github.ygojson.model.data.definition.localization;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Region code/prefix used in print codes to be used.
 *
 * @see io.github.ygojson.model.data.Print#getPrintCode()
 */
public enum Region {
	/**
	 * Worldwide English (2-character code).
	 */
	EN,
	/**
	 * European English (1-character code).
	 */
	E,
	/**
	 * Australian/Oceanic English (1-character code).
	 */
	A,
	/**
	 * French (1-character code).
	 */
	F,
	/**
	 * French (2-character code).
	 */
	FR,
	/**
	 * Canadian French (1-character code).
	 */
	C,
	/**
	 * German (1-character code).
	 */
	G,
	/**
	 * German (2-character code).
	 */
	DE,
	/**
	 * Italian (1-character code).
	 */
	I,
	/**
	 * Italian (2-character code).
	 */
	IT,
	/**
	 * Portuguese (1-character code).
	 */
	P,
	/**
	 * Portuguese (2-character code).
	 */
	PT,
	/**
	 * Spanish (1-character code).
	 */
	S,
	/**
	 * Spanish (2-character code).
	 */
	SP,
	/**
	 * Japanese (2-character code).
	 */
	JP,
	/**
	 * Japanese-Asian (2-character code).
	 */
	JA,
	/**
	 * Asian-English (2-character code).
	 */
	AE,
	/**
	 * Korean (1-character code).
	 */
	K,
	/**
	 * Korean (2-character code).
	 */
	KR,
	/**
	 * Traditional Chinese (2-character code).
	 */
	TC,
	/**
	 * Simplified Chinese (2-character code).
	 */
	SC,
	/**
	 * No prefix used in the past for North American English,
	 * Japanese and Asian-English (when 1-character code was used).
	 */
	NONE;

	/**
	 * Gets the region as the code on the print.
	 *
	 * @return the code string.
	 */
	@JsonValue
	public String asCodeString() {
		if (this == NONE) {
			return "";
		}
		return name();
	}
}
