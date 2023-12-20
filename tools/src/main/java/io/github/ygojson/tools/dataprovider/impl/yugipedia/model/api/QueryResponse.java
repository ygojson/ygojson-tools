package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response for the Yugipedia Query API.
 */
public record QueryResponse(
	boolean batchcomplete,

	// should use jackson annotation as it is a reserved keyword
	@JsonProperty("continue") Continue continueInfo,
	Query query,
	/**
	 * Untyped warnings.
	 */
	JsonNode warnings,
	JsonNode limits
) {
	/**
	 * Sugar-syntax for {@link #continueInfo()}.
	 *
	 * @return same as {@link #continueInfo()}
	 */
	public Continue getContinue() {
		return continueInfo();
	}
}
