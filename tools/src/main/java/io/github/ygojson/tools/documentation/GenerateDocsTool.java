package io.github.ygojson.tools.documentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.tools.common.YgoJsonTool;
import io.github.ygojson.tools.common.YgoJsonToolException;
import io.github.ygojson.tools.documentation.domain.ModelDocumentation;

@Slf4j
@RequiredArgsConstructor
public class GenerateDocsTool implements YgoJsonTool<GenerateDocsTool.Input> {

	private static final Set<Class<?>> MODEL_DOCUMENTATION_CLASSES = Set.of(
		io.github.ygojson.model.data.Card.class,
		io.github.ygojson.model.data.Set.class,
		io.github.ygojson.model.data.Print.class,
		io.github.ygojson.model.data.VersionInfo.class
	);

	private final ObjectMapper objectMapper;

	@Override
	public void execute(final Input input) throws YgoJsonToolException {
		input.validate();
		doExecute(input);
	}

	private void doExecute(final Input input) throws YgoJsonToolException {
		log.info("Starting model documentation");
		final List<ModelDocumentation> modelDocs = MODEL_DOCUMENTATION_CLASSES
			.stream()
			.map(modelClass ->
				new ModelDocumentation(modelClass, objectMapper)
					.setSchemaPrefix(input.schemaIdPrefix())
			)
			.toList();
		for (final ModelDocumentation modelDoc : modelDocs) {
			log.info("Generating schema for {}", modelDoc.getModelName());
			modelDoc.writeJsonSchemaTo(input.schemaOutputDir(), input.forceOutput());
		}
	}

	/**
	 * {@link GenerateDocsTool} input.
	 *
	 * @param schemaOutputDir path for the schema output (cannot be {@code null}).
	 * @param schemaIdPrefix prefix for the schema-id (cannot be {@code null}).
	 * @param forceOutput if {@code true}, allow over-writing generated files
	 *                    if {@code false}, fail if a file to be written is encountered.
	 */
	public record Input(
		Path schemaOutputDir,
		String schemaIdPrefix,
		boolean forceOutput
	) {
		/**
		 * Validates the input.
		 *
		 * @return optional error.
		 */
		private void validate() {
			if (schemaOutputDir == null) {
				throw new YgoJsonToolException.InputException(
					"Schema output directory must be provided"
				);
			}
			if (schemaIdPrefix == null) {
				throw new YgoJsonToolException.InputException(
					"ID prefix must be provided"
				);
			}
			if (!Files.exists(schemaOutputDir)) {
				try {
					Files.createDirectories(schemaOutputDir);
				} catch (final IOException e) {
					throw new YgoJsonToolException.InputException(
						"Cannot create output directory: " + e.getMessage()
					);
				}
			}
		}
	}
}
