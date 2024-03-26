package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaException;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Page;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Revision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

/**
 * Abstract class to help constructing Yugipedia responses.
 * <br>
 * Subclasses must implement {@link #processWikitext(long, String, String)} which contains the wikitext content.
 * Optionally it also could be implemented {@link #filterPage(Page)} to pre-filter the page.
 *
 *
 * @param <T> the type of the result to be returned.
 */
abstract class QueryResponseWikitextProcessor<T> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Gets the model name that is being processed.
	 *
	 * @return the model name
	 */
	protected abstract String getModelName();

	/**
	 * Processes the wikitext content from a revision on a query-reponse single page.
	 *
	 * @param pageId id of the page being processed
	 * @param title title of the page being processed
	 * @param wikitextContent the wikitext content to be processed
	 *
	 * @return the result
	 *
	 * @throws YugipediaException if there is an error while processing the wikitext
	 */
	protected abstract Stream<T> processWikitext(
		final long pageId,
		final String title,
		final String wikitextContent
	) throws YugipediaException;

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

	/**
	 * Process the query response.
	 *
	 * @param queryResponse the actual response
	 *
	 * @return the result as a stream; {@code Stream#empty()} the response cannot be processed.
	 * @throws YugipediaException if there is an unexpected error
	 */
	public Stream<T> processQuery(QueryResponse queryResponse) {
		if (
			queryResponse.warnings() != null && !queryResponse.warnings().isEmpty()
		) {
			log.warn(
				"Warning on Yugipedia query-response: {}",
				queryResponse.warnings()
			);
		}
		final List<Page> pages = queryResponse.query().pages();
		if (pages == null || pages.isEmpty()) {
			log.warn("No pages found on query-response: {}", queryResponse);
			return Stream.empty();
		}
		return pages.stream().flatMap(this::processPage);
	}

	private final Stream<T> processPage(final Page page) {
		final long pageId = page.pageid();
		final String title = page.title();
		if (page.missing()) {
			return invalidStream(pageId, title, "missing page");
		}
		if (page.revisions() == null || page.revisions().isEmpty()) {
			return invalidStream(pageId, title, "no revisions");
		}
		final Revision revision = page.revisions().getFirst();
		if (!isWikitext(revision)) {
			final String invalidFormatMsg = String.format(
				"contentformat=%s contentmodel=%s",
				revision.contentformat(),
				revision.contentmodel()
			);
			return invalidStream(pageId, title, invalidFormatMsg);
		}
		if (filterPage(page)) {
			log.debug("Page with id={} and title={} is filtered", pageId, title);
			return Stream.empty();
		}
		try {
			return processWikitext(pageId, title, revision.content());
		} catch (YugipediaException e) {
			log.debug("Ignored error", e);
			final String ignoredMsg = String.format("ignored (%s)", e.getMessage());
			return invalidStream(pageId, title, ignoredMsg);
		}
	}

	protected Stream<T> invalidStream(
		final long pageId,
		final String title,
		final String msg
	) {
		log.warn(
			"Page with id={} and title={} cannot be processed into a {}-model: {}",
			pageId,
			title,
			getModelName(),
			msg
		);
		return Stream.empty();
	}

	private boolean isWikitext(final Revision revision) {
		// from https://www.mediawiki.org/wiki/API:Parsing_wikitext
		return (
			Revision.WIKITEXT_CONTENTFORMAT.equals(revision.contentformat()) &&
			Revision.WIKITEXT_CONTENTMODEL.equals(revision.contentmodel())
		);
	}
}
