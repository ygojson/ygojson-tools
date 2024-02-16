package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.PrintData;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaCardMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaPrintMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.CardTable2Mapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Page;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.CardTable2;

public class FetchDataProcessor extends QueryResponseWikitextProcessor<PrintData> {

	private final CardTable2Mapper cardTable2Mapper = Mappers.getMapper(CardTable2Mapper.class);

	private final YugipediaCardMapper cardMapper = Mappers.getMapper(YugipediaCardMapper.class);
	private final YugipediaPrintMapper printMapper = Mappers.getMapper(YugipediaPrintMapper.class);

	@Override
	protected boolean filterPage(final Page page) {
		// TODO: maybe we need to filter out some for the case of recent-changes.
		return false;
	}

	@Override
	protected Stream<PrintData> processWikitext(final long pageId,
												final String title,
												final String content
	) {
		try {
			final CardTable2 cardTable2 = cardTable2Mapper.mapWikitextToCardTable2(content);
			final Card card = cardMapper.mapToCard(cardTable2, title, pageId);
			final List<Print> prints = printMapper.mapToPrints(cardTable2);
			final Set set = null; // TODO: use the print-mapper to be sure that we can get the set-title from the CardTable2
			return prints.stream()
				.map(print -> new PrintData(print, card, set));
		} catch (Exception e) {
			// TODO: better exception types
			throw new RuntimeException(MessageFormat.format(
				"Error while processing yugipedia ''{0}'' (pageid={1})",
				title,	pageId),
				e);
		}
	}
}
