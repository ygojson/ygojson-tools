package io.github.ygojson.application.yugipedia.parser.acceptance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.approvaltests.Approvals;

class ParseVerifier {

	private static ParseVerifier INSTANCE;

	private final ObjectMapper objectMapper;

	ParseVerifier() {
		final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
		objectMapper =
			new ObjectMapper()
				.enable(SerializationFeature.INDENT_OUTPUT)
				.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.setDefaultPrettyPrinter(prettyPrinter);
	}

	public static ParseVerifier getInstance() {
		return INSTANCE == null ? INSTANCE = new ParseVerifier() : INSTANCE;
	}

	public void verifySerializedProperties(
		final String baseName,
		final Object properties
	) throws JsonProcessingException {
		final String asJsonString = objectMapper.writeValueAsString(properties);
		// then
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters()
				.forFile()
				.withBaseName(baseName)
				.forFile()
				.withExtension(".json")
		);
	}
}
