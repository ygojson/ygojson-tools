package io.github.ygojson.cli.hidden;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import picocli.CommandLine;

import io.github.ygojson.cli.hidden.yugipedia.cmd.client.ClientCommand;
import io.github.ygojson.cli.hidden.yugipedia.cmd.provider.ProviderCommand;
import io.github.ygojson.cli.hidden.yugipedia.output.YugipediaOutputWriter;

@CommandLine.Command(
	name = "yugipedia",
	description = "Tools related with the Yugipedia integration",
	mixinStandardHelpOptions = true,
	scope = CommandLine.ScopeType.INHERIT,
	// this command is hidden by default
	hidden = true,
	subcommands = {
		CommandLine.HelpCommand.class, ClientCommand.class, ProviderCommand.class,
	}
)
public class YugipediaCommand {

	@Inject
	private ObjectMapper mapper;

	@CommandLine.Option(
		names = { "--pretty" },
		description = "Pretty-print the output. Default: ${DEFAULT-VALUE}",
		scope = CommandLine.ScopeType.INHERIT
	)
	boolean prettyPrint = false;

	/**
	 * Gets the output writer.
	 *
	 * @return the output writer
	 */
	public YugipediaOutputWriter getOutputWriter() {
		return new YugipediaOutputWriter(prettyPrint, mapper);
	}
}
