package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Timestamp {

	private String stringValue;

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
	public static Timestamp of(ZonedDateTime zonedDateTime) {
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
