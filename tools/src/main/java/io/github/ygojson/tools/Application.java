package io.github.ygojson.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import picocli.CommandLine;

import io.github.ygojson.tools.cli.ErrorCodes;
import io.github.ygojson.tools.cli.ParentCli;
import io.github.ygojson.tools.common.YgoJsonTool;

/**
 * Entry point of the application.
 */
@Slf4j
@SpringBootApplication
@ComponentScan(
	includeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			value = { YgoJsonTool.class }
		),
	}
)
@RequiredArgsConstructor
public class Application implements CommandLineRunner, ExitCodeGenerator {

	private final CommandLine.IFactory factory;
	private final ParentCli parentCli;
	private int exitCode = ErrorCodes.UNKNOWN;

	/**
	 * Entry point for the application.
	 *
	 * @param args arguments for the application.
	 */
	public static void main(String[] args) {
		final int springExitCode = SpringApplication.exit(
			SpringApplication.run(Application.class, args)
		);
		System.exit(springExitCode);
	}

	@Override
	public void run(String... args) throws Exception {
		exitCode = new CommandLine(parentCli, factory).execute(args);
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}
}
