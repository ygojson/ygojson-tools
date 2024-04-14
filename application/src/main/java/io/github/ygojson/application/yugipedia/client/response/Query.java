package io.github.ygojson.application.yugipedia.client.response;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public record Query(
	/**
	 * Untyped redirects.
	 */
	JsonNode redirects,
	List<Page> pages
) {}
