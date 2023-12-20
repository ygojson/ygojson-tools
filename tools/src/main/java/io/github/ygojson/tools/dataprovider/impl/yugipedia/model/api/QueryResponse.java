package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response for the Yugipedia Query API.
 */
public class QueryResponse {

	private boolean batchcomplete;

	// should use jackson annotation as it is a reserved keyword
	@JsonProperty("continue")
	private Continue continueInfo;

	private Query query;

	private JsonNode warnings;

	private JsonNode limits;

	public boolean isBatchcomplete() {
		return batchcomplete;
	}

	public Continue getContinue() {
		return continueInfo;
	}

	public Query getQuery() {
		return query;
	}

	/**
	 * Untyped warnings.
	 */
	public JsonNode getWarnings() {
		return warnings;
	}

	public JsonNode getLimits() {
		return limits;
	}
}
