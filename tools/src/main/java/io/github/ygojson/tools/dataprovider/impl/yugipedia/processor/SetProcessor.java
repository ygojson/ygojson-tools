package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.InfoboxSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.Page;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

/**
 * Processor for the {@link Set} model.
 */
public class SetProcessor extends QueryResponseWikitextProcessor<Set> {

	private static List<String> ACCEPTED_MEDIUM = List.of(
		"tcg",
		"ocg"
	);

	private final InfoboxSetMapper infoboxSetMapper = Mappers.getMapper(
		InfoboxSetMapper.class
	);
	private final YugipediaSetMapper setMapper = Mappers.getMapper(
		YugipediaSetMapper.class
	);

	@Override
	protected String getModelName() {
		return "set";
	}

	@Override
	protected boolean filterPage(Page page) {
		final String lowerCaseTitle = page.title().toLowerCase();
		if (lowerCaseTitle.contains("(series)")) {
			return true;
		}
		// TODO: this should not be filtered, but doing it to test
		if (lowerCaseTitle.contains("(25th anniversary edition)")) {
			return true;
		}
		return super.filterPage(page);
	}

	@Override
	protected Stream<Set> processWikitext(
		long pageId,
		String title,
		String wikitextContent
	) {
		final InfoboxSet infoboxSet = infoboxSetMapper.mapWikitextToInfoboxSet(
			wikitextContent
		);
		if (infoboxSet.medium() != null) {
			final String medium = infoboxSet.medium().withoutMarkup().toLowerCase();
			if (!ACCEPTED_MEDIUM.contains(medium)) {
				return Stream.of();
				//return invalidStream(pageId, title, "medium is " + medium);
			}
		}
		if (infoboxSet.appears_in_video_games() != null) {
			final String videoGames = infoboxSet.appears_in_video_games().withoutMarkup();
			if (videoGames != null && !videoGames.isEmpty()) {
				return invalidStream(pageId, title, "appears_in_video_games is " + infoboxSet.appears_in_video_games());
			}
		}
		final Set set = setMapper.mapToSet(infoboxSet, title);
		return Stream.of(set);
	}
}
