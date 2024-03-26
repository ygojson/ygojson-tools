package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.tools.common.ApplicationInfo;
import io.github.ygojson.tools.dataprovider.DataProvider;
import io.github.ygojson.tools.dataprovider.domain.uuid.YgojsonIDGenerator;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.utils.client.ClientFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class SetDownloadPlaygroud {

	private static final Logger LOG = LoggerFactory.getLogger(SetDownloadPlaygroud.class);

	record Stats(AtomicLong total, AtomicLong filtered, AtomicLong noId, AtomicReference<Set> currentSet) {

		Stats() {
			this(new AtomicLong(0), new AtomicLong(0), new AtomicLong(0), new AtomicReference<>(null));
		}

		private void addFilterer() {
			filtered.incrementAndGet();
		}

		private void addNoId() {
			noId.incrementAndGet();
		}

		private void logProcessing(final Set set) {
			currentSet.set(set);
			if (total.incrementAndGet() % 500 == 0) {
				LOG.info("Processed {}/{} sets", total.get() - filtered.get(), total.get());
				LOG.info("Current set: {}", set == null ? "null" : set.getName());
			}
		}

		private void logFailure(ObjectMapper jsonMapper, Exception e) {
			String serializedSet;
			if (currentSet.get() != null) {
				try {
					serializedSet = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(currentSet.get());
				} catch (final JsonProcessingException e2) {
					LOG.error("Cannot serialize current set", e2);
					serializedSet = "cannot serialize";
				}
			} else {
				serializedSet = "no current set";
				LOG.error("Unexpected error", e);
			}
			LOG.error("Error while processing set:\n\n{}\n\nActual error", serializedSet, e);
		}

		private void logDone() {
			LOG.info("Downloaded {}/{} sets", total.get() - filtered.get(), total.get());
			LOG.warn("Sets without IDs: {}", noId.get());

		}

	}

	@Test
	void testDownload() {
		final Stats stats = new Stats();
		final ObjectMapper jsonMapper = JsonUtils.getObjectMapper();
		final ApplicationInfo applicationInfo = new ApplicationInfo("ygojson-tools", "0.0.1-test", "https://github.com/ygojson/ygojson-tools");

		try {
			final YugipediaApi yugipediaApi = new ClientFactory(jsonMapper, applicationInfo).getClient(new YugipediaConfig());
			final YgojsonIDGenerator idGenerator = new YgojsonIDGenerator();
			// setup
			final Path outputDir = Path.of("dist/sets");
			Files.createDirectories(outputDir);
			// processing
			LOG.info("Starting download...");
			LOG.info("Output dir: {}", outputDir.toAbsolutePath());
			final DataProvider provider = new YugipediaDataProvider(yugipediaApi, Limit.getMax());
			provider.fetchSets()
				.peek(stats::logProcessing)
				.map(set -> addUuidToSet(stats, set, idGenerator))
				.filter(set -> filterSet(stats, set))
				.forEach(set -> writeSet(outputDir, set, jsonMapper));

			// stats
			stats.logDone();
			LOG.info("Done");
		} catch (final Exception e) {
			stats.logFailure(jsonMapper, e);
		}
	}

	private boolean filterSet(final Stats stats, final Set set) {
		if (set == null) {
			stats.addFilterer();
			return false;
		}
		return true;
	}

	private Set addUuidToSet(final Stats stats, final Set set, final YgojsonIDGenerator idGenerator) {
		final UUID uuid = idGenerator.generate(set);
		if (YgojsonIDGenerator.NIL_UUID.equals(uuid)) {
			stats.noId().incrementAndGet();
		}
		set.setId(uuid);
		return set;
	}

	private void writeSet(Path outputDir, Set set, ObjectMapper jsonMapper) {
		final boolean nilUUID = YgojsonIDGenerator.NIL_UUID.equals(set.getId());
		final boolean emptyName = set.getName() == null || set.getName().isEmpty();
		if (nilUUID) {
			outputDir = outputDir.resolve(set.getId().toString());
		} else if (emptyName) {
			LOG.warn("Set without name: {}", set.getId());
			outputDir = outputDir.resolve("no_name");
		}
		final String fileName = (emptyName ? set.getId() : sanitizeFileName(set.getName())) + ".json";
		Path outputFile = outputDir.resolve(fileName);
		if (Files.exists(outputFile)) {
			LOG.warn("Duplicated set: {}", fileName);
			outputDir =  outputDir.resolve("duplicated");
			outputFile = outputDir.resolve(fileName);
		}

		try {

			Files.createDirectories(outputDir);
			try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
				jsonMapper.writerWithDefaultPrettyPrinter().writeValue(writer, set);
			}
		} catch (final IOException e) {
			LOG.error("Error while creating output for set {}", outputFile, e);
		}
	}


	private String sanitizeFileName(String fileName) {
		return fileName.replaceAll("[^a-zA-Z0-9\\._]+", "_");
	}


}
