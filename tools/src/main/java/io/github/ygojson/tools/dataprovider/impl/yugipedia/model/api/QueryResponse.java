package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response for the Yugipedia Query API.
 */
public class QueryResponse {

	public boolean batchcomplete;

	@JsonProperty("continue")
	public Continue continueInfo;

	public Query query;

	/**
	 * Untyped warnings.
	 */
	public JsonNode warnings;

	public static class Query {

		/**
		 * Untyped redirects.
		 */
		public JsonNode redirects;

		public List<Page> pages;
	}

	public JsonNode limits;
}
