package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.CardPrints;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.tools.dataprovider.CardProvider;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.CardTable2Mapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaCardMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaPrintMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.CardTable2;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.QueryResponse;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Revision;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Category;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.SortDirection;
import io.github.ygojson.tools.dataprovider.utils.PaginatorStreamFactory;

public class YugipediaDataProvider implements CardProvider {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final CardTable2Mapper CARDTABLE2_MAPPER = Mappers.getMapper(
		CardTable2Mapper.class
	);
	private final YugipediaCardMapper CARD_MAPPER = Mappers.getMapper(
		YugipediaCardMapper.class
	);
	private final YugipediaPrintMapper PRINT_MAPPER = Mappers.getMapper(
		YugipediaPrintMapper.class
	);
	// injected
	private final YugipediaApi api;

	private final Limit providerLimit;

	public YugipediaDataProvider(
		final YugipediaApi api,
		final Limit providerLimit
	) {
		this.api = api;
		this.providerLimit = providerLimit;
	}

	@Override
	public Stream<CardPrints> getCardPrintsStream() {
		return PaginatorStreamFactory
			.create(new YugipediaCardHandller())
			.flatMap(response -> response.query().pages().stream())
			.map(page ->
				processCardPrintRevision(
					page.pageid(),
					page.title(),
					page.revisions().getFirst()
				)
			)
			.filter(Objects::nonNull);
	}

	private CardPrints processCardPrintRevision(
		long pageid,
		String title,
		Revision revision
	) {
		if (revision.contentformat() != "wikitext") {
			log.warn("Ignoring yugipedia revision without wikitext format");
			return null;
		}
		final CardTable2 cardTable2 = CARDTABLE2_MAPPER.mapWikitextToCardTable2(
			revision.content()
		);
		final Card card = CARD_MAPPER.mapToCard(cardTable2, title, pageid);
		final List<Print> prints = PRINT_MAPPER.mapToPrints(cardTable2);
		final CardPrints cardPrints = new CardPrints();
		cardPrints.setCard(card);
		cardPrints.setPrints(prints);
		return cardPrints;
	}

	private class YugipediaCardHandller
		implements PaginatorStreamFactory.PageHandler<QueryResponse> {

		@Override
		public QueryResponse getNext(final QueryResponse previous)
			throws Throwable {
			if (previous == null) {
				return doQuery(null);
			}
			final String continueToken = previous.getContinue().gcmcontinue();
			return continueToken == null ? null : doQuery(continueToken);
		}

		private QueryResponse doQuery(final String gcmcontinue) throws IOException {
			final Response<QueryResponse> response = api
				.queryCategoryMembersByTimestamp(
					Category.CARDS,
					providerLimit,
					SortDirection.NEWER,
					gcmcontinue
				)
				.execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			throw new YugipediaException(
				MessageFormat.format(
					"Yugipedia error {0}: {1}",
					response.code(),
					response.errorBody()
				)
			);
		}

		@Override
		public void handleError(Throwable e) {
			PaginatorStreamFactory.PageHandler.super.handleError(e);
			if (e instanceof YugipediaException ype) {
				throw ype;
			}
			throw new YugipediaException("Unexpected error on yugipedia API", e);
		}
	}
}
