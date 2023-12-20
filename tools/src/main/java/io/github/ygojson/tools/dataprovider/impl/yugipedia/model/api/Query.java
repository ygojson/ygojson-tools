package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class Query {

	private JsonNode redirects;

	private List<Page> pages;

	/**
	 * Untyped redirects.
	 */
	public JsonNode getRedirects() {
		return redirects;
	}

	public List<Page> getPages() {
		return pages;
	}
}
