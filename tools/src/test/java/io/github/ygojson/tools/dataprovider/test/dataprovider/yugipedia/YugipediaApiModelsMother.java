package io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.instancio.Instancio;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Page;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Query;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Revision;

public class YugipediaApiModelsMother {

	private YugipediaApiModelsMother() {
		// utility class
	}

	public static QueryResponse queryResponse(final List<Page> pages) {
		return Instancio
			.of(QueryResponse.class)
			.ignore(all(JsonNode.class))
			.set(field(Query::pages), pages)
			.create();
	}

	public static QueryResponse queryResponse(final Page page) {
		return queryResponse(List.of(page));
	}

	public static Page missingPage() {
		return Instancio.of(Page.class).set(field(Page::missing), true).create();
	}

	public static Page randomPage() {
		return Instancio.create(Page.class);
	}

	public static Revision randomRevision() {
		return Instancio.create(Revision.class);
	}

	public static Revision wikitextRevision(final String content) {
		return new Revision(
			ZonedDateTime.now(),
			"text/x-wiki",
			"wikitext",
			content
		);
	}

	public static Page pageWithRevision(List<Revision> revision) {
		return Instancio
			.of(Page.class)
			.set(field(Page::missing), false)
			.set(field(Page::revisions), revision)
			.create();
	}

	public static Page pageWithRevision(Revision revision) {
		return pageWithRevision(List.of(revision));
	}
}
