package io.github.ygojson.cli.hidden.yugipedia.cmd.client;

import java.util.concurrent.Callable;

import picocli.CommandLine;

import io.github.ygojson.application.yugipedia.client.params.PipeSeparated;

@CommandLine.Command(name = "titles", description = "Fetch titles")
public class TitlesCommand implements Callable<Integer> {

	@CommandLine.ParentCommand
	ClientCommand parent;

	@CommandLine.Option(names = { "-t", "--name" }, required = true)
	String[] titles;

	@Override
	public Integer call() throws Exception {
		return parent.clientCall(client ->
			client.queryPagesByTitle(PipeSeparated.of(titles))
		);
	}
}
