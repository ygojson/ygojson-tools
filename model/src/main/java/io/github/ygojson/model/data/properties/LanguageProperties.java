package io.github.ygojson.model.data.properties;

/**
 * Properties related with the language.
 */
public class LanguageProperties {

	private LanguageProperties() {
		// contant class
	}

	/**
	 * Property for the language.
	 * </br>
	 * Should be used on a model if it is assinged to a language
	 * and the type is {@link io.github.ygojson.model.data.definitions.localization.Language}.
	 */
	public static final String LANGUAGE = "language";

	/**
	 * Property for localized data on different models.
	 */
	public static final String LOCALIZED_DATA = "localizedData";

	/**
	 * English localization property.
	 * <br>
	 * Note: this is the default and is not used in the localized data.
	 */
	public static final String EN = "en";

	/**
	 * German localization property.
	 */
	public static final String DE = "de";

	/**
	 * Spanish localization property.
	 */
	public static final String ES = "es";

	/**
	 * French localization property.
	 */
	public static final String FR = "fr";

	/**
	 * Italian localization property.
	 */
	public static final String IT = "it";

	/**
	 * Japanese localization property.
	 */
	public static final String JA = "ja";

	/**
	 * Korean localization property.
	 */
	public static final String KO = "ko";

	/**
	 * Portuguese localization property.
	 */
	public static final String PT = "pt";

	/**
	 * Chinese localization property.
	 */
	public static final String ZH = "zh";
}
