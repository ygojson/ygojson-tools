package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class Revision {

	private ZonedDateTime timestamp;
	private String contentformat;
	private String contentmodel;
	private String content;
}
