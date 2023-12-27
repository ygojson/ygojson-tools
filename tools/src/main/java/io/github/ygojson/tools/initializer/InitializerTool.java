package io.github.ygojson.tools.initializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.tools.common.YgoJsonToolException;
import io.github.ygojson.tools.dataprovider.CardProvider;
import io.github.ygojson.tools.initializer.domain.InitialData;

public class InitializerTool { //implements YgoJsonTool<InitializerTool.Input> {

	private static final Logger log = LoggerFactory.getLogger(
		InitializerTool.class
	);

	private final ObjectMapper objectMapper;
	private final CardProvider provider;

	public InitializerTool(
		final ObjectMapper objectMapper,
		final CardProvider provider
	) {
		this.objectMapper = objectMapper;
		this.provider = provider;
	}

	//@Override
	public void execute(Input input) throws YgoJsonToolException {
		input.validate();
		final InitialData initialData = new InitialData(
			objectMapper,
			input.outputPath(),
			provider
		);
		// first initialize cards
		initializeCards(initialData, input);
		// then initialize the sets
		initialData.initializeSets();
	}

	private void initializeCards(
		final InitialData initialData,
		final Input input
	) {
		// TODO: in the future it should also be able to initialize from the local filesystem
		// TODO: to enable only initialization of sets
		if (input.maxCards().isPresent()) {
			final long maxCards = input.maxCards().get();
			log.warn(
				"Only fetching first {} cards - this parameter should be used only for development/testing purposes",
				maxCards
			);
			initialData.initializeCards(maxCards);
		} else {
			initialData.initializeCards();
		}
	}

	public record Input(Path outputPath, Optional<Long> maxCards) {
		private void validate() {
			if (outputPath == null) {
				throw new YgoJsonToolException.InputException(
					"Schema output directory must be provided"
				);
			}
			if (!Files.exists(outputPath)) {
				try {
					Files.createDirectories(outputPath);
				} catch (final IOException e) {
					throw new YgoJsonToolException.InputException(
						"Cannot create output directory: " + e.getMessage()
					);
				}
			}

			// TODO: implement any kind of validation
			if (maxCards.isPresent() && maxCards.get() <= 1) {
				throw new YgoJsonToolException.InputException(
					"Maximum cards should be more than 1"
				);
			}
		}
	}
}
