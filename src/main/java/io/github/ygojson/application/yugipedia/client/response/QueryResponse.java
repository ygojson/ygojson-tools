package io.github.ygojson.application.yugipedia.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response for the Yugipedia Query API.
 *
 * @param batchcomplete
 * @param continueInfo
 * @param query
 * @param warnings untyped warnings
 * @param limits
 */
public record QueryResponse(
	boolean batchcomplete,

	// should use jackson annotation as it is a reserved keyword
	@JsonProperty("continue") Continue continueInfo,
	Query query,
	JsonNode error,
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
