package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;

/**
 * Internal filter for the {@link FetchDataProcessor} to skip some
 * {@link io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2}
 * data that should not be processed.
 */
class FetchCardFilter {


	/**
	 * Protected method to allow pre-filtering of the card.
	 * <br>
	 * This is executed on a {@link io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2}
	 * with wikitext, before
	 * {@link #processWikitext(long, String, String)} is executed.
	 *
	 * @param cardTable2 the card to pre-filter.
	 *
	 * @return {@code true} if the card is filtered; {@code false} otherwise (default).
	 */
	protected boolean filterCard(final CardTable2 cardTable2) {
		// filter the cards with
		return false;
	}

}
