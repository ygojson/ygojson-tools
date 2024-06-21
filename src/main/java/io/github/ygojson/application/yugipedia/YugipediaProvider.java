package io.github.ygojson.application.yugipedia;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import io.smallrye.mutiny.Multi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.params.Limit;
import io.github.ygojson.application.yugipedia.client.params.Template;
import io.github.ygojson.application.yugipedia.client.response.Continue;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;
import io.github.ygojson.application.yugipedia.parser.model.YugipediaProperty;
import io.github.ygojson.application.yugipedia.processor.YugipediaProcessor;

/**
 * Yugipedia provider to fetch the Yugipedia model.
 */
public class YugipediaProvider {

	private static final Logger LOG = LoggerFactory.getLogger(
		YugipediaProvider.class
	);

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
		final AtomicReference<String> state = new AtomicReference<>(null);
		return Multi
			.createBy()
			.repeating()
			.uni( //
				() -> state, // starting supplier
				nextState ->
					client.queryPagesWithTemplate(Template.SETS, limit, nextState.get())
			)
			.whilst(page -> computeAndHasNext(page, state, Continue::geicontinue))
			.flatMap(processor::processQuery);
	}

	private boolean computeAndHasNext(
		final QueryResponse response,
		final AtomicReference<String> state,
		final Function<Continue, String> tokenExtractor
	) {
		final Continue nextContinue = response.getContinue();
		if (nextContinue != null) {
			final String token = tokenExtractor.apply(nextContinue);
			if (token != null) {
				state.set(token);
				return true;
			}
			LOG.warn(
				"Expected continue-token not present (stopping iteration): {}",
				nextContinue
			);
		}
		return false;
	}
}
