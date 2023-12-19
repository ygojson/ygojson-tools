package io.github.ygojson.model.data.propertie;

/**
 * Properties related with common fields shared
 * across models.
 */
public class IdProperties {

	/**
	 * YGOJSON ID property for the actual model.
	 */
	public static final String SELF_ID = "id";

	/**
	 * YGOJSON ID property for the linked card model.
	 */
	public static final String CARD_ID = "cardId";

	/**
	 * YGOJSON ID property for the linked print model.
	 */
	public static final String PRINT_ID = "printId";

	/**
	 * YGOJSON ID property for the linked set model.
	 */
	public static final String SET_ID = "setId";

	private IdProperties() {
		// contant class
	}
}
