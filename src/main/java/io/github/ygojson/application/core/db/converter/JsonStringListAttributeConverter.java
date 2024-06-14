package io.github.ygojson.application.core.db.converter;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.slf4j.LoggerFactory;

public class JsonStringListAttributeConverter
	implements AttributeConverter<List<String>, String> {

	private final TypeReference<List<String>> ref = new TypeReference<>() {};
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(final List<String> attribute) {
		try {
			return mapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			logConversionException("Cannot save DB json field: " + attribute, e);
			return null;
		}
	}

	@Override
	public List<String> convertToEntityAttribute(final String dbData) {
		try {
			return mapper.readValue(dbData, ref);
		} catch (final JsonProcessingException e) {
			logConversionException("Cannot convert DB json field", e);
			return null;
		}
	}

	private void logConversionException(
		final String msg,
		final JsonProcessingException e
	) {
		LoggerFactory.getLogger(this.getClass()).warn(msg);
		LoggerFactory.getLogger(this.getClass()).debug("Stacktrace", e);
	}
}
