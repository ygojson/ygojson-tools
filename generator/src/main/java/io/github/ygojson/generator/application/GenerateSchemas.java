package io.github.ygojson.generator.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;
import io.github.ygojson.generator.annotations.UseCase;
import io.github.ygojson.model.atomic.card.Card;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@UseCase
public class GenerateSchemas {

    // not injected as this is not the YGOJSON mapper
    // and it is only used to generate write schemas
    // requiring most likely some special handling
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<SchemaError> execute(final Command command) {
        try {
            Optional<SchemaError> validation = validateCommand(command);
            if (validation.isPresent()) {
                return validation;
            }
            Files.createDirectories(command.outputPath);
            final ObjectNode objectNode = new SchemaGenerator(createSchemaConfig()).generateSchema(Card.class);
            writeOutput(objectNode, command.outputPath().resolve("card.schema.json"));
        } catch (final IOException e) {
            return Optional.of(new SchemaError(e.getMessage()));
        }
        return Optional.empty();
    }

    private void writeOutput(final Object schema, final Path outputFile) throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, schema);
        }
    }

    private Optional<SchemaError> validateCommand(Command command) {
        if (command.outputPath() == null) {
            return Optional.of(new SchemaError("Output path cannot be null"));
        }
        // TODO: check also for file and/or non-empty directory
        return Optional.empty();
    }

    private SchemaGeneratorConfig createSchemaConfig() {
        return new SchemaGeneratorConfigBuilder(objectMapper, OptionPreset.PLAIN_JSON)
                .with(configureJacksonModule())
                .with(configureJackartaModule())
                .with(Option.DEFINITIONS_FOR_ALL_OBJECTS) // could be useful
                .build();
    }

    private JacksonModule configureJacksonModule() {
        return new JacksonModule(
                JacksonOption.RESPECT_JSONPROPERTY_ORDER,
                JacksonOption.RESPECT_JSONPROPERTY_REQUIRED,
                JacksonOption.FLATTENED_ENUMS_FROM_JSONPROPERTY
        );
    }

    private JakartaValidationModule configureJackartaModule() {
        return new JakartaValidationModule(
                JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS
        );
    }

    public record SchemaError(String message) {}

    // TODO: add a path to the command
    public record Command(Path outputPath)  {}

}
