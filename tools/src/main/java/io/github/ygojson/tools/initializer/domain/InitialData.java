package io.github.ygojson.tools.initializer.domain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.model.data.CardPrints;
import io.github.ygojson.tools.common.YgoJsonToolException;
import io.github.ygojson.tools.dataprovider.CardProvider;

/**
 * Domain model representing the initial data.
 */
public class InitialData {

	private static final Logger log = LoggerFactory.getLogger(InitialData.class);

	private final ObjectMapper jsonMapper;
	private final OutputPaths outputPath;
	private final CardProvider cardProvider;

	private Set<String> foundSets = new HashSet<>();

	public InitialData(
		final ObjectMapper jsonMapper,
		final Path outputPath,
		final CardProvider cardProvider
	) {
		this.jsonMapper = jsonMapper;
		this.outputPath = new OutputPaths(outputPath);
		this.cardProvider = cardProvider;
	}

	record OutputPaths(Path outputPath) {
		public Path cardPath() {
			return outputPath.resolve("cards");
		}
	}

	public void initializeCards() {
		initializeCardsInternal(cardProvider::getCardPrintsStream);
	}

	public void initializeSets() {
		foundSets.forEach(set -> log.info("Initialized set: {}", set));
		throw new UnsupportedOperationException("Not implemented");
	}

	public void initializeCards(final long nCards) {
		initializeCardsInternal(() ->
			cardProvider.getCardPrintsStream().limit(nCards)
		);
	}

	private void initializeCardsInternal(
		Supplier<Stream<CardPrints>> cardPrintSupplier
	) {
		initCardsFolder();
		cardPrintSupplier.get().forEach(this::processCard);
	}

	private void initCardsFolder() {
		try {
			Files.createDirectories(outputPath.cardPath());
		} catch (final IOException e) {
			throw new YgoJsonToolException(
				"Cannot create card output directory: " + outputPath.cardPath()
			);
		}
	}

	private void processCard(final CardPrints cardPrints) {
		// accumulate all the sets
		cardPrints.getPrints().forEach(print -> foundSets.add(print.getSetCode()));
		// output card
		final String filename =
			cardPrints.getCard().getIdentifiers().getYugipediaPageId() + ".json";
		final Path outputFile = outputPath.cardPath().resolve(filename);
		try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
			jsonMapper.writeValue(writer, cardPrints);
		} catch (final IOException e) {
			throw new YgoJsonToolException("Cannot write card file " + filename);
		}
	}
}
