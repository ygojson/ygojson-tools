package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model to represent all possible continue properties from a Yugipedia API response
 * used on YGOJSON.
 * <br>
 * Note that not all of them are used for the same return type.
 */
public record Continue(
	String gcmcontinue,

	String grccontinue,

	String geicontinue,

	// should use jackson annotation as it is a reserved keyword
	@JsonProperty("continue") String continueProperty
) {
	/**
	 * Sugar-syntax for {@link #continueProperty()}
	 * as it is a reserved keyword.
	 *
	 * @return same as {@link #continueProperty()}
	 */
	public String getContinue() {
		return continueProperty();
	}
}
