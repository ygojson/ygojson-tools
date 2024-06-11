package io.github.ygojson.cli.hidden.yugipedia.cmd.client;

import java.util.concurrent.Callable;

import picocli.CommandLine;

import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.client.params.Timestamp;
import io.github.ygojson.cli.hidden.yugipedia.cmd.client.converter.LimitConverter;
import io.github.ygojson.cli.hidden.yugipedia.cmd.client.converter.TimestampConverter;

@CommandLine.Command(
	name = "recentchanges",
	description = "Fetch recent changes"
)
public class RecentChangesCommand implements Callable<Integer> {

	@CommandLine.ParentCommand
	ClientCommand parent;

	@CommandLine.Option(names = { "--limit" }, converter = LimitConverter.class)
	Limit limit = Limit.getDefault();

	@CommandLine.Option(
		names = { "--start" },
		converter = TimestampConverter.class
	)
	Timestamp start;

	@CommandLine.Option(names = { "--end" }, converter = TimestampConverter.class)
	Timestamp end;

	@CommandLine.Option(names = { "--continue" })
	String token;

	@Override
	public Integer call() throws Exception {
		return parent.clientCall(client ->
			client.queryRecentChanges(limit, start, end, token)
		);
	}
}
