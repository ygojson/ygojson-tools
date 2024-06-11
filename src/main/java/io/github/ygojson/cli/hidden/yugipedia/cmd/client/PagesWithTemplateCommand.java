package io.github.ygojson.cli.hidden.yugipedia.cmd.client;

import java.util.concurrent.Callable;

import picocli.CommandLine;

import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.client.params.Template;
import io.github.ygojson.cli.hidden.yugipedia.cmd.client.converter.LimitConverter;

@CommandLine.Command(
	name = "templates",
	description = "Fetch pages containing defined templates"
)
public class PagesWithTemplateCommand implements Callable<Integer> {

	@CommandLine.ParentCommand
	ClientCommand parent;

	@CommandLine.Option(names = { "--limit" }, converter = LimitConverter.class)
	Limit limit = Limit.getDefault();

	@CommandLine.Option(
		names = { "--template" },
		description = "Template to fetch. Valid values: ${COMPLETION-CANDIDATES}",
		required = true
	)
	Template template;

	@CommandLine.Option(names = { "--continue" })
	String token;

	@Override
	public Integer call() throws Exception {
		return parent.clientCall(client ->
			client.queryPagesWithTemplate(template, limit, token)
		);
	}
}
