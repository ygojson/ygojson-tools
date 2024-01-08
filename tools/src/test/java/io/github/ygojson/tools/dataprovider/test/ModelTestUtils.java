package io.github.ygojson.tools.dataprovider.test;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelTestUtils {

	private ModelTestUtils() {}

	/**
	 * Extracts the serialized properties of the given model as a list of strings (sorted).
	 *
	 * @param model the model
	 *
	 * @return the serialized properties.
	 */
	public static List<String> extractSerializedProperties(final Object model) {
		return new ObjectMapper()
			.valueToTree(model)
			.properties()
			.stream()
			.map(Map.Entry::getKey)
			.sorted()
			.toList();
	}

	/**
	 * Extracts the properties of the given model as a list of strings (sorted).
	 *
	 * @param modelAsMap the model as a map
	 *
	 * @return the properties
	 */
	public static List<String> extractProperties(
		final Map<String, ?> modelAsMap
	) {
		return modelAsMap.keySet().stream().sorted().toList();
	}
}
