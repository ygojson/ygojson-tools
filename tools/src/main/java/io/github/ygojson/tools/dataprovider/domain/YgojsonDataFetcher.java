package io.github.ygojson.tools.dataprovider.domain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.tools.dataprovider.DataProvider;
import io.github.ygojson.tools.dataprovider.PrintData;

public class YgojsonDataFetcher {

	private final DataProvider dataProvider = null; // TODO: use DI
	private final UUIDGenerator uuidGenerator = new UUIDGenerator();

	private record OutputFolders(Path printPath, Path cardPath, Path setPath) {}

	public void initializeData(final Path outputFolder) {
		final PrintDataCollector collector = new PrintDataCollector();

		final OutputFolders outputFolders = preparePaths(outputFolder);
		fetchData(collector);
		addUUIDS(collector,  uuidGenerator); // TODO: update can also happen (extract?)
		collector.getPrintData()
			.filter(Objects::nonNull)
			.forEach(data -> writeOutput(outputFolders, data));

	}

	private void addUUIDS(final PrintDataCollector collector, final UUIDGenerator uuidGenerator) {
		collector.getPrintData()
			.map(data -> addUUIDs(data))
			.forEach(data -> {
				// TODO: maybe can be done in a single transaction?
				collector.updatePrint(data.print());
				collector.updateCard(data.card());
				collector.updateSet(data.set());
			});
	}

	private OutputFolders preparePaths(Path outputFolder) {
		// TODO: actually prepare paths
		return new OutputFolders(outputFolder, outputFolder, outputFolder);
	}

	private void writeOutput(OutputFolders outputFolder, PrintData data) {
		writeOutputJson(outputFolder.printPath, data.print(), Print::getId);
		writeOutputJson(outputFolder.cardPath, data.card(), Card::getId);
		writeOutputJson(outputFolder.setPath, data.set(), Set::getId);

	}

	private <T> void writeOutputJson(final Path outputFolder, final T object, final Function<T, UUID> idFunction) {
		final Path file = outputFolder.resolve(idFunction.apply(object) + ".json");
		try (final BufferedWriter writer = Files.newBufferedWriter(file)) {
			JsonUtils.getObjectMapper().writeValue(writer, object);
		} catch (final IOException e) {
			// TODO: handle
		}
	}

	private void fetchData(PrintDataCollector collector) {
		dataProvider.fetchData().forEach(collector::addPrintData);
		if (dataProvider.shouldFinalizeSets()) {
			dataProvider.finalizeSets(collector.getSets())
				.forEach(collector::updateSet);
		}
	}

	private PrintData addUUIDs(PrintData data) {
		final UUID printId = uuidGenerator.generate(data.print());
		final UUID cardId = uuidGenerator.generate(data.card());
		final UUID setId = uuidGenerator.generate(data.set());
		// TODO: handle unknown UUIDS!
		// set self IDs
		data.card().setId(cardId);
		data.set().setId(setId);
		data.print().setId(printId);
		// set print IDs
		data.print().setCardId(cardId);
		data.print().setSetId(setId);
		return data;
	}

}
