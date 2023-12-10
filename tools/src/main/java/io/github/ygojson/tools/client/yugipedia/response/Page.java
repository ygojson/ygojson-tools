package io.github.ygojson.tools.client.yugipedia.response;

import java.util.List;

import lombok.Data;

@Data
public class Page {

	private long pageid;
	private int ns;
	private String title;
	private List<Revision> revisions;
	private List<Categories> categories;
}
