package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model to represent all possible continue properties from a Yugipedia API response
 * used on YGOJSON.
 * <br>
 * Note that not all of them are used for the same return type.
 */
public class Continue {

	public String gcmcontinue;

	public String grccontinue;

	@JsonProperty("continue")
	public String continueProperty;
}
