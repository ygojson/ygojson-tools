package io.github.ygojson.application.core.datastore.db.set;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Embeddable localized values for the {@link RawSet}.
 */
@Embeddable
public class RawSetLocalizedData {

	public String name;

	@Column(name = "setcode")
	public String setCode;

	@Column(name = "setcode_alt")
	public String setCodeAlt;
}
