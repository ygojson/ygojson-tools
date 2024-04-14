package io.github.ygojson.application.yugipedia.client.response;

import java.util.List;

public record Page(
	long pageid,
	int ns,
	boolean missing,
	String title,
	List<Revision> revisions,
	List<Categories> categories
) {}
