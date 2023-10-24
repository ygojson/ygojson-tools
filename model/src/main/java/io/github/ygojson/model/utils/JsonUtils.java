package io.github.ygojson.model.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilities to work with the JSON model.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    /**
     * Gets a static {@link ObjectMapper} to be re-used.
     * </br>
     * This object mapper ensures that dates and other models
     * are properly serialized/deserialized.
     *
     * @return object mapper.
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Creates an object mapper able to serialize/deserialize
     * the YGOJSON model.
     *
     * @return object-mapper configured to be used with YGOJSON.
     */
    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
