package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.time.ZonedDateTime;

public class Revision {

	private ZonedDateTime timestamp;
	private String contentformat;
	private String contentmodel;
	private String content;

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public String getContentformat() {
		return contentformat;
	}

	public String getContentmodel() {
		return contentmodel;
	}

	public String getContent() {
		return content;
	}
}
