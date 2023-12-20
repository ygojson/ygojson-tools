package io.github.ygojson.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import picocli.CommandLine;

import io.github.ygojson.tools.cli.ParentCli;
import io.github.ygojson.tools.common.YgoJsonTool;

/**
 * Entry point of the application.
 */
@SpringBootApplication
@ComponentScan(
	includeFilters = {
		@ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			value = { YgoJsonTool.class }
		),
	}
)
public class Application implements CommandLineRunner, ExitCodeGenerator {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	private final CommandLine.IFactory factory;
	private final ParentCli parentCli;
	private int exitCode = ParentCli.UNEXPECTED_ERROR_CODE;

	public Application(
		final ParentCli parentCli,
		final CommandLine.IFactory factory
	) {
		this.parentCli = parentCli;
		this.factory = factory;
	}

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
