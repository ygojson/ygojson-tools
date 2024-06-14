package io.github.ygojson.runtime;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

/**
 * Default {@link ObjectMapperCustomizer} for non-model objects.
 */
@Singleton
public class DefaultObjectMapperCustomizer implements ObjectMapperCustomizer {

	@Override
	public void customize(final ObjectMapper objectMapper) {
		// configure pretty printing to indent
		objectMapper
			// order map entries by key - not save to be used with the model-objects
			.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
			// support date/time objects
			.registerModule(new JavaTimeModule())
			.setDefaultPrettyPrinter(customPrettyPrinter());
	}

	private DefaultPrettyPrinter customPrettyPrinter() {
		// indent always with eol without \r (even in windows) and with 2 spaces
		final DefaultIndenter indenter = new DefaultIndenter("  ", "\n");
		final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentArraysWith(indenter);
		prettyPrinter.indentObjectsWith(indenter);
		return prettyPrinter;
	}
}
