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
	@Mapping(target = "name", source = "en.name")
	@Mapping(target = "nameAlt", source = "enNameAlt")
	@Mapping(target = "setCode", source = "en.setCode")
	@Mapping(target = "setCodeAlt", source = "en.setCodeAlt")
	@Mapping(target = "localizedData.de.name", source = "de.name")
	@Mapping(target = "localizedData.es.name", source = "es.name")
	@Mapping(target = "localizedData.fr.name", source = "fr.name")
	@Mapping(target = "localizedData.it.name", source = "it.name")
	@Mapping(target = "localizedData.ja.name", source = "ja.name")
	@Mapping(target = "localizedData.ko.name", source = "ko.name")
	@Mapping(target = "localizedData.pt.name", source = "pt.name")
	@Mapping(target = "localizedData.zhHans.name", source = "zhHans.name")
	@Mapping(target = "localizedData.zhHant.name", source = "zhHant.name")
	@Mapping(target = "localizedData.de.setCode", source = "de.setCode")
	@Mapping(target = "localizedData.es.setCode", source = "es.setCode")
	@Mapping(target = "localizedData.fr.setCode", source = "fr.setCode")
	@Mapping(target = "localizedData.it.setCode", source = "it.setCode")
	@Mapping(target = "localizedData.ja.setCode", source = "ja.setCode")
	@Mapping(target = "localizedData.ko.setCode", source = "ko.setCode")
	@Mapping(target = "localizedData.pt.setCode", source = "pt.setCode")
	@Mapping(target = "localizedData.zhHans.setCode", source = "zhHans.setCode")
	@Mapping(target = "localizedData.zhHant.setCode", source = "zhHant.setCode")
	@Mapping(target = "localizedData.de.setCodeAlt", source = "de.setCodeAlt")
	@Mapping(target = "localizedData.es.setCodeAlt", source = "es.setCodeAlt")
	@Mapping(target = "localizedData.fr.setCodeAlt", source = "fr.setCodeAlt")
	@Mapping(target = "localizedData.it.setCodeAlt", source = "it.setCodeAlt")
	@Mapping(target = "localizedData.ja.setCodeAlt", source = "ja.setCodeAlt")
	@Mapping(target = "localizedData.ko.setCodeAlt", source = "ko.setCodeAlt")
	@Mapping(target = "localizedData.pt.setCodeAlt", source = "pt.setCodeAlt")
	@Mapping(
		target = "localizedData.zhHans.setCodeAlt",
		source = "zhHans.setCodeAlt"
	)
	@Mapping(
		target = "localizedData.zhHant.setCodeAlt",
		source = "zhHant.setCodeAlt"
	)
	public abstract Set toModel(SetEntity entity);

	@InheritInverseConfiguration
	@Mapping(target = "id", ignore = true)
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
