package io.github.ygojson.application.yugipedia.mapper.language;

import java.util.List;

/**
 * Factory class for language handlers.
 */
public final class LanguageHandlerFactory {

	private LanguageHandlerFactory() {
		// factory class
	}

	public static List<LanguageHandler> createAll() {
		return List.of(
			new DeLanguageHandler(),
			new EnLanguageHandler(),
			new EsLanguageHandler(),
			new FrLanguageHandler(),
			new ItLanguageHandler(),
			new JaLanguageHandler(),
			new KoLanguageHandler(),
			new PtLanguageHandler(),
			new ZhHansLanguageHandler(),
			new ZhHantLanguageHandler()
		);
	}
}
