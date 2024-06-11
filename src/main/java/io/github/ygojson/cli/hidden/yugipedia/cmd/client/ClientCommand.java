package io.github.ygojson.cli.hidden.yugipedia.cmd.client;

import java.io.IOException;
import java.util.function.Function;

import jakarta.inject.Inject;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import retrofit2.Call;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;
import io.github.ygojson.cli.hidden.YugipediaCommand;

@CommandLine.Command(
	name = "client",
	mixinStandardHelpOptions = true,
	scope = CommandLine.ScopeType.INHERIT,
	description = "Interact with the YGOJSON-client for Yugipedia",
	subcommands = {
		TitlesCommand.class,
		RecentChangesCommand.class,
		PagesWithTemplateCommand.class,
	}
)
public class ClientCommand {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@CommandLine.ParentCommand
	YugipediaCommand yugipediaCommand;

	@CommandLine.Option(
		names = { "--dry-run" },
		description = "Only output the request that will be performed to the standard output. Default: ${DEFAULT-VALUE}",
		scope = CommandLine.ScopeType.INHERIT
	)
	boolean dryRun = false;

	@Inject
	private YugipediaClient client;

	/**
	 * Perform the call.
	 * <br>
	 * This allows the sub-commands to simplify their implementation.
	 *
	 * @param callProvider function for the call
	 * @return the exit code
	 */
	int clientCall(
		final Function<YugipediaClient, Call<QueryResponse>> callProvider
	) {
		final Call<QueryResponse> call = callProvider.apply(client);
		printRequest(call.request());
		if (!dryRun) {
			try {
				final QueryResponse response = call.execute().body();
				yugipediaCommand.getOutputWriter().ouputSingle(response);
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return 1;
			}
		}
		return 0;
	}

	void printRequest(final Request request) {
		final StringBuilder asCurlCmd = new StringBuilder();
		asCurlCmd.append("curl -X ").append(request.method());
		request
			.headers()
			.forEach(header ->
				asCurlCmd
					.append(" -H '")
					.append(header.getFirst())
					.append(": ")
					.append(header.getSecond())
					.append("'")
			);
		// we don't expect body here
		asCurlCmd.append(" -i '").append(request.url()).append("'");
		if (dryRun) {
			System.out.println(asCurlCmd);
		} else {
			logger.info(asCurlCmd.toString());
		}
	}
}
