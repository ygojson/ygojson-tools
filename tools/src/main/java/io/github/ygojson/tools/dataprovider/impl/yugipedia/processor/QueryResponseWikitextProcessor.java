package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor;

import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Page;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Revision;

abstract class QueryResponseWikitextProcessor<T> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected abstract Stream<T> processWikitext(final long pageId, final String title, final String wikitextContent);

	/**
	 * Protected method to allow pre-filtering of the page.
	 * <br>
	 * This is executed on a {@link Page} with wikitext, before
	 * {@link #processWikitext(long, String, String)} is executed.
	 *
	 * @param page the page to pre-filter.
	 *
	 * @return {@code true} if the page is filtered; {@code false} otherwise (default).
	 */
	protected boolean filterPage(final Page page) {
		return false;
	}

	public Stream<T> processQuery(QueryResponse queryResponse) {
		if (queryResponse.warnings() != null && !queryResponse.warnings().isEmpty()) {
			log.warn("Warning on Yugipedia query-response: {}", queryResponse.warnings());
		}
		final List<Page> pages = queryResponse.query().pages();
		if (pages.isEmpty()) {
			log.warn("No pages found on query-repsonse: {}", queryResponse);
			return null;
		}
		return pages.stream().flatMap(this::processPage);
	}

	private final Stream<T> processPage(final Page page) {
		final long pageId = page.pageid();
		final String title = page.title();
		if (page.missing()) {
			log.warn("Page with id={} and title={} is missing", pageId, title);
			return null;
		}
		if (page.revisions() == null || page.revisions().isEmpty()) {
			log.warn("Page with id={} and title={} has no revisions", pageId, title);
			return null;
		}
		final Revision revision = page.revisions().getFirst();
		// from https://www.mediawiki.org/wiki/API:Parsing_wikitext
		// TODO: should be constants!
		if (!("text/x-wiki".equals(revision.contentformat()) && "wikitext".equals(revision.contentmodel()))) {
			log.warn("Page with id={} and title={} revision with non-wikitext format: contentformat={} contentmodel={}",
				pageId, title, revision.contentformat(), revision.contentmodel());
			return null;
		}
		if (filterPage(page)) {
			return null;
		}
		return processWikitext(pageId, title, revision.content());
	}
}
