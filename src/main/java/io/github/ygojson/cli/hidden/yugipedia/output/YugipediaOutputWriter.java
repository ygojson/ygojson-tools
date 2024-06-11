package io.github.ygojson.cli.hidden.yugipedia.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Output writer for internal tools to explore the Yugipedia data.
 */
public class YugipediaOutputWriter {

	private final boolean prettyPrint;
	private final ObjectMapper mapper;

	public YugipediaOutputWriter(
		final boolean prettyPrint,
		final ObjectMapper mapper
	) {
		this.prettyPrint = prettyPrint;
		this.mapper = mapper;
	}

	/**
	 * Output a single object.
	 *
	 * @param object the object to write
	 */
	public void ouputSingle(Object object) throws IOException {
		try (final JsonGenerator generator = getGenerator()) {
			generator.writeObject(object);
		}
	}

	/**
	 * Output multiple objects.
	 *
	 * @param objects
	 *
	 * @throws IOException
	 */
	public void outputMultiple(Stream<?> objects) throws IOException {
		try (final JsonGenerator generator = getGenerator()) {
			generator.writeStartArray();
			final Iterator<?> iter = objects.iterator();
			for (var o = iter.next(); iter.hasNext(); o = iter.next()) {
				generator.writeObject(o);
			}
			generator.writeEndArray();
		}
	}

	protected JsonGenerator getGenerator() throws IOException {
		final JsonGenerator generator = mapper
			.getFactory()
			.createGenerator(
				new PrintWriter(System.out, true, StandardCharsets.UTF_8)
			);
		if (prettyPrint) {
			generator.useDefaultPrettyPrinter();
		}
		return generator;
	}
}
