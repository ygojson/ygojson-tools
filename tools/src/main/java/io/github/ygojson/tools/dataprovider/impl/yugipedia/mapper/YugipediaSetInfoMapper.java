package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import io.github.ygojson.model.data.definition.SetInfo;
import io.github.ygojson.model.data.definition.localization.Region;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper.wikitext.MarkupStringMapper;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext.InfoboxSet;

@Mapper
public abstract class YugipediaSetInfoMapper {

	private static final Function<InfoboxSet, String> NO_ALT_NAME = infoboxSet ->
		null;

	protected MarkupStringMapper markupStringMapper = Mappers.getMapper(
		MarkupStringMapper.class
	);

	@Named("mainSetInfo")
	protected SetInfo mapToMainSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::en_name,
			InfoboxSet::en_name_2,
			model ->
				prefixesAsList(
					model.prefix(),
					model.en_prefix(),
					model.eu_prefix(),
					model.na_prefix(),
					model.oc_prefix()
				)
		);
	}

	@Named("esSetInfo")
	protected SetInfo mapEsSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::es_name,
			NO_ALT_NAME,
			model -> prefixesAsList(model.sp_prefix())
		);
	}

	@Named("deSetInfo")
	protected SetInfo mapDeSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::de_name,
			NO_ALT_NAME,
			model -> prefixesAsList(model.de_prefix())
		);
	}

	@Named("frSetInfo")
	protected SetInfo mapFrSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::fr_name,
			NO_ALT_NAME,
			model -> prefixesAsList(model.fr_prefix(), model.fc_prefix())
		);
	}

	@Named("itSetInfo")
	protected SetInfo mapItSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::it_name,
			NO_ALT_NAME,
			model -> prefixesAsList(model.it_prefix())
		);
	}

	@Named("jaSetInfo")
	protected SetInfo mapJaSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			model -> markupStringMapper.map(model.ja_name()),
			NO_ALT_NAME,
			model -> prefixesAsList(model.jp_prefix(), model.ja_prefix())
		);
	}

	@Named("koSetInfo")
	protected SetInfo mapKoSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			model -> markupStringMapper.map(model.ko_name()),
			NO_ALT_NAME,
			model -> prefixesAsList(model.kr_prefix())
		);
	}

	@Named("ptSetInfo")
	protected SetInfo mapPtSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::pt_name,
			NO_ALT_NAME,
			model -> prefixesAsList(model.pt_prefix())
		);
	}

	@Named("zhHansSetInfo")
	protected SetInfo mapZhHansSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::sc_name,
			NO_ALT_NAME,
			model -> prefixesAsList(model.sc_prefix())
		);
	}

	@Named("zhHantSetInfo")
	protected SetInfo mapZhHantSetInfo(final InfoboxSet infoboxSet) {
		return mapToSetInfo(
			infoboxSet, // the infoboxSet
			InfoboxSet::tc_name,
			NO_ALT_NAME,
			model -> prefixesAsList(model.tc_prefix())
		);
	}

	// helper method to avoid NPEs on List.of
	private List<String> prefixesAsList(final String... prefixes) {
		return Stream.of(prefixes).filter(Objects::nonNull).toList();
	}

	protected SetInfo mapToSetInfo(
		final InfoboxSet infoboxSet,
		final Function<InfoboxSet, String> nameGetter,
		final Function<InfoboxSet, String> nameAltGetter,
		final Function<InfoboxSet, List<String>> prefixesGetter
	) {
		final String name = nameGetter.apply(infoboxSet);
		final String nameAlt = nameAltGetter.apply(infoboxSet);
		final List<String> prefixes = prefixesGetter.apply(infoboxSet);
		if (name == null && prefixes.isEmpty()) {
			return null;
		}
		final SetInfo info = new SetInfo();
		info.setName(name);
		info.setNameAlt(nameAlt);
		if (!prefixes.isEmpty()) {
			final List<PrefixData> prefixDataList = prefixes
				.stream()
				.filter(Objects::nonNull)
				.map(PrefixData::ofPrefix)
				.toList();
			final List<String> codes = prefixDataList
				.stream()
				.map(PrefixData::setCode)
				.distinct()
				.toList();
			// final List<Region> regions = prefixDataList.stream().map(PrefixData::getRegion).distinct().toList();
			switch (codes.size()) {
				case 0 -> {}
				case 1 -> info.setSetCode(codes.getFirst());
				case 2 -> {
					info.setSetCode(codes.getFirst());
					info.setSetCodeAlt(codes.get(1));
				}
				default -> throw new IllegalStateException(
					"More than 2 set codes for set: " + name
				);
			}
		}
		return info;
	}

	// TODO: extract as it might be useful for the prints too
	record PrefixData(String setCode, String regionCode, String printNumber) {
		private static final Pattern SET_CODE_SPLIT_PATTERN = Pattern.compile("-");

		public static PrefixData ofPrefix(final String prefix) {
			return ofSplit(SET_CODE_SPLIT_PATTERN.split(prefix, 2));
		}

		public static PrefixData ofCardNumber(final String cardNumber) {
			return ofSplit(SET_CODE_SPLIT_PATTERN.split(cardNumber, 3));
		}

		private static PrefixData ofSplit(final String[] split) {
			return switch (split.length) {
				case 0 -> new PrefixData(null, null, null);
				case 1 -> new PrefixData(split[0], null, null);
				case 2 -> new PrefixData(split[0], split[1], null);
				case 3 -> new PrefixData(split[0], split[1], split[2]);
				default -> throw new IllegalStateException(
					"Too many split parts: " + Arrays.toString(split)
				);
			};
		}

		Region getRegion() {
			if (regionCode == null) {
				return null;
			}
			return Region.valueOf(regionCode);
		}
	}
}
