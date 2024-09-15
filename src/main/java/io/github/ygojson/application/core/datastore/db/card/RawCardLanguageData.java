package io.github.ygojson.application.core.datastore.db.card;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Embeddable localized values for the {@link RawCard}.
 */
@Embeddable
public class RawCardLanguageData {

	public String name;

	@Column(name = "effect_text")
	public String effectText;

	@Column(name = "flavor_text")
	public String flavorText;

	@Column(name = "materials_text")
	public String materialsText;

	@Column(name = "pendulum_effect_text")
	public String pendulumEffectText;
}
