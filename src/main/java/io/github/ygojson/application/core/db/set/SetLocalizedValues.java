package io.github.ygojson.application.core.db.set;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Embeddable localized values for the {@link SetEntity}.
 */
@Embeddable
public class SetLocalizedValues {

	public String name;

	@Column(name = "setcode")
	public String setCode;

	@Column(name = "setcode_alt")
	public String setCodeAlt;
}
