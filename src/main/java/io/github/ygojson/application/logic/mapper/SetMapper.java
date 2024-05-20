package io.github.ygojson.application.logic.mapper;

import org.mapstruct.*;

import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.SetInfo;
import io.github.ygojson.model.utils.LocalizationUtils;

/**
 * Mapper between the {@link Set} model and the {@link SetEntity}.
 */
@Mapper(
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR,
	componentModel = MappingConstants.ComponentModel.JAKARTA_CDI
)
public abstract class SetMapper {

	private static final SetInfo EMPTY = new SetInfo();

	@Mapping(target = "id", source = "ygojsonId")
	@Mapping(target = "name", source = "nameEn")
	@Mapping(target = "nameAlt", source = "nameAltEn")
	@Mapping(target = "setCode", source = "setCodeEn")
	@Mapping(target = "setCodeAlt", source = "setCodeAltEn")
	@Mapping(target = "localizedData.de.name", source = "nameDe")
	@Mapping(target = "localizedData.es.name", source = "nameEs")
	@Mapping(target = "localizedData.fr.name", source = "nameFr")
	@Mapping(target = "localizedData.it.name", source = "nameIt")
	@Mapping(target = "localizedData.ja.name", source = "nameJa")
	@Mapping(target = "localizedData.ko.name", source = "nameKo")
	@Mapping(target = "localizedData.pt.name", source = "namePt")
	@Mapping(target = "localizedData.zhHans.name", source = "nameZhHans")
	@Mapping(target = "localizedData.zhHant.name", source = "nameZhHant")
	@Mapping(target = "localizedData.de.setCode", source = "setCodeDe")
	@Mapping(target = "localizedData.es.setCode", source = "setCodeEs")
	@Mapping(target = "localizedData.fr.setCode", source = "setCodeFr")
	@Mapping(target = "localizedData.it.setCode", source = "setCodeIt")
	@Mapping(target = "localizedData.ja.setCode", source = "setCodeJa")
	@Mapping(target = "localizedData.ko.setCode", source = "setCodeKo")
	@Mapping(target = "localizedData.pt.setCode", source = "setCodePt")
	@Mapping(target = "localizedData.zhHans.setCode", source = "setCodeZhHans")
	@Mapping(target = "localizedData.zhHant.setCode", source = "setCodeZhHant")
	@Mapping(target = "localizedData.de.setCodeAlt", source = "setCodeAltDe")
	@Mapping(target = "localizedData.es.setCodeAlt", source = "setCodeAltEs")
	@Mapping(target = "localizedData.fr.setCodeAlt", source = "setCodeAltFr")
	@Mapping(target = "localizedData.it.setCodeAlt", source = "setCodeAltIt")
	@Mapping(target = "localizedData.ja.setCodeAlt", source = "setCodeAltJa")
	@Mapping(target = "localizedData.ko.setCodeAlt", source = "setCodeAltKo")
	@Mapping(target = "localizedData.pt.setCodeAlt", source = "setCodeAltPt")
	@Mapping(
		target = "localizedData.zhHans.setCodeAlt",
		source = "setCodeAltZhHans"
	)
	@Mapping(
		target = "localizedData.zhHant.setCodeAlt",
		source = "setCodeAltZhHant"
	)
	public abstract Set toModel(SetEntity entity);

	@InheritInverseConfiguration
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "setCodesEn", ignore = true)
	@Mapping(target = "setCodesDe", ignore = true)
	@Mapping(target = "setCodesEs", ignore = true)
	@Mapping(target = "setCodesFr", ignore = true)
	@Mapping(target = "setCodesIt", ignore = true)
	@Mapping(target = "setCodesJa", ignore = true)
	@Mapping(target = "setCodesKo", ignore = true)
	@Mapping(target = "setCodesPt", ignore = true)
	@Mapping(target = "setCodesZhHans", ignore = true)
	@Mapping(target = "setCodesZhHant", ignore = true)
	public abstract SetEntity toEntity(Set model);

	/**
	 * Updates the model by deleting the empty or duplciated localized data.
	 *
	 * @param model the model to update.
	 */
	@AfterMapping
	protected void deleteEmptyOrDuplicatedLocalizedData(
		@MappingTarget Set model
	) {
		LocalizationUtils
			.getNonMainLanguages()
			.forEach(language -> {
				final SetInfo info = LocalizationUtils.getLocalizedData(
					language,
					model
				);
				if (EMPTY.equals(info)) {
					LocalizationUtils.setLocalizedData(model, null, language);
					return;
				}
				if (
					model.getSetCode() != null &&
					model.getSetCode().equals(info.getSetCode())
				) {
					info.setSetCode(null);
				}
			});
	}
}
