package io.github.ygojson.tools.cli;

import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import io.github.ygojson.tools.common.YgoJsonTool;
import io.github.ygojson.tools.common.YgoJsonToolException;

/**
 * Helper class to implement cli-commands for picocli based on a {@link YgoJsonTool}.
 *
 * @param <I> input type
 * @param <T> tool type
 */
abstract class CliCommand<I, T extends YgoJsonTool<I>>
	implements Callable<Integer> {

	private static final Logger log = LoggerFactory.getLogger(CliCommand.class);

	/**
	 * Builds the tool input from the command-line options.
	 *
	 * @return input for the tool.
	 */
	protected abstract I buildInput();

	/**
	 * Gets an instance of the tool to run.
	 * <br>
	 * Any special initialization should happen on the implementation and might be injected by our infrastructure.
	 *
	 * @return the instance of the tool to run.
	 */
	protected abstract YgoJsonTool<I> getTool();

	/**
	 * Gets the command-spec to be used for logging.
	 *
	 * @return command-specs.
	 */
	protected abstract CommandLine.Model.CommandSpec getCmdSpec();

	/**
	 * Gets the name of the sub-command.
	 *
	 * @return the name of the sub-command.
	 */
	private final String getCliName() {
		return getCmdSpec().name();
	}

	/**
	 * Gets the options as a string.
	 *
	 * @return string options/arguments.
	 */
	private String getCliOptsAsString() {
		return getCmdSpec()
			.commandLine()
			.getParseResult()
			.expandedArgs()
			.stream()
			.collect(Collectors.joining(" "));
	}

	private void doCall() {
		log.info(
			"Starting {} execution with options: {}",
			getCmdSpec(),
			getCliOptsAsString()
		);
		final I input = buildInput();
		getTool().execute(input);
		log.info("Successfully executed {}", getCliName());
	}

	@Override
	public final Integer call() {
		try {
			doCall();
			return CommandLine.ExitCode.OK;
		} catch (final YgoJsonToolException e) {
			log.error("Finished with errors");
			throw mapYgoJsonException(e);
		} catch (final Exception e) {
			log.error("Unexpected error", e);
			// signal unexpected erorrs
			return ParentCli.UNEXPECTED_ERROR_CODE;
		}
	}

	private CommandLine.PicocliException mapYgoJsonException(
		final YgoJsonToolException e
	) {
		log.error(e.getMessage());
		if (e instanceof YgoJsonToolException.InputException) {
			return new CommandLine.ParameterException(
				getCmdSpec().commandLine(),
				e.getMessage()
			);
		}
		return new CommandLine.ExecutionException(
			getCmdSpec().commandLine(),
			e.getMessage()
		);
	}
}
