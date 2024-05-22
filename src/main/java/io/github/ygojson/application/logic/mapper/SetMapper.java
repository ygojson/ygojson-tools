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
	@Mapping(target = "name", source = "enName")
	@Mapping(target = "nameAlt", source = "enNameAlt")
	@Mapping(target = "setCode", source = "enSetCode")
	@Mapping(target = "setCodeAlt", source = "enSetCodeAlt")
	@Mapping(target = "localizedData.de.name", source = "deName")
	@Mapping(target = "localizedData.es.name", source = "esName")
	@Mapping(target = "localizedData.fr.name", source = "frName")
	@Mapping(target = "localizedData.it.name", source = "itName")
	@Mapping(target = "localizedData.ja.name", source = "jaName")
	@Mapping(target = "localizedData.ko.name", source = "koName")
	@Mapping(target = "localizedData.pt.name", source = "ptName")
	@Mapping(target = "localizedData.zhHans.name", source = "zhHansName")
	@Mapping(target = "localizedData.zhHant.name", source = "zhHantName")
	@Mapping(target = "localizedData.de.setCode", source = "deSetCode")
	@Mapping(target = "localizedData.es.setCode", source = "esSetCode")
	@Mapping(target = "localizedData.fr.setCode", source = "frSetCode")
	@Mapping(target = "localizedData.it.setCode", source = "itSetCode")
	@Mapping(target = "localizedData.ja.setCode", source = "jaSetCode")
	@Mapping(target = "localizedData.ko.setCode", source = "koSetCode")
	@Mapping(target = "localizedData.pt.setCode", source = "ptSetCode")
	@Mapping(target = "localizedData.zhHans.setCode", source = "zhHansSetCode")
	@Mapping(target = "localizedData.zhHant.setCode", source = "zhHantSetCode")
	@Mapping(target = "localizedData.de.setCodeAlt", source = "deSetCodeAlt")
	@Mapping(target = "localizedData.es.setCodeAlt", source = "esSetCodeAlt")
	@Mapping(target = "localizedData.fr.setCodeAlt", source = "frSetCodeAlt")
	@Mapping(target = "localizedData.it.setCodeAlt", source = "itSetCodeAlt")
	@Mapping(target = "localizedData.ja.setCodeAlt", source = "jaSetCodeAlt")
	@Mapping(target = "localizedData.ko.setCodeAlt", source = "koSetCodeAlt")
	@Mapping(target = "localizedData.pt.setCodeAlt", source = "ptSetCodeAlt")
	@Mapping(
		target = "localizedData.zhHans.setCodeAlt",
		source = "zhHansSetCodeAlt"
	)
	@Mapping(
		target = "localizedData.zhHant.setCodeAlt",
		source = "zhHantSetCodeAlt"
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
