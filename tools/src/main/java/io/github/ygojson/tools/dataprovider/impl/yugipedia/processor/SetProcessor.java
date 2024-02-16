package io.github.ygojson.tools.dataprovider.impl.yugipedia.processor;

import java.util.stream.Stream;

import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.YugipediaSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.InfoboxSetMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;

public class SetProcessor extends QueryResponseWikitextProcessor<Set> {

	private final InfoboxSetMapper infoboxSetMapper = Mappers.getMapper(InfoboxSetMapper.class);
	private final YugipediaSetMapper setMapper = Mappers.getMapper(YugipediaSetMapper.class);

	@Override
	protected Stream<Set> processWikitext(long pageId, String title, String wikitextContent) {
		final InfoboxSet infoboxSet = infoboxSetMapper.mapWikitextToInfoboxSet(wikitextContent);
		final Set set = setMapper.mapToSet(infoboxSet, title);
		return Stream.of(set);
	}
}
