package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.util.List;

public class Page {

	public long pageid;
	public int ns;
	public String title;
	public List<Revision> revisions;
	public List<Categories> categories;
}
