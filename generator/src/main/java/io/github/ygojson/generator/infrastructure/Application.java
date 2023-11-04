package io.github.ygojson.generator.infrastructure;

import io.github.ygojson.generator.annotations.UseCase;
import io.github.ygojson.generator.infrastructure.cli.ParentCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import picocli.CommandLine;

@Slf4j
@SpringBootApplication
@ComponentScan(
        basePackages = "io.github.ygojson.generator",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {UseCase.class})
        }
)
@RequiredArgsConstructor
public class Application implements CommandLineRunner, ExitCodeGenerator {

    private final CommandLine.IFactory factory;
    private final ParentCommand command; // TODO: make a type command
    private int exitCode = 0;

    /**
     * Entry point for the application.
     *
     * @param args arguments for the application.
     */
    public static void main(String[] args) {
        final int springExitCode = SpringApplication.exit(SpringApplication.run(Application.class, args));
        System.exit(springExitCode);
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(command, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
