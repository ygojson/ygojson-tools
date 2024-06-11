package io.github.ygojson.cli.hidden.yugipedia.cmd.client.converter;

import picocli.CommandLine;

import io.github.ygojson.application.yugipedia.client.params.Limit;

/**
 * Converter for the {@link Limit} on the command line.
 */
public class LimitConverter implements CommandLine.ITypeConverter<Limit> {

	@Override
	public Limit convert(String value) throws Exception {
		return Limit.of(Integer.parseInt(value));
	}
}
