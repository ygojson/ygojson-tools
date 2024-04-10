package io.github.ygojson.model.utils.test;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.ygojson.model.data.definition.localization.Region;

/**
 * Model to test region serialization/deserialization.
 */
public record RegionTestModel(@JsonProperty("regionCode") Region regionCode) {}
