package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * Response for the Yugipedia Query API.
 */
@Data
public class QueryResponse {

	private boolean batchcomplete;

	@JsonProperty("continue")
	private Continue continueInfo;

	private Query query;

	/**
	 * Untyped warnings.
	 */
	private JsonNode warnings;

	@Data
	public static class Query {

		/**
		 * Untyped redirects.
		 */
		private JsonNode redirects;

		private List<Page> pages;
	}

	public JsonNode limits;
}
