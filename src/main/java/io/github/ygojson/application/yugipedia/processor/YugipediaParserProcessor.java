package io.github.ygojson.application.yugipedia.processor;

import java.util.List;
import java.util.Map;

import io.smallrye.mutiny.Multi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.yugipedia.YugipediaException;
import io.github.ygojson.application.yugipedia.client.response.Page;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;
import io.github.ygojson.application.yugipedia.client.response.Revision;
import io.github.ygojson.application.yugipedia.parser.YugipediaParser;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;

/**
 * Yugipedia {@link QueryResponse} processor with a parser.
 */
class YugipediaParserProcessor implements YugipediaProcessor {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final YugipediaParser parser;

	/**
	 * Default constructor.
	 *
	 * @param parser parser to be used.
	 */
	public YugipediaParserProcessor(final YugipediaParser parser) {
		this.parser = parser;
	}

	/**
	 * Process the query response.
	 *
	 * @param queryResponse the actual response
	 *
	 * @return the result as a stream; {@code Stream#empty()} the response cannot be processed.
	 * @throws YugipediaException if there is an unexpected error
	 */
	@Override
	public final Multi<Map<String, YugipediaProperty>> processQuery(
		QueryResponse queryResponse
	) {
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
			return Multi.createFrom().empty();
		}

		return Multi
			.createFrom()
			.items(pages.stream())
			.map(this::processPage)
			.filter(props -> !props.isEmpty());
	}

	private Map<String, YugipediaProperty> processPage(final Page page) {
		final long pageId = page.pageid();
		final String title = page.title();
		if (page.missing()) {
			return invalidProperties(pageId, title, "missing page");
		}
		if (page.revisions() == null || page.revisions().isEmpty()) {
			return invalidProperties(pageId, title, "no revisions");
		}
		final Revision revision = page.revisions().getFirst();
		if (!isWikitext(revision)) {
			final String invalidFormatMsg = String.format(
				"contentformat=%s contentmodel=%s",
				revision.contentformat(),
				revision.contentmodel()
			);
			return invalidProperties(pageId, title, invalidFormatMsg);
		}
		// TODO: should we provide a way to filter pages to avoid parsing?
		try {
			return parser.parse(title, pageId, revision.content());
		} catch (YugipediaException e) {
			log.debug("Ignored error", e);
			final String ignoredMsg = String.format("ignored (%s)", e.getMessage());
			return invalidProperties(pageId, title, ignoredMsg);
		}
	}

	protected Map<String, YugipediaProperty> invalidProperties(
		final long pageId,
		final String title,
		final String msg
	) {
		log.warn(
			"Page with id={} and title={} cannot be processed into a {}: {}",
			pageId,
			title,
			parser.getName(),
			msg
		);
		return Map.of();
	}

	private boolean isWikitext(final Revision revision) {
		// from https://www.mediawiki.org/wiki/API:Parsing_wikitext
		return (
			Revision.WIKITEXT_CONTENTFORMAT.equals(revision.contentformat()) &&
			Revision.WIKITEXT_CONTENTMODEL.equals(revision.contentmodel())
		);
	}
}
