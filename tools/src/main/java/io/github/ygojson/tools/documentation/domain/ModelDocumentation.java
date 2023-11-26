package io.github.ygojson.tools.documentation.domain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import io.github.ygojson.tools.common.YgoJsonToolException;

/**
 * Domain model representing the documention
 * of a model.
 */
@Slf4j
public class ModelDocumentation {

	@Getter
	private final Class<?> modelClass;

	@Getter
	private final String modelName;

	@Getter(AccessLevel.PACKAGE)
	private final ObjectMapper objectMapper;

	private SchemaDoc schemaDoc;

	public ModelDocumentation(
		final Class<?> modelClass,
		final ObjectMapper objectMapper
	) {
		this.modelClass = modelClass;
		this.modelName = modelClass.getSimpleName().toLowerCase();
		this.objectMapper = objectMapper;
	}

	/**
	 * Sets the schema prefix.
	 *
	 * @param prefix the schema prefix.
	 * @return this instance with the prefix set.
	 *
	 * @throws IllegalStateException if the prefix was already set.
	 */
	public ModelDocumentation setSchemaPrefix(final String prefix) {
		if (schemaDoc == null) {
			this.schemaDoc = SchemaDoc.forModel(this, prefix);
		} else {
			log.error("Cannot add prefix to model documentation twice");
			throw new IllegalStateException("Prefix already set");
		}
		return this;
	}

	/**
	 * Writes the JSON-schema documentation to the provided output directory.
	 *
	 * @param outputDir the output directory
	 * @param force if {@code true}, allow to overwrite the file if it exists; otherwise throw.
	 *
	 * @return returns the file where the JSON-schema was written
	 *
	 * @throws IOException if there is an problem writing the schema file.
	 */
	public Path writeJsonSchemaTo(final Path outputDir, final boolean force)
		throws YgoJsonToolException {
		// initialize the schemaDoc if null
		if (schemaDoc == null) {
			setSchemaPrefix("");
		}
		final Path outputFile = outputDir.resolve(schemaDoc.getSchemaFilename());
		if (!force && Files.exists(outputFile)) {
			throw new YgoJsonToolException("Schema file exists " + outputFile);
		}
		log.info("Generating schema {}", schemaDoc.getFullSchemaId());
		final ObjectNode schema = schemaDoc.generateSchema();
		try {
			try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
				log.info("Writing file: {}", outputFile);
				objectMapper
					.writerWithDefaultPrettyPrinter()
					.writeValue(writer, schema);
			}
		} catch (final IOException e) {
			throw new YgoJsonToolException(
				"Unable to write schema file: " + outputFile,
				e
			);
		}
		return outputFile;
	}
}
