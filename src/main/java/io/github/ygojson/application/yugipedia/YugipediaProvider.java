package io.github.ygojson.application.yugipedia;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import io.smallrye.mutiny.Multi;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.client.params.Template;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.application.yugipedia.processor.YugipediaProcessor;

/**
 * Yugipedia provider to fetch the Yugipedia model.
 */
public class YugipediaProvider {

	private final YugipediaClient client;
	private final Limit limit;

	public YugipediaProvider(final YugipediaClient client, final Limit limit) {
		this.client = client;
		this.limit = limit;
	}

	/**
	 * Fetch the sets from Yugipedia as a map of properties.
	 *
	 * @return lazy-loaded stream with the sets.
	 */
	public Multi<Map<String, YugipediaProperty>> fetchSets() {
		final YugipediaProcessor processor =
			YugipediaProcessor.createSetProcessor();
		// context
		final AtomicReference<String> nextToken = new AtomicReference<>(null);
		return Multi
			.createBy()
			.repeating()
			.uni( //
				() -> nextToken, // starting supplier
				state ->
					client.queryPagesWithTemplateReactive(
						Template.SETS,
						limit,
						nextToken.get()
					)
			)
			.whilst(page -> {
				if (page.getContinue() != null) {
					nextToken.set(page.getContinue().geicontinue());
					return true;
				}
				return false;
			})
			.flatMap(processor::processQueryReactive);
	}
}
