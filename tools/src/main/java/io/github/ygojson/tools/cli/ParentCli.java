package io.github.ygojson.tools.cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * Parent CLI definiton.
 * <br>
 * All ygojson-tools are sub-commands and this one should not contain any logic.
 */
@Component
@CommandLine.Command(
	name = "ygojson-tools",
	description = "tools for generate, document and explore the YGOJSON data",
	subcommands = {
		CommandLine.HelpCommand.class, GenerateDocumentationCli.class,
	}
)
public final class ParentCli implements Runnable {

	@CommandLine.Spec
	CommandLine.Model.CommandSpec spec;

	@Override
	public final void run() {
		throw new CommandLine.ParameterException(
			spec.commandLine(),
			"Missing required subcommand"
		);
	}
}
