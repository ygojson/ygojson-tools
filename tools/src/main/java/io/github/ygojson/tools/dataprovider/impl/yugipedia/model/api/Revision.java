package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.time.ZonedDateTime;

public record Revision(
	ZonedDateTime timestamp,
	String contentformat,
	String contentmodel,
	String content
) {
	/**
	 * Constant representing a wikitext on the {@link #contentmodel()}.
	 */
	public static final String WIKITEXT_CONTENTMODEL = "wikitext";

	/**
	 * Constant representing a wikitext on the {@link #contentformat()}.
	 */
	public static final String WIKITEXT_CONTENTFORMAT = "text/x-wiki";
}
