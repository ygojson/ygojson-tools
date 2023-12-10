package io.github.ygojson.tools.client.yugipedia.response;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
class Revision {

	private ZonedDateTime timestamp;
	private String contentformat;
	private String contentmodel;
	private String content;
}
