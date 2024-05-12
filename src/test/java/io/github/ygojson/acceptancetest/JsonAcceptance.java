package io.github.ygojson.acceptancetest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.approvaltests.Approvals;
import org.opentest4j.AssertionFailedError;

public class JsonAcceptance {

	private final ObjectMapper objectMapper;

	public JsonAcceptance() {
		this(createDefaultObjectMapper());
	}

	public JsonAcceptance(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	private static ObjectMapper createDefaultObjectMapper() {
		final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
		return new ObjectMapper()
			.enable(SerializationFeature.INDENT_OUTPUT)
			.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			.setDefaultPrettyPrinter(prettyPrinter);
	}

	public void verify(final String testCase, final Object object) {
		verify(testCase, convert(object));
	}

	private void verify(final String testCase, final String asJsonString) {
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters()
				.forFile()
				.withBaseName(testCase)
				.forFile()
				.withExtension(".json")
		);
	}

	private String convert(final Object object) {
		try {
			return objectMapper
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new AssertionFailedError("Cannot serialize object: " + object);
		}
	}
}
