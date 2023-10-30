package io.github.ygojson.model.acceptance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.jakarta.customProperties.ValidationSchemaFactoryWrapper;
import io.github.ygojson.model.atomic.card.Card;
import io.github.ygojson.model.utils.JsonUtils;
import org.approvaltests.Approvals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Validates that the schemas are stable.
 * </br>
 * If these tests fail, the schema have changes and this shows that
 * our model have been modified, so the YGJSON version should be updated.
 * </br>
 * We would like to keep our model backwards compatible and this tests ensures that.
 */
public class SchemaValidationTest {

    /**
     * This class is here to avoid checking if the schema has
     * changed only based on the documentation description and
     * class name.
     */
    public abstract class JsonSchemaWithoutDescriptionMixIn {

        @JsonIgnore
        @JsonProperty("id")
        private String id;

        @JsonIgnore
        @JsonProperty("description")
        private String description;
    }

    public static String toTestSchema(final Class<?> type) throws IOException {
        final JsonSchema schema =  new JsonSchemaGenerator(
                JsonUtils.getObjectMapper(),
                new ValidationSchemaFactoryWrapper() // include the validation wrapper
        ).generateSchema(type);
        // for the schema we don't need the ObjectMapper from YGOJSON, and we need to modify it
        // to include a mix-in
        return new ObjectMapper() //
                .addMixIn(JsonSchema.class, JsonSchemaWithoutDescriptionMixIn.class) //
                .writerWithDefaultPrettyPrinter() //
                .writeValueAsString(schema);
    }

    static Stream<Class<?>> getAtomicSchemaClasses() {
        return Stream.of(
            Card.class
        );
    }

    @ParameterizedTest
    @MethodSource("getAtomicSchemaClasses")
    void testAtomicSchemas(final Class<?> type) throws IOException {
        Approvals.verify(
                toTestSchema(type),
                Approvals.NAMES.withParameters(type.getSimpleName()));
    }

}
