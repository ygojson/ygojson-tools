package io.github.ygojson.application.yugipedia.client.response;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @param redirects untyped redirects
 * @param pages
 */
public record Query(JsonNode redirects, List<Page> pages) {}
