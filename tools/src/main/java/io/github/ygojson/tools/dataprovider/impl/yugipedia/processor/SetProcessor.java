package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.InfoboxSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;
import org.mapstruct.factory.Mappers;

import java.util.stream.Stream;

/**
 * Processor for the {@link Set} model.
 */
public class SetProcessor extends QueryResponseWikitextProcessor<Set> {

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
	protected Stream<Set> processWikitext(
		long pageId,
		String title,
		String wikitextContent
	) {
		final InfoboxSet infoboxSet = infoboxSetMapper.mapWikitextToInfoboxSet(
			title,
			wikitextContent
		);
		final Set set = setMapper.mapToSet(infoboxSet, title);
		return Stream.of(set);
	}
}
