package io.github.ygojson.runtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.github.ygojson.application.util.http.ClientFactory;
import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.application.yugipedia.client.params.Limit;

/**
 * Configuration for Yugipedia client/provider.
 */
@ApplicationScoped
public class YugipediaConfig {

	public static final String LIMIT_PROPERTY = "ygojson.yugipedia.limit";
	public static final String URL_PROPERTY = "ygojson.yugipedia.url";

	@ConfigProperty(name = LIMIT_PROPERTY)
	Integer limit;

	@ConfigProperty(name = URL_PROPERTY, defaultValue = YugipediaClient.YUGIPEDIA_URL)
	String url;

	/**
	 * Produces the limit for the {@link io.github.ygojson.application.yugipedia.YugipediaProvider}.
	 *
	 * @return the limit.
	 */
	@Produces
	@Dependent
	Limit limit() {
		// using the max limit for production
		if (limit == null) {
			return Limit.getMax();
		}
		return Limit.of(limit);
	}

	/**
	 * Produces the {@link YugipediaClient} from the {@link ClientFactory}.
	 *
	 * @param factory the {@link ClientFactory}
	 *
	 * @return the client.
	 */
	@Produces
	@Dependent
	YugipediaClient yugipediaClient(final ClientFactory factory) {
		return factory.getClient(YugipediaClient.getConfig(url));
	}
}
