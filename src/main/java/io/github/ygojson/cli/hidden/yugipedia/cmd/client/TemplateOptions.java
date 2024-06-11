package io.github.ygojson.cli.hidden.yugipedia.cmd.client;

import picocli.CommandLine;

import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.client.params.Template;
import io.github.ygojson.cli.hidden.yugipedia.cmd.client.converter.LimitConverter;

public class TemplateOptions {

	@CommandLine.Option(names = { "--limit" }, converter = LimitConverter.class)
	Limit limit;

	@CommandLine.Option(
		names = { "--template" },
		description = "Template to fetch. Valid values: ${COMPLETION-CANDIDATES}",
		required = true
	)
	Template template;

	@CommandLine.Option(names = { "--continue" })
	String token;
}
