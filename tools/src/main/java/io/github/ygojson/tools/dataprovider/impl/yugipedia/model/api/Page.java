package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api;

import java.util.List;

public class Page {

	private long pageid;
	private int ns;
	private String title;
	private List<Revision> revisions;
	private List<Categories> categories;

	public long getPageid() {
		return pageid;
	}

	public int getNs() {
		return ns;
	}

	public String getTitle() {
		return title;
	}

	public List<Revision> getRevisions() {
		return revisions;
	}

	public List<Categories> getCategories() {
		return categories;
	}
}
