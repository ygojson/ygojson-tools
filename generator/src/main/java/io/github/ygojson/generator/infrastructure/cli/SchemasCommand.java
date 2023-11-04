package io.github.ygojson.generator.infrastructure.cli;

import io.github.ygojson.generator.application.GenerateSchemas;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.Callable;

@Slf4j
@Component
@CommandLine.Command(name = "schemas")
@RequiredArgsConstructor
public class SchemasCommand implements Callable<Integer> {

    private final GenerateSchemas schemas;

    @CommandLine.Option(
            names = {"-o", "--outputPath"},
            description = "Output path for the schema files",
            required = true
    )
    public Path outputPath;

    @Override
    public Integer call() throws Exception {
        log.info("Starting schemas command");
        final GenerateSchemas.Command cmd = new GenerateSchemas.Command(outputPath);
        final Optional<GenerateSchemas.SchemaError> optionalError = schemas.execute(cmd);
        return optionalError
                .map(error -> {
                    log.error(error.message());
                    return 1;
                })
                .orElse(0);
    }
}
