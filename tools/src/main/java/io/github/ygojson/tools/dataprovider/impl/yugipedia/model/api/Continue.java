package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model to represent all possible continue properties from a Yugipedia API response
 * used on YGOJSON.
 * <br>
 * Note that not all of them are used for the same return type.
 */
public class Continue {

	private String gcmcontinue;

	private String grccontinue;

	// should use jackson annotation as it is a reserved keyword
	@JsonProperty("continue")
	private String continueProperty;

	public String getGcmcontinue() {
		return gcmcontinue;
	}

	public String getGrccontinue() {
		return grccontinue;
	}

	public String getContinue() {
		return continueProperty;
	}
}
