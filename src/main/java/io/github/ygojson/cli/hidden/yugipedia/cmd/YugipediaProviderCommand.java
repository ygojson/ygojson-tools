package io.github.ygojson.cli.hidden.yugipedia.cmd;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import io.github.ygojson.application.yugipedia.YugipediaProvider;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.cli.hidden.yugipedia.option.ModelOption;

@CommandLine.Command(
	name = "yugipedia-provider",
	mixinStandardHelpOptions = true,
	description = "Interact with the YGOJSON-provider for Yugipedia",
	hidden = true
)
public class YugipediaProviderCommand implements Callable<Integer> {

	private static final Logger LOG = LoggerFactory.getLogger(
		YugipediaProviderCommand.class
	);

	@Inject
	private ObjectMapper mapper;

	@Inject
	private YugipediaProvider provider;

	@CommandLine.Option(
		names = { "--output" },
		description = "Output file",
		required = true
	)
	Path output;

	@CommandLine.Option(
		names = { "--pretty" },
		description = "Pretty-print the output. Default: ${DEFAULT-VALUE}"
	)
	boolean prettyPrint = false;

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
		final Stream<Map<String, YugipediaProperty>> fetched = fetchData();
		try (final OutputStream outputStream = Files.newOutputStream(output)) {
			writeOutput(outputStream, fetched);
		} catch (IOException e) {
			LOG.error("Failed to write output", e);
			return 1;
		}
		return 0;
	}

	private Stream<Map<String, YugipediaProperty>> fetchData() {
		Stream<Map<String, YugipediaProperty>> fetched =
			switch (model) {
				case sets -> provider.fetchSets();
				case cards -> throw new RuntimeException("Not implemented");
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
		return fetched;
	}

	public void writeOutput(
		final OutputStream outputStream,
		final Stream<?> objects
	) throws IOException {
		try (final JsonGenerator generator = createGenerator(outputStream)) {
			generator.writeStartArray();
			final Iterator<?> iter = objects.iterator();
			for (var o = iter.next(); iter.hasNext(); o = iter.next()) {
				generator.writeObject(o);
			}
			generator.writeEndArray();
		}
	}

	protected JsonGenerator createGenerator(final OutputStream outputStream)
		throws IOException {
		final JsonGenerator generator = mapper
			.getFactory()
			.createGenerator(
				new PrintWriter(outputStream, true, StandardCharsets.UTF_8)
			);
		if (prettyPrint) {
			generator.useDefaultPrettyPrinter();
		}
		return generator;
	}
}
