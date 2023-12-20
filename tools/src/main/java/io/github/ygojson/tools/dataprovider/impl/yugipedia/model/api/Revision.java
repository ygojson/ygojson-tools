package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.time.ZonedDateTime;

public record Revision(
	ZonedDateTime timestamp,
	String contentformat,
	String contentmodel,
	String content
) {}
