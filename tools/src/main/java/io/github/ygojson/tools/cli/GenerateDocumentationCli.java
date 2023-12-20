package io.github.ygojson.tools.cli;

import java.nio.file.Path;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import io.github.ygojson.tools.common.YgoJsonTool;
import io.github.ygojson.tools.documentation.GenerateDocsTool;

/**
 * CLI for the {@link GenerateDocsTool}.
 */
@Component
@CommandLine.Command(
	name = "documentation",
	description = "generates the documentation for the YGOJSON models"
)
public class GenerateDocumentationCli
	extends CliCommand<GenerateDocsTool.Input, GenerateDocsTool> {

	private final GenerateDocsTool tool;

	public GenerateDocumentationCli(final GenerateDocsTool tool) {
		this.tool = tool;
	}

	@CommandLine.Spec
	CommandLine.Model.CommandSpec spec;

	@CommandLine.Option(
		names = { "-o", "--outputPath" },
		description = "Output path for the schema files",
		required = true
	)
	Path outputPath;

	@CommandLine.Option(
		names = { "--schemaIdPrefix" },
		description = "Prefix for the schema IDs (default: '${DEFAULT-VALUE}')"
	)
	String schemaPrefix = "/";

	@CommandLine.Option(
		names = { "--force" },
		description = "Overwrite outputs if they already exists (default: '${DEFAULT-VALUE}')",
		negatable = true
	)
	public boolean force;

	@Override
	protected CommandLine.Model.CommandSpec getCmdSpec() {
		return spec;
	}

	@Override
	protected GenerateDocsTool.Input buildInput() {
		return new GenerateDocsTool.Input(outputPath, schemaPrefix, force);
	}

	@Override
	protected YgoJsonTool<GenerateDocsTool.Input> getTool() {
		return tool;
	}
}
