package io.github.ygojson.cli.hidden.yugipedia.cmd.provider;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import jakarta.inject.Inject;
import picocli.CommandLine;

import io.github.ygojson.application.yugipedia.YugipediaProvider;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.cli.hidden.YugipediaCommand;

@CommandLine.Command(
	name = "provider",
	mixinStandardHelpOptions = true,
	description = "Interact with the YGOJSON-provider for Yugipedia"
)
public class ProviderCommand implements Callable<Integer> {

	@CommandLine.ParentCommand
	YugipediaCommand parent;

	@Inject
	private YugipediaProvider provider;

	@CommandLine.Option(
		names = { "--model" },
		description = "Model to fetch. Valid values: ${COMPLETION-CANDIDATES}",
		required = true
	)
	ModelOption model;

	@CommandLine.Option(
		names = { "--offset" },
		description = "Offset to start the results (0-based). Default: no offset"
	)
	Optional<Long> offset = Optional.empty();

	@CommandLine.Option(
		names = { "--max" },
		description = "Maximum number of results. Default: all"
	)
	Optional<Long> max = Optional.empty();

	@Override
	public Integer call() {
		try {
			Stream<Map<String, YugipediaProperty>> fetched =
				switch (model) {
					case sets -> provider.fetchSets();
					default -> throw new IllegalArgumentException(
						"Unknown model: " + model
					);
				};
			if (offset.isPresent()) {
				fetched = fetched.skip(offset.get());
			}
			if (max.isPresent()) {
				fetched = fetched.limit(max.get() + 1);
			}
			parent.getOutputWriter().outputMultiple(fetched);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return 1;
		}
		return 0;
	}
}
