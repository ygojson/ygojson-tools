package io.github.ygojson.cli.hidden.yugipedia.cmd.client.converter;

import java.time.ZonedDateTime;

import picocli.CommandLine;

import io.github.ygojson.application.yugipedia.client.params.Timestamp;

/**
 * Converts a string to a {@link Timestamp}
 */
public class TimestampConverter
	implements CommandLine.ITypeConverter<Timestamp> {

	@Override
	public Timestamp convert(String value) throws Exception {
		return Timestamp.of(ZonedDateTime.parse(value));
	}
}
