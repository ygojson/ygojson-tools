package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.util.List;

public record Page(
	long pageid,
	int ns,
	boolean missing,
	String title,
	List<Revision> revisions,
	List<Categories> categories
) {}
