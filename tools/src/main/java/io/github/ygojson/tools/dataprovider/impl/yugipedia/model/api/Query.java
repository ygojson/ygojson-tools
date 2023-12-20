package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public record Query(
	/**
	 * Untyped redirects.
	 */
	JsonNode redirects,
	List<Page> pages
) {}
