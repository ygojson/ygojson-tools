package io.github.ygojson.application.core.datastore.db.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JsonStringListAttributeConverterUnitTest {

	private static JsonStringListAttributeConverter CONVERTER;

	@BeforeAll
	static void beforeAll() {
		CONVERTER = new JsonStringListAttributeConverter();
	}

	// ensures hat the converter doesn't crash the application when cannot read the entity
	// meaning that something wrong was persisted
	@Test
	void given_invalidList_when_convertToEntityAttribute_then_returnNull() {
		// given
		final String invalid = "{100}";
		// when
		final Object result = CONVERTER.convertToEntityAttribute(invalid);
		// then
		assertNull(result);
	}
}
