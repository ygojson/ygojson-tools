package io.github.ygojson.application.yugipedia.client.params;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Timestamp parameter for Yugipedia API.
 * <br>
 * Contains some convenience factory-methods to create yugipedia-formatted
 * timestamps.
 */
public class Timestamp {

	private final String stringValue;

	private Timestamp(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * Return the timestamp parameter of "now"
	 *
	 * @return timestamp
	 */
	public static Timestamp ofNow() {
		return new Timestamp("now");
	}

	/**
	 * Return the timestamp parameter of a ZonedDateTime
	 *
	 * @param zonedDateTime timestamp
	 *
	 * @return timestamp
	 */
	public static Timestamp of(final ZonedDateTime zonedDateTime) {
		final String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
			zonedDateTime
		);
		return new Timestamp(timestamp);
	}

	@Override
	public String toString() {
		return stringValue;
	}
}
