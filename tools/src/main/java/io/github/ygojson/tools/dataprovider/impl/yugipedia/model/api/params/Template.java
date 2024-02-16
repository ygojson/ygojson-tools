package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params;

public enum Template {

	SETS("Infobox_set");

	private static final String TEMPLATE_PREFIX = "Template:";

	private final String value;

	Template(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return TEMPLATE_PREFIX + value;
	}
}
