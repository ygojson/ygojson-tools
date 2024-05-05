package io.github.ygojson.application.yugipedia.mapper;

import java.util.List;
import java.util.Map;

import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.Region;

enum YugipediaLanguageProps {
	EN(Language.EN, "en", List.of(Region.EN)),
	NA(Language.EN, "na", List.of(Region.EN)),
	EU(Language.EN, "eu", List.of(Region.EN, Region.E)),
	AU(Language.EN, "au", List.of(Region.EN, Region.A)),
	AE(Language.EN, "ae", List.of(Region.EN, Region.AE)),
	DE(Language.DE, "de", List.of(Region.DE, Region.G)),
	ES(Language.ES, "sp", List.of(Region.SP, Region.S)),
	FR(Language.FR, "fr", List.of(Region.FR, Region.F)),
	FC(Language.FR, "fc", List.of(Region.FR, Region.C)),
	IT(Language.IT, "it", List.of(Region.IT, Region.I)),
	JA(Language.JA, "ja", List.of(Region.JA)),
	JP(Language.JA, "jp", List.of(Region.JP)),
	KO(Language.KO, "kr", List.of(Region.KR, Region.K)),
	PT(Language.PT, "pt", List.of(Region.PT, Region.P)),
	ZH_HANS(Language.ZH_HANS, "sc", List.of(Region.SC)),
	ZH_HANT(Language.ZH_HANT, "tc", List.of(Region.TC));

	private final Language language;
	private final String setProperty;
	private final List<Region> possibleRegion;

	YugipediaLanguageProps(
		final Language language,
		final String setPrefix,
		final List<Region> possibleRegions
	) {
		this.language = language;
		this.setProperty = setPrefix + "_sets";
		this.possibleRegion = possibleRegions;
	}

	public Language getLanguage() {
		return language;
	}

	public List<Region> getPossibleRegion() {
		return possibleRegion;
	}

	public YugipediaProperty.SetsProp getProperty(
		final Map<String, YugipediaProperty> properties
	) {
		return (YugipediaProperty.SetsProp) properties.get(setProperty);
	}
}
